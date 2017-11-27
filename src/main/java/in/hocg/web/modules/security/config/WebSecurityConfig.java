package in.hocg.web.modules.security.config;

import in.hocg.web.modules.security.details.member.IMemberDetailsService;
import in.hocg.web.modules.security.handler.IAjaxWebSuccessHandler;
import in.hocg.web.modules.security.handler.IWebUnauthorizedEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by hocgin on 2017/11/23.
 * email: hocgin@gmail.com
 * 会员登陆配置
 */
@Configuration
@EnableWebSecurity
@Order(4)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private IMemberDetailsService memberDetailsService;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .exceptionHandling()
                .authenticationEntryPoint(new IWebUnauthorizedEntryPoint()).and()
                .csrf().and()
                .authorizeRequests()
                
                // 允许对于网站静态资源的无授权访问
                .antMatchers(
                        HttpMethod.GET,
                        // 静态资源
                        "/favicon.ico",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.woff",
                        "/**/*.ttf",
                        "/**/*.jpg",
                        "/**/*.png",
                        "/**/*.woff2"
                ).permitAll()
                
                // 允许匿名访问(前台登陆)
                .antMatchers(
                        "/login-modal.html",
                        "/register-modal.html",
                        "/",
                        "/index.html",
                        "/login",
                        "/login.html"
                ).permitAll()
                
                // 除以上连接, 其余都要认证
                .anyRequest().authenticated().and()
                .userDetailsService(memberDetailsService)

//                // 登陆页面
                .formLogin()
//                // 拦截表单登陆的action
                .loginProcessingUrl("/login")
//                // 登陆页面
                .loginPage("/login.html")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(new IAjaxWebSuccessHandler()).and()
                .logout()
                .logoutUrl("/logout")
                .permitAll();
//                // false 为登陆后跳转至之前访问的界面
//                .defaultSuccessUrl("/admin/dashboard/index.html", false)
//                // 登陆失败跳转
//                .failureUrl(String.format("%s?error=true", "/admin/login.html"))
//                .permitAll().and()
//
//                // 自动登陆
//                .rememberMe()
//                .rememberMeParameter("remember-me")
//                .userDetailsService(userDetailsService)
//                .and()
//
//                // 后台退出登陆
//                .logout()
//                .logoutUrl("/admin/logout");
    }
}
