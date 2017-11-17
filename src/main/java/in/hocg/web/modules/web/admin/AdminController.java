package in.hocg.web.modules.web.admin;

import in.hocg.web.global.aspect.ILog;
import in.hocg.web.modules.service.AuthService;
import in.hocg.web.modules.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/%s";
    private AuthService authService;
    
    @Autowired
    public AdminController(AuthService authService) {
        this.authService = authService;
    }
    
    @ILog(value = "测试信息", tag = "后台登陆界面")
    @GetMapping({"/login.html", ""})
    public String vLogin() {
        return String.format(BASE_TEMPLATES_PATH, "login");
    }
}
