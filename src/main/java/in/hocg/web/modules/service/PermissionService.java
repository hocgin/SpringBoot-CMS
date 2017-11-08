package in.hocg.web.modules.service;

import in.hocg.web.filter.PermissionFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.Permission;

import java.util.List;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
public interface PermissionService {
    void insert(PermissionFilter filter, CheckError checkError);
    void delete(String id);
    void update(PermissionFilter filter, CheckError checkError);
    
    List<Permission> queryChildren(String parentId);
    
    List<Permission> queryRoot();
    
    Permission findById(String id);
    
    List<Permission> queryById(String... id);
    List<Permission> queryAllByIdOrderByPathAes(String... id);
    
    void updateAvailable(String id, boolean b);
}
