package in.hocg.web.modules.security.handler;

import com.google.gson.Gson;
import in.hocg.web.lang.utils.RequestKit;
import in.hocg.web.lang.utils.ResponseKit;
import in.hocg.web.modules.base.body.ResultCode;
import in.hocg.web.modules.base.body.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hocgin on 2017/11/14.
 * email: hocgin@gmail.com
 * 处理权限不足
 */
@Component
public class IAccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    
    private Gson gson;
    
    @Autowired
    public IAccessDeniedHandler(Gson gson) {
        this.gson = gson;
    }
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (!RequestKit.isAjax(request)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, accessDeniedException.getMessage());
//            response.sendRedirect("/index.html");
        }
        ResponseKit.write(response,
                gson.toJson(Results.error(ResultCode.UNAUTHORIZED, "权限不足")
                .setData(accessDeniedException)));
    }
}
