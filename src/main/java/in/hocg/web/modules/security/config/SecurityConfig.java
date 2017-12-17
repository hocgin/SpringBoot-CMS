package in.hocg.web.modules.security.config;

import in.hocg.web.modules.security.details.IMemberDetailsService;
import in.hocg.web.modules.security.details.IManagerDetailsService;
import in.hocg.web.modules.security.handler.IAccessDeniedHandler;
import in.hocg.web.modules.security.handler.IWebUnauthorizedEntryPoint;
import in.hocg.web.modules.security.handler.reception.FailureHandler;
import in.hocg.web.modules.security.handler.reception.SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by hocgin on 2017/11/28.
 * email: hocgin@gmail.com @Override
 * public void configure(WebSecurity web) throws Exception {
 * web.ignoring()
 * .antMatchers("/static/**")
 * .antMatchers("/i18n/**");
 * }
 * 安全配置
 */
@EnableWebSecurity
public class SecurityConfig {
    
    private IManagerDetailsService iManagerDetailsService;
    private IMemberDetailsService iMemberDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    public SecurityConfig(IManagerDetailsService iManagerDetailsService,
                          IMemberDetailsService iMemberDetailsService,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.iManagerDetailsService = iManagerDetailsService;
        this.iMemberDetailsService = iMemberDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    @Bean("iManagerAuthenticationProvider")
    DaoAuthenticationProvider iManagerAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        daoAuthenticationProvider.setUserDetailsService(iManagerDetailsService);
        return daoAuthenticationProvider;
    }
    
    @Bean("iMemberAuthenticationProvider")
    DaoAuthenticationProvider iMemberAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        daoAuthenticationProvider.setUserDetailsService(iMemberDetailsService);
        return daoAuthenticationProvider;
    }
    
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    
    
    /**
     * 后台认证
     */
    @Order(1)
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class BackstageSecurityConfig extends WebSecurityConfigurerAdapter {
        private IAccessDeniedHandler accessDeniedHandler;
        private DaoAuthenticationProvider iUserAuthenticationProvider;
        private SessionRegistry sessionRegistry;
        
        @Autowired
        public BackstageSecurityConfig(IAccessDeniedHandler accessDeniedHandler,
                                       SessionRegistry sessionRegistry,
                                       @Qualifier("iManagerAuthenticationProvider") DaoAuthenticationProvider iUserAuthenticationProvider) {
            this.accessDeniedHandler = accessDeniedHandler;
            this.iUserAuthenticationProvider = iUserAuthenticationProvider;
            this.sessionRegistry = sessionRegistry;
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/admin/**")
                    .authenticationProvider(iUserAuthenticationProvider)
                    
                    .csrf()
                    .and()
                    
                    .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler).and()
                    
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated().and()
                    
                    .sessionManagement()
                    .maximumSessions(1)
                    .expiredUrl("/admin/login?expired")
                    .sessionRegistry(sessionRegistry).and()
                    .invalidSessionUrl("/admin/login.html")
                    .and()
                    
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
                    .logoutUrl("/admin/logout")
                    .permitAll();

//            http.addFilterBefore(new TFilter(IUser.class), UsernamePasswordAuthenticationFilter.class);
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
        public ReceptionSecurityConfig(@Qualifier("iMemberAuthenticationProvider") @Lazy DaoAuthenticationProvider iMemberAuthenticationProvider,
                                       SuccessHandler successHandler,
                                       FailureHandler failureHandler) {
            this.iMemberAuthenticationProvider = iMemberAuthenticationProvider;
            this.successHandler = successHandler;
            this.failureHandler = failureHandler;
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/**")
                    .authenticationProvider(iMemberAuthenticationProvider)
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
                            "/dist/**",
                            // -- swagger ui
                            "/swagger-resources/**",
                            "/swagger-ui.html",
                            "/v2/api-docs",
                            "/webjars/**"
                    ).permitAll()
                    
                    // 允许匿名访问(前台登陆)
                    .antMatchers(
                            "/",
                            "/index.html",
                            "/register",
                            "/send-reset-password",
                            "/reset-password.html",
                            "/set-new-password.html",
                            "/set-new-password"
                    ).permitAll()
                    
                    // 除以上连接, 其余都要认证
                    .anyRequest()
                    .authenticated().and()
                    
                    .sessionManagement()
                    .maximumSessions(1).expiredUrl("/index.html?expired").and()
                    .invalidSessionUrl("/index.html")
                    .and()
                    
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
