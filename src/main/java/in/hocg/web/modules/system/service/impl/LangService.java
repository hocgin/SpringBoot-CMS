package in.hocg.web.modules.system.service.impl;

import in.hocg.web.lang.utils.SecurityKit;
import in.hocg.web.lang.utils.tree.Node;
import in.hocg.web.modules.security.details.IUser;
import in.hocg.web.modules.system.domain.SysMenu;
import in.hocg.web.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

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
    
    public List<Node<SysMenu>> getLeftMenu() {
        IUser iUser = SecurityKit.iUser();
        if (ObjectUtils.isEmpty(iUser)) {
            return null;
        }
        return userService.getLeftMenu(iUser.getUser().getId());
    }
}
