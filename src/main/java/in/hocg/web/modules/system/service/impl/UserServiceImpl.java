package in.hocg.web.modules.system.service.impl;

import in.hocg.web.modules.base.Base2Service;
import in.hocg.web.modules.system.domain.repository.UserRepository;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Created by hocgin on 2017/12/16.
 * email: hocgin@gmail.com
 */
@Service
public class UserServiceImpl extends Base2Service<User, String, UserRepository> implements UserService {
}
