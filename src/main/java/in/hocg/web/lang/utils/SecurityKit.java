package in.hocg.web.lang.utils;

import in.hocg.web.modules.security.details.IUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 *
 *
 */
public class SecurityKit {
    
    /**
     * 当前登入的后台管理员信息
     * @return
     */
    public static String username() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    
    public static boolean isLogged() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }
    
    public static Collection<? extends GrantedAuthority> getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }
    
    
    /**
     * - 未登陆
     * - 前台用户
     * - 后台用户
     * @return
     */
    public static IUser iUser() {
        IUser user;
        try {
           user = (IUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
        return user;
    }
}
