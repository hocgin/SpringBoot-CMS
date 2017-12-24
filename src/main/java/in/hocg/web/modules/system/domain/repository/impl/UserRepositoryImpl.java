package in.hocg.web.modules.system.domain.repository.impl;

import in.hocg.web.modules.base.BaseMongoCustom;
import in.hocg.web.modules.base.body.Page;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.domain.repository.custom.UserRepositoryCustom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public class UserRepositoryImpl
        extends BaseMongoCustom<User, String>
        implements UserRepositoryCustom {
    
    /**
     * 移除部门
     *
     * @param departmentId
     */
    @Override
    public void removeDepartmentField(String... departmentId) {
        Query query = Query.query(Criteria.where("department").in(departmentId));
        Update update = new Update().set("department", null);
        updateMulti(query, update);
    }
    
    @Override
    public List<User> findAllByRole(String... rolesId) {
        return find(Query.query(Criteria.where("role").in(rolesId)));
    }
    
    @Override
    public User findByUserName(String username, User.Type type) {
        Query query = Query.query(Criteria.where("username").is(username)
                .and("type").is(type.getCode()));
        return findOne(query);
    }
    
    
    @Override
    public User findByEmailForMember(String email) {
        Query query = Query.query(Criteria.where("email").is(email)
                .and("type").is(User.Type.Member.getCode()));
        return findOne(query);
    }
    
    @Override
    public User findOneByTokenForMember(String token) {
        Query query = Query.query(Criteria.where("token.token").is(token)
                .and("type").is(User.Type.Member.getCode()));
        return findOne(query);
    }
    
    @Override
    public void resumeTokenForMember() {
        Query query = Query.query(Criteria.where("token").ne(null)
                .and("type").is(User.Type.Member.getCode()));
        updateMulti(query, Update.update("token.$.count", 0));
    }
    
    @Override
    public Page<User> pager(Query query, int page, int size) {
        return pageX(query, (page - 1) < 0 ? 0 : (page - 1), size);
    }
}
