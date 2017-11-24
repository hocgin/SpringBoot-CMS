package in.hocg.web.modules.web;

import in.hocg.web.modules.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by hocgin on 2017/11/23.
 * email: hocgin@gmail.com
 */
@Controller
public class IndexController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/web/%s";
    
    @GetMapping({"/", "/index.html"})
    public String vIndex() {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
}
