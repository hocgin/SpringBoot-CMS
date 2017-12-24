package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.user.User;
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
    
//    User findByUsernameAndTypeIs(String username, Integer type);
    
    User findOneByUsername(String username);
    User findOneByEmail(String mail);
    
    void deleteAllByIdInAndTypeIs(String[] id, Integer type);
    
    List<User> findAllByIdInAndTypeIs(String[] ids, Integer type);
    
}
