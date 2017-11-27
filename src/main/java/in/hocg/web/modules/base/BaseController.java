package in.hocg.web.modules.base;

import org.springframework.web.servlet.ModelAndView;

/**
 * Created by hocgin on 2017/10/8.
 * email: hocgin@gmail.com
 */
public abstract class BaseController {
    
    /**
     * 重定向
     * @param redirect
     * @return
     */
    public String redirect(String redirect) {
        return String.format("redirect:%s", redirect);
    }
    
    
    public ModelAndView vRedirect(String redirect) {
        return new ModelAndView(redirect(redirect));
    }
}
