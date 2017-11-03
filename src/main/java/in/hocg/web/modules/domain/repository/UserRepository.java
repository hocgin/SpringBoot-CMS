package in.hocg.web.modules.domain.repository;

import in.hocg.web.modules.domain.User;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
public interface UserRepository
        extends MongoRepository<User, String>,
        DataTablesRepository<User, String> {
    
    User findByUsername(String username);
    
    void deleteAllByIdIn(String... username);
    
    List<User> findAllByIdIn(String... ids);
}
