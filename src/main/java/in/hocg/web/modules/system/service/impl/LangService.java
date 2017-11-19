package in.hocg.web.modules.system.service.impl;

import in.hocg.web.lang.body.response.LeftMenu;
import in.hocg.web.lang.utils.SecurityKit;
import in.hocg.web.modules.security.IUser;
import in.hocg.web.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by hocgin on 2017/11/14.
 * email: hocgin@gmail.com
 */
@Service("langService")
public class LangService {
    UserService userService;
    
    @Autowired
    public LangService(UserService userService) {
        this.userService = userService;
    }
    
    public LeftMenu getLeftMenu() {
        IUser iUser = SecurityKit.iUser();
        return userService.getLeftMenu(iUser.getId());
    }
}
