package in.hocg.web.modules.system.controller.self;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hocgin on 2017/12/19.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/message/remind")
public class RemindController {
    public final String BASE_TEMPLATES_PATH = "/admin/self/remind/%s";
    
    @GetMapping({"/index.html", "/"})
    public String index() {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
}
