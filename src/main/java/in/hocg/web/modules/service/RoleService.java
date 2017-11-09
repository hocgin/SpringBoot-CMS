package in.hocg.web.modules.service;

import in.hocg.web.filter.RoleFilter;
import in.hocg.web.filter.RoleDataTablesInputFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.Role;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
public interface RoleService {
    
    DataTablesOutput<Role> data(RoleDataTablesInputFilter input);
    
    void insert(RoleFilter filter, CheckError checkError);
    
    void delete(String... id);
    
    void updateAvailable(String id, boolean b);
    
    Role find(String id);
    
    void updateDescription(RoleFilter filter, CheckError checkError);
    
    void updatePermission(RoleFilter filter, CheckError checkError);
    
    void deleteAllByDepartmentIn(String... DepartmentId);
    
    void removePermissionForAllRole(String... id);
}
