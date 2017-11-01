package in.hocg.web.modules.service.impl;

import in.hocg.web.modules.domain.Permission;
import in.hocg.web.modules.domain.repository.PermissionRepository;
import in.hocg.web.modules.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PermissionServiceImpl extends BaseService implements PermissionService {
    PermissionRepository permissionRepository;
    
    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }
    
    @Override
    public void insert(Permission permission) {
        String parentId = permission.getParent();
        String path = "";
        if (!StringUtils.isEmpty(parentId)) {
            Permission parent = permissionRepository.findOne(parentId);
            path = parent.getPath();
            parent.setHasChildren(true);
            permissionRepository.save(parent);
        }
        permission.setPath(getSubPath(path));
        permissionRepository.save(permission);
    }
    
    @Override
    public void delete(String id) {
        Permission permission = permissionRepository.findOne(id);
        // 删除此权限 及 子权限
        List<Permission> all = permissionRepository.findAllByPathRegex(String.format("%s.*", (StringUtils.isEmpty(permission.getPath()) ? "" : permission.getPath())));
        if (all.size() > 1) {
            return;
        }
        String[] ids = all
                .stream()
                .map(Permission::getId)
                .toArray(String[]::new);
        permissionRepository.deleteAllByIdIn(ids);
        if (!StringUtils.isEmpty(permission.getParent())
                && permissionRepository.countByParent(permission.getParent()) < 1) { // 判断是否把父节点设置为根结点
            permissionRepository.updateHasChildren(permission.getParent(), false);
        }
    }
    
    @Override
    public void update(Permission permission) {
        permissionRepository.save(permission);
    }
    
    @Override
    public List<Permission> queryChildren(String parentId) {
        return permissionRepository.findAllByParentIn(parentId);
    }
    
    @Override
    public List<Permission> queryRoot() {
        return permissionRepository.findAllByParentIn(null, "");
    }
    
    @Override
    public Permission findById(String id) {
        return permissionRepository.findOne(id);
    }
    
    @Override
    public List<Permission> queryById(String... id) {
        return permissionRepository.findAllByIdIn(id);
    }
    
    @Override
    public List<Permission> queryAllByIdOrderByPathAes(String... id) {
        return permissionRepository.findAllByIdOrderByPathAes(id);
    }
    
    @Override
    public void updateAvailable(String id, boolean b) {
        Permission permission = permissionRepository.findOne(id);
        if (permission != null) {
            permission.setAvailable(b);
            permissionRepository.save(permission);
        }
    }
    
    /**
     * 获取子路径结构算法
     *
     * @param parentPath
     * @return
     */
    public String getSubPath(String parentPath) {
        List<Permission> all = permissionRepository.findAllByPathRegexOrderByPathDesc(parentPath + ".{4}");
        String rsvalue = parentPath + "0001";
        if (all.size() > 0) {
            rsvalue = all.get(0).getPath();
            int newvalue = Integer.parseInt(rsvalue.substring(rsvalue.length() - 4)) + 1;
            rsvalue = rsvalue.substring(0, rsvalue.length() - 4)
                    + new java.text.DecimalFormat("0000")
                    .format(newvalue);
        }
        return rsvalue;
    }
}