package in.hocg.web.modules.system.service.impl;

import in.hocg.web.lang.utils.SecurityKit;
import in.hocg.web.modules.security.details.user.IUser;
import in.hocg.web.modules.system.body.LeftMenu;
import in.hocg.web.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
        if (ObjectUtils.isEmpty(iUser)) {
            return null;
        }
        return userService.getLeftMenu(iUser.getId());
    }
}
