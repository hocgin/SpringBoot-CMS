package in.hocg.web.modules.system.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.user.User;

/**
 * Created by hocgin on 2017/10/24.
 * email: hocgin@gmail.com
 */
public interface AuthService {
    User register(User user, CheckError checkError);
    
    String login(String username, String password);
    
    String refresh(String oldToken);
}
