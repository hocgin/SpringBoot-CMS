package in.hocg.web.modules.security.handler.reception;

import com.google.gson.Gson;
import in.hocg.web.modules.base.body.ResultCode;
import in.hocg.web.modules.base.body.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hocgin on 2017/11/27.
 * email: hocgin@gmail.com
 */
@Component
public class FailureHandler extends SimpleUrlAuthenticationFailureHandler {
    
    @Autowired
    private Gson gson;
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String message = exception.getMessage();
        if (exception instanceof BadCredentialsException) {
            message = "用户名/密码错误";
        }
    
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            Results<Exception> result = Results.error(ResultCode.BAD_REQUEST, message);
            result.setData(exception);
            writer.write(gson.toJson(result));
        }
    }
}
