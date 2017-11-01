package in.hocg.web.modules.domain.repository;

import in.hocg.web.modules.domain.Role;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
public interface RoleRepository extends DataTablesRepository<Role, String>,
        MongoRepository<Role, String> {
    
    List<Role> findAllByRole(String role);
    Role findTopByRole(String role);
    
    void deleteAllByIdIn(String... id);
}
