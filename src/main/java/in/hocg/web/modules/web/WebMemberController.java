package in.hocg.web.modules.web;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.SecurityKit;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.system.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hocgin on 2017/12/17.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/u")
public class WebMemberController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/web/member/%s";
    private MemberService memberService;
    
    @Autowired
    public WebMemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    
    @GetMapping({"", "/"})
    public String index(Model model) {
        model.addAttribute("user", memberService.find(SecurityKit.iUser().getId()));
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @PostMapping("/toggle-token")
    @ResponseBody
    public Results toggleToken() {
        CheckError checkError = CheckError.get();
        String token = memberService.toggleToken(checkError);
        return  Results.check(checkError, "切换 Token 成功")
                .setData(token);
    }

}
