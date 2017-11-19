package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.User;
import in.hocg.web.modules.system.domain.repository.custom.UserRepositoryCustom;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
@Component
public interface UserRepository
        extends MongoRepository<User, String>,
        UserRepositoryCustom,
        DataTablesRepository<User, String> {
    
    User findByUsername(String username);
    
    void deleteAllByIdIn(String... id);
    
    List<User> findAllByIdIn(String... ids);
}
