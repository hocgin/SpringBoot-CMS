package in.hocg.web.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.Arrays;

/**
 * Created by hocgin on 2017/12/22.
 * email: hocgin@gmail.com
 * 对 Session 进行管理
 */
@Configuration
public class SessionRepositoryConfig {
    @Bean
    public SessionRepository inMemorySessionRepository() {
        return new MapSessionRepository();
    }
    
    @Bean
    public FilterRegistrationBean sessionRepositoryFilterRegistration() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new DelegatingFilterProxy(new SessionRepositoryFilter<>(inMemorySessionRepository())));
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegistrationBean;
    }
}
