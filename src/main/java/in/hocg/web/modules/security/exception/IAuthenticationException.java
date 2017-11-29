package in.hocg.web.modules.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by hocgin on 2017/11/29.
 * email: hocgin@gmail.com
 */
public class IAuthenticationException extends AuthenticationException {
    public IAuthenticationException(String msg) {
        super(msg);
    }
}
