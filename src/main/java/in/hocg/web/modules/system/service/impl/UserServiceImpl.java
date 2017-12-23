package in.hocg.web.modules.system.service.impl;

import in.hocg.web.modules.base.Base2Service;
import in.hocg.web.modules.system.domain.repository.UserRepository;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.filter.UserQueryDataTablesFilter;
import in.hocg.web.modules.system.service.UserService;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
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
    public DataTablesOutput find(UserQueryDataTablesFilter filter) {
        Criteria criteria = new Criteria();
        List<Criteria> orOperators = new ArrayList<>();
        if (!StringUtils.isEmpty(filter.getNicknameOrUsernameOrIDorEmail())) {
            orOperators.add(Criteria.where("username").regex(String.format("%s.*", filter.getNicknameOrUsernameOrIDorEmail())));
            orOperators.add(Criteria.where("nickname").regex(String.format("%s.*", filter.getNicknameOrUsernameOrIDorEmail())));
            orOperators.add(Criteria.where("id").regex(String.format("%s.*", filter.getNicknameOrUsernameOrIDorEmail())));
            orOperators.add(Criteria.where("email").regex(String.format("%s.*", filter.getNicknameOrUsernameOrIDorEmail())));
        }
        
        if (!orOperators.isEmpty()) {
            criteria.orOperator(orOperators.toArray(new Criteria[0]));
        }
        return repository.findAll(filter, criteria);
    }
}
