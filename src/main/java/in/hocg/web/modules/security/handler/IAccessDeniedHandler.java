package in.hocg.web.modules.security.handler;

import com.google.gson.Gson;
import in.hocg.web.lang.body.response.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hocgin on 2017/11/14.
 * email: hocgin@gmail.com
 * 处理权限不足
 */
@Component
public class IAccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    
    @Autowired
    Gson gson;
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(gson.toJson(Results.error(0, "权限不足").setData(accessDeniedException)));
        }
    }
}
