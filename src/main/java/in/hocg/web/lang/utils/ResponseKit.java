package in.hocg.web.lang.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by hocgin on 2017/11/18.
 * email: hocgin@gmail.com
 */
public class ResponseKit {
    public static HttpServletResponse get() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
    }
    
}
