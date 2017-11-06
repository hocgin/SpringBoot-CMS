package in.hocg.web.modules.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public class ILoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint
        implements Serializable {
    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public ILoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }
    
    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        return super.determineUrlToUseForThisRequest(request, response, exception);
    }
}
