package in.hocg.web.modules.system.controller.system;

import in.hocg.web.modules.system.filter.UserQueryDataTablesFilter;
import in.hocg.web.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    @PostMapping("/find")
    @ResponseBody
    public DataTablesOutput find(@RequestBody UserQueryDataTablesFilter filter) {
        return userService.find(filter);
    }
}
