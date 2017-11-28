package in.hocg.web.modules.security.config;

import in.hocg.web.modules.security.JwtAuthenticationTokenFilter;
import in.hocg.web.modules.security.details.user.IUserDetailsService;
import in.hocg.web.modules.security.handler.IAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by hocgin on 2017/11/12.
 * email: hocgin@gmail.com
 */
//@EnableWebSecurity
//@Order(2)
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private IAuthenticationEntryPoint unauthorizedHandler;
    
    @Autowired
    private IUserDetailsService userDetailsService;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**").
                // 由于使用的是JWT，我们这里不需要csrf
                csrf().disable()
                
                // 所有请求进行拦截处理
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                
                .authorizeRequests()
                // 允许匿名访问 Token 操作
                .antMatchers("/api/auth/**").permitAll()
                
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated().and()
                
                // 添加JWT 拦截器
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
                
                // 禁用缓存
                .headers().cacheControl();
        
    }
    
    
    
    /**
     * 必须.. 用于验证账号和密码
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // 因为自动登陆, 所以 false
                .eraseCredentials(false)
                // 设置UserDetailsService
                .userDetailsService(userDetailsService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(bCryptPasswordEncoder);
    }
    
    
    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }
}
