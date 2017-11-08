package in.hocg.web.modules.web.admin;

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
    
    @GetMapping("/login.html")
    public String vLogin() {
        return String.format(BASE_TEMPLATES_PATH, "login");
    }
    
    
//    @PostMapping("/login")
//    @ResponseBody
//    public Results login(String username, String password,
//                         HttpSession session) {
//        String token = authService.login(username, password);
//        session.setAttribute(SESSION.TOKEN, token);
//        return Results.success()
//                .setMessage(!StringUtils.isEmpty(token) ? "登陆成功" : "登陆失败")
//                .setData("/admin/system/department/index.html");
//    }
}
