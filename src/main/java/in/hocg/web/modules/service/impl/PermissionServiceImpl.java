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
        if (!StringUtils.isEmpty(parentId)) {
            Permission parent = permissionRepository.findOne(parentId);
            parent.setHasChildren(true);
            permissionRepository.save(parent);
        }
        permissionRepository.save(permission);
    }
    
    @Override
    public void delete(String id) {
        Permission permission = permissionRepository.findOne(id);
        permissionRepository.findAllByParentIn(permission.getId())
                .stream()
                .map(Permission::getId)
                .forEach(this::delete);
    
        // 删除此权限 及 子类权限
        permissionRepository.deleteAllByIdIn(id);
        if (!StringUtils.isEmpty(permission.getParent())) { // 判断是否把父节点设置为根结点
            List<Permission> all = permissionRepository.findAllByParentIn(permission.getParent());
            if (all == null
                    || all.size() == 0) {
                Permission parentPermission = permissionRepository.findOne(permission.getParent());
                parentPermission.setHasChildren(false);
                permissionRepository.save(parentPermission);
            }
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
    public void updateAvailable(String id, boolean b) {
        Permission permission = permissionRepository.findOne(id);
        if (permission != null) {
            permission.setAvailable(b);
            permissionRepository.save(permission);
        }
    }
    
}