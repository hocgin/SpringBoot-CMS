package in.hocg.web.modules.system.service.impl;

import in.hocg.web.modules.base.Base2Service;
import in.hocg.web.modules.base.body.Page;
import in.hocg.web.modules.system.domain.repository.UserRepository;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.service.UserService;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hocgin on 2017/12/16.
 * email: hocgin@gmail.com
 */
@Service
public class UserServiceImpl extends Base2Service<User, String, UserRepository> implements UserService {
    @Override
    public Page<User> findByUsernameOrNicknameOrIDOrMail(String value, int page, int size) {
        Criteria criteria = new Criteria();
        List<Criteria> orOperators = new ArrayList<>();
        if (!StringUtils.isEmpty(value)) {
            orOperators.add(Criteria.where("username").regex(String.format("%s.*", value)));
            orOperators.add(Criteria.where("nickname").regex(String.format("%s.*", value)));
            orOperators.add(Criteria.where("id").is(String.format("%s.*", value)));
            orOperators.add(Criteria.where("email").is(String.format("%s.*", value)));
        }
        if (!orOperators.isEmpty()) {
            criteria.orOperator(orOperators.toArray(new Criteria[0]));
        }
        criteria.andOperator(Criteria.where("available").is(true));
        return repository.pager(Query.query(criteria), page, size);
    }
}
