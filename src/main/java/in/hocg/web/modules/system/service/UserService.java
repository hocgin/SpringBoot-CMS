package in.hocg.web.modules.system.service;

import in.hocg.web.modules.system.domain.user.User;

/**
 * Created by hocgin on 2017/12/16.
 * email: hocgin@gmail.com
 */
public interface UserService {
    
    User findOne(String id);
}
