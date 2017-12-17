package in.hocg.web.modules.system.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.MailVerify;
import in.hocg.web.modules.system.domain.user.User;

/**
 * Created by hocgin on 2017/12/17.
 * email: hocgin@gmail.com
 */
public interface MailVerifyService {
    MailVerify create(User user, String code, int minute);
    
    MailVerify create(User user);
    MailVerify findOne(String id);
    
    boolean verify(MailVerify mailVerify, CheckError checkError);
    
}
