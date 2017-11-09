package in.hocg.web.modules.service.impl;

import in.hocg.web.filter.PermissionFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.Permission;
import in.hocg.web.modules.domain.repository.PermissionRepository;
import in.hocg.web.modules.service.PermissionService;
import in.hocg.web.modules.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class PermissionServiceImpl extends BaseService implements PermissionService {
    private PermissionRepository permissionRepository;
    private RoleService roleService;
    
    @Autowired
    @Lazy
    public PermissionServiceImpl(PermissionRepository permissionRepository,
                                 RoleService roleService) {
        this.permissionRepository = permissionRepository;
        this.roleService = roleService;
    }
    
    @Override
    public void insert(PermissionFilter filter, CheckError checkError) {
        Permission permission = filter.get();
        String path = "";
        
        // 检测上级权限是否存在
        if (!StringUtils.isEmpty(permission.getParent())) {
            Permission parent = permissionRepository.findOne(permission.getParent());
            if (ObjectUtils.isEmpty(parent)) {
                checkError.putError("上级权限不存在");
                return;
            }
            path = StringUtils.isEmpty(parent.getPath()) ? "" : parent.getPath();
            parent.setHasChildren(true);
            permissionRepository.save(parent);
        }
        
        // 检测权限标识是否已经存在
        if (!ObjectUtils.isEmpty(permissionRepository.findByPermission(permission.getPermission()))) {
            checkError.putError("该权限标识已存在");
            return;
        }
        
        permission.setPath(getSubPath(path));
        permissionRepository.save(permission);
    }
    
    @Override
    public void delete(String id) {
        Permission permission = permissionRepository.findOne(id);
        if (ObjectUtils.isEmpty(permission)) {
            return;
        }
        List<Permission> all = permissionRepository.findAllByPathRegex(String.format("%s.*", (StringUtils.isEmpty(permission.getPath()) ? "" : permission.getPath())));
        String[] ids = all
                .stream()
                .map(Permission::getId)
                .toArray(String[]::new);
        // 删除此权限 及 子权限
        permissionRepository.deleteAllByIdIn(ids);
        if (!StringUtils.isEmpty(permission.getParent())
                && permissionRepository.countByParent(permission.getParent()) < 1) { // 判断是否把父节点设置为根结点
            permissionRepository.updateHasChildren(permission.getParent(), false);
        }
        // 移除角色 与 此权限 及 子权限的关联
        roleService.removePermissionForAllRole(ids);
    }
    
    @Override
    public void update(PermissionFilter filter, CheckError checkError) {
        Permission permission = permissionRepository.findOne(filter.getId());
        
        // 检测权限是否存在
        if (ObjectUtils.isEmpty(permission)) {
            checkError.putError("更新的权限异常");
            return;
        }
        
        // 检测权限标识是否已经存在
        if (!Objects.equals(filter.getPermission(), permission.getPermission())) {
            Permission byPermission = permissionRepository.findByPermission(filter.getPermission());
            if (!ObjectUtils.isEmpty(byPermission)
                    && Objects.equals(byPermission.getId(), permission.getId())) {
                checkError.putError("该权限标识已存在");
                return;
            }
        }
        
        permissionRepository.save(filter.update(permission));
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