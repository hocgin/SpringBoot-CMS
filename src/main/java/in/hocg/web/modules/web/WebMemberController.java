package in.hocg.web.modules.web;

import in.hocg.web.modules.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hocgin on 2017/12/17.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/u")
public class WebMemberController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/web/member/%s";
    

}
