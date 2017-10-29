package in.hocg.web.modules.service;

import in.hocg.web.modules.domain.Permission;

import java.util.List;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
public interface PermissionService {
    void insert(Permission permission);
    void delete(String id);
    void update(Permission permission);
    
    List<Permission> queryChildren(String parentId);
    
    List<Permission> queryRoot();
    
    Permission findById(String parentId);
    
    void updateAvailable(String id, boolean b);
}
