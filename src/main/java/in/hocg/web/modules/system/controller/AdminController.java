package in.hocg.web.modules.system.controller;

import in.hocg.web.global.aspect.ILog;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.system.domain.IFile;
import in.hocg.web.modules.system.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/%s";
    
    @Autowired
    private IFileService iFileService;
    
    @ILog(value = "后台登陆界面")
    @GetMapping({"/login.html", ""})
    public String vLogin() {
        return String.format(BASE_TEMPLATES_PATH, "login");
    }
    
    @Autowired
    private SessionRegistry sessionRegistry;
    
    /**
     * todo 测试 查看当前在线用户
     *
     * @return
     */
    @GetMapping("/all")
    @ResponseBody
    public Results all() {
        for (Object o : sessionRegistry.getAllPrincipals()) {
            System.out.println(o);
        }
        return Results.success();
    }
    
    // TODO: 2017/12/3 测试文件上传框
    @GetMapping("/index.html")
    public String index(Model model) {
        List<IFile> all = iFileService.findAll();
        model.addAttribute("all", all);
        return "/admin/index";
    }
    
}
