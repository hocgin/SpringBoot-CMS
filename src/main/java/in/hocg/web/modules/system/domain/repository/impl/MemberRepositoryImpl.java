package in.hocg.web.modules.system.domain.repository.impl;

import in.hocg.web.modules.base.BaseMongoCustom;
import in.hocg.web.modules.system.domain.Member;
import in.hocg.web.modules.system.domain.repository.custom.MemberRepositoryCustom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by hocgin on 2017/11/25.
 * email: hocgin@gmail.com
 */
public class MemberRepositoryImpl
        extends BaseMongoCustom<Member, String>
        implements MemberRepositoryCustom {
    @Override
    public Member findByEmailAvailableTrue(String email) {
        Query query = Query.query(Criteria.where("email").is(email)
                .and("available").is(true));
        return findOne(query);
    }
    
    @Override
    public Member findByEmail(String email) {
        Query query = Query.query(Criteria.where("email").is(email));
        return findOne(query);
    }
}
