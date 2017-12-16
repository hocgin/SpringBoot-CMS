package in.hocg.web.modules.system.domain.repository.impl;

import in.hocg.web.modules.base.BaseMongoCustom;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.domain.repository.custom.MemberRepositoryCustom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by hocgin on 2017/11/25.
 * email: hocgin@gmail.com
 */
public class MemberRepositoryImpl
        extends BaseMongoCustom<User, String>
        implements MemberRepositoryCustom {
    
    @Override
    public List<User> findAllByRoleForMember(String... roleIds) {
        return find(Query.query(Criteria.where("role").in(roleIds)
                .and("type").is(User.Type.Member.getCode())));
    }
}
