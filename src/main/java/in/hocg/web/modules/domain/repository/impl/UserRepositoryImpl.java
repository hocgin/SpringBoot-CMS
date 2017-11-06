package in.hocg.web.modules.domain.repository.impl;

import in.hocg.web.modules.domain.User;
import in.hocg.web.modules.domain.repository.custom.UserRepositoryCustom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public class UserRepositoryImpl
        extends BaseMongoCustom<User, String>
        implements UserRepositoryCustom {
    
    @Override
    public User findByUserNameAvailableTrue(String username) {
        Query query = Query.query(Criteria.where("username").is(username)
                .and("available").is(true));
        return findOne(query);
    }
}
