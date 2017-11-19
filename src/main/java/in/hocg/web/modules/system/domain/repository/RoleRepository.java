package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.Role;
import in.hocg.web.modules.system.domain.repository.custom.RoleRepositoryCustom;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
public interface RoleRepository extends DataTablesRepository<Role, String>,
        MongoRepository<Role, String>,
        RoleRepositoryCustom {
    
    List<Role> findAllByRole(String role);
    Role findTopByRole(String role);
    
    void deleteAllByIdIn(String... id);
    
    void deleteAllByDepartmentIn(String... departmentId);
    
    List<Role> findAllByIdIn(String... id);
}
