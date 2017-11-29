package in.hocg.web.lang.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hocgin on 2017/11/18.
 * email: hocgin@gmail.com
 */
public class ResponseKit {
    public static HttpServletResponse get() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
    }
    
    
    public static void write(HttpServletResponse response, String text) {
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
