package in.hocg.web.modules.security.config;

import in.hocg.web.modules.security.details.member.IMemberDetailsService;
import in.hocg.web.modules.security.details.user.IUserDetailsService;
import in.hocg.web.modules.security.handler.IAccessDeniedHandler;
import in.hocg.web.modules.security.handler.IWebUnauthorizedEntryPoint;
import in.hocg.web.modules.security.handler.reception.FailureHandler;
import in.hocg.web.modules.security.handler.reception.SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by hocgin on 2017/11/28.
 * email: hocgin@gmail.com
 * 安全配置
 */
@EnableWebSecurity
public class SecurityConfig {
    
    private IUserDetailsService iUserDetailsService;
    private IMemberDetailsService iMemberDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    public SecurityConfig(IUserDetailsService iUserDetailsService,
                          IMemberDetailsService iMemberDetailsService,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.iUserDetailsService = iUserDetailsService;
        this.iMemberDetailsService = iMemberDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    @Bean
    DaoAuthenticationProvider iUserAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        daoAuthenticationProvider.setUserDetailsService(iUserDetailsService);
        return daoAuthenticationProvider;
    }
    
    @Bean
    DaoAuthenticationProvider iMemberAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        daoAuthenticationProvider.setUserDetailsService(iMemberDetailsService);
        return daoAuthenticationProvider;
    }
    
    
    /**
     * 后台认证
     */
    @Order(1)
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class BackstageSecurityConfig extends WebSecurityConfigurerAdapter {
        private IAccessDeniedHandler accessDeniedHandler;
        private DaoAuthenticationProvider iUserAuthenticationProvider;
        
        @Autowired
        public BackstageSecurityConfig(IAccessDeniedHandler accessDeniedHandler,
                                       DaoAuthenticationProvider iUserAuthenticationProvider) {
            this.accessDeniedHandler = accessDeniedHandler;
            this.iUserAuthenticationProvider = iUserAuthenticationProvider;
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/admin/**")
                    .csrf().and()
                    
                    .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler).and()
                    
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated().and()
                    
                    .authenticationProvider(iUserAuthenticationProvider)
                    
                    // 登陆
                    .formLogin()
                    .loginProcessingUrl("/admin/login")
                    .loginPage("/admin/login.html")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/admin/dashboard/index.html", false)
                    .failureUrl(String.format("%s?error=true", "/admin/login.html"))
                    .permitAll().and()
                    
                    // 自动登陆
                    .rememberMe()
                    .rememberMeParameter("remember-me")
                    .and()
                    
                    // 退出
                    .logout()
                    .logoutUrl("/admin/logout");
        }
    }
    
    /**
     * 公共
     */
    @Order(2)
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class PublicSecurityConfig extends WebSecurityConfigurerAdapter {
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/public/**")
                    .csrf().disable();
        }
    }
    
    /**
     * 前台认证
     */
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public class ReceptionSecurityConfig extends WebSecurityConfigurerAdapter {
        private DaoAuthenticationProvider iMemberAuthenticationProvider;
        private SuccessHandler successHandler;
        private FailureHandler failureHandler;
        
        @Autowired
        public ReceptionSecurityConfig(DaoAuthenticationProvider iMemberAuthenticationProvider, SuccessHandler successHandler, FailureHandler failureHandler) {
            this.iMemberAuthenticationProvider = iMemberAuthenticationProvider;
            this.successHandler = successHandler;
            this.failureHandler = failureHandler;
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/**")
                    .exceptionHandling()
                    .authenticationEntryPoint(new IWebUnauthorizedEntryPoint())
                    .and()
                    .csrf()
                    .and()
                    
                    .authorizeRequests()
                    // 允许对静态资源无授权访问
                    .antMatchers(
                            HttpMethod.GET,
                            // 静态资源
                            "/favicon.ico",
                            "/web-lte/**",
                            "/admin-lte/**",
                            "/dist/**"
                    ).permitAll()
                    
                    // 允许匿名访问(前台登陆)
                    .antMatchers(
                            "/",
                            "/index.html",
                            "/register"
                    ).permitAll()
                    
                    // 除以上连接, 其余都要认证
                    .anyRequest().authenticated().and()
                    
                    .authenticationProvider(iMemberAuthenticationProvider)
                    
                    .formLogin().loginPage("/login.html")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(successHandler)
                    .failureHandler(failureHandler)
                    .permitAll().and()
                    
                    // 退出
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .permitAll();
        }
    }
}
