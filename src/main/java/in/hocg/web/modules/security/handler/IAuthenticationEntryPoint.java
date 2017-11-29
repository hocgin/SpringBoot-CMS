package in.hocg.web.modules.security.handler;

import com.google.gson.Gson;
import in.hocg.web.lang.utils.ResponseKit;
import in.hocg.web.modules.base.body.ResultCode;
import in.hocg.web.modules.base.body.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 处理登陆问题
 */
@Component
public class IAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    
    private static final long serialVersionUID = -8970718410437077606L;
    
    private Gson gson;
    
    @Autowired
    public IAuthenticationEntryPoint(Gson gson) {
        this.gson = gson;
    }
    
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
        String message = authException.getMessage();
        if (authException instanceof BadCredentialsException) {
            message = "用户名/密码错误";
        }
        
        ResponseKit.write(response, gson.toJson(Results.error(ResultCode.BAD_REQUEST, message)
                .setData(authException)));
    }
}
