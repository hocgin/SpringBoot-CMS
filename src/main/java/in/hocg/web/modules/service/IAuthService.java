package in.hocg.web.modules.service;

import in.hocg.web.lang.CheckError;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
@Deprecated
public interface IAuthService {
    String login(String username, String password, CheckError checkError);
}
