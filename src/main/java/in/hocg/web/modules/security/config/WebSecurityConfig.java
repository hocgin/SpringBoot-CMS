package in.hocg.web.modules.security.config;

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
 */
@Configuration
@EnableWebSecurity
@Order(4)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .csrf().and()
                .authorizeRequests()
                
                // 允许对于网站静态资源的无授权访问
                .antMatchers(
                        HttpMethod.GET,
                        // 静态资源
//                        "/*.html",
//                        "/**/*.html",
                        "/favicon.ico",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.woff",
                        "/**/*.ttf",
                        "/**/*.jpg",
                        "/**/*.png",
                        "/**/*.woff2"
                ).permitAll()
                
                // 允许匿名访问(后台登陆)
                .antMatchers("/",
                        "/index.html",
                        "/login",
                        "/login.html"
                ).permitAll()
                
                // 除以上连接, 其余都要认证
                .anyRequest().authenticated().and();
                
//                // 后台登陆页面
//                .formLogin()
//                // 拦截表单登陆的action
//                .loginProcessingUrl("/admin/login")
//                // 登陆页面
//                .loginPage("/admin/login.html")
//                .usernameParameter("username")
//                .passwordParameter("password")
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
