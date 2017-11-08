package in.hocg.web.modules.service;

import in.hocg.web.filter.RoleQueryFilter;
import in.hocg.web.filter.RoleUpdateInfoFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.Role;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
public interface RoleService {
    
    DataTablesOutput<Role> data(RoleQueryFilter input);
    
    void insert(Role role, String[] permissionIds, CheckError checkError);
    
    void delete(String... id);
    
    void updateAvailable(String id, boolean b);
    
    Role find(String id);
    
    void save(RoleUpdateInfoFilter updateInfoFilter, CheckError checkError);
    
    void save(String id, String[] permissionIds, CheckError checkError);
    
    void deleteAllByDepartmentIn(String... DepartmentId);
}
