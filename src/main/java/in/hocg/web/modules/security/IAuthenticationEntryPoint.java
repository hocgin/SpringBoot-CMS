package in.hocg.web.modules.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class IAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    
    private static final long serialVersionUID = -8970718410437077606L;
    
    
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未授权");
        
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        Results results = Results.error(HttpServletResponse.SC_UNAUTHORIZED, "未授权")
//                .setData(request.getPathInfo());
//        try (PrintWriter writer = response.getWriter()) {
//            writer.write(gson.toJson(results));
//        }
    }
}
