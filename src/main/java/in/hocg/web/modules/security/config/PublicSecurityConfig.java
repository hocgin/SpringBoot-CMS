package in.hocg.web.modules.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by hocgin on 2017/11/12.
 * email: hocgin@gmail.com
 */
@Configuration
@EnableWebSecurity
@Order(3)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class PublicSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/public/**")
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/public/**/**")
                .permitAll();
        
    }
}
