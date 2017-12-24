package in.hocg.web.modules.security.config;

import in.hocg.web.global.config.httpsession.HttpSessionConfig;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Created by hocgin on 2017/12/24.
 * email: hocgin@gmail.com
 */
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
    public SecurityInitializer() {
        super(SecurityConfig.class, HttpSessionConfig.class);
    }
}
