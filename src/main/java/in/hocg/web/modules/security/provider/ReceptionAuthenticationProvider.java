package in.hocg.web.modules.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by hocgin on 2017/11/28.
 * email: hocgin@gmail.com
 * 前台的认证过程
 */
@Deprecated
public class ReceptionAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }
    
    /**
     * 该 AuthenticationProvider 是否可用
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
