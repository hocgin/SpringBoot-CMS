package in.hocg.web.modules.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.Role;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
public interface RoleService {
    
    DataTablesOutput<Role> data(DataTablesInput input);
    
    void insert(Role role, String[] permissionIds, CheckError checkError);
    
    void delete(String... id);
    
    void updateAvailable(String id, boolean b);
    
    Object find(String id);
}
