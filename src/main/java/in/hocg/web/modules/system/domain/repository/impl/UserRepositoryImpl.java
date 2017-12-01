package in.hocg.web.modules.system.domain.repository.impl;

import in.hocg.web.modules.system.domain.User;
import in.hocg.web.modules.system.domain.repository.custom.UserRepositoryCustom;
import in.hocg.web.modules.base.BaseMongoCustom;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import java.util.List;

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
    public List<User> findAllByDepartmentAndRole(String departmentId, String roleId) {
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(departmentId)) {
            criteria.andOperator(Criteria.where("department.$id").is(new ObjectId(departmentId)));
        }
        if (!StringUtils.isEmpty(roleId)) {
            criteria.andOperator(Criteria.where("role.$id").is(new ObjectId(roleId)));
        }
        return find(Query.query(criteria));
    }
}
