package in.hocg.web.modules.security;

import in.hocg.web.modules.security.details.user.IUserDetailsService;
import in.hocg.web.modules.security.handler.IAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
//@Configuration
//@EnableWebSecurity
//@Order(2)
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private IAuthenticationEntryPoint unauthorizedHandler;
    @Autowired
    private IUserDetailsService userDetailsService;
    
    /**
     * 用于配置需要认证 or 不需要认证的URL
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                
                // 所有请求进行拦截处理
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                
                // 基于token，所以不需要session
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // 允许匿名访问 Token 操作
                .antMatchers("/auth/**").permitAll()
                // 允许匿名访问 后台登陆
                .antMatchers("/admin/login",
                        SecurityConstants.ADMIN_SIGN_UP_PAGE).permitAll()
                
                // 允许对于网站静态资源的无授权访问
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        // 静态资源
//                        "/*.html",
//                        "/**/*.html",
                        "/favicon.ico",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.woff",
                        "/**/*.ttf",
                        "/**/*.jpg",
                        "/**/*.woff2"
                ).permitAll()
                
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated().and()
        
                .formLogin()
                    .loginPage(SecurityConstants.ADMIN_SIGN_UP_PAGE)
                    .failureUrl(String.format("%s?error=true", SecurityConstants.ADMIN_SIGN_UP_PAGE))
                    .permitAll().and()
                .logout()
                    .logoutUrl("/admin/logout");
        
        // 添加JWT filter
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        
        // 禁用缓存
        http.headers().cacheControl();
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
                .passwordEncoder(passwordEncoder());
    }
    
    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }
    
    /**
     * 装载BCrypt密码编码器
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
