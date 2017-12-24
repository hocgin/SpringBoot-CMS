package in.hocg.web.modules.system.controller.system;

import in.hocg.web.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hocgin on 2017/12/21.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/system/user")
public class UserController {
    
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
}
