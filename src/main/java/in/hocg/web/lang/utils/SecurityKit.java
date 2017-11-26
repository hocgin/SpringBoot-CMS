package in.hocg.web.lang.utils;

import in.hocg.web.modules.security.details.user.IUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */
public class SecurityKit {
    
    /**
     * 当前登入的后台管理员信息
     * @return
     */
    public static String username() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof IUser) {
            return ((IUser) principal).getUsername();
        } else if (principal instanceof Principal) {
            return ((Principal) principal).getName();
        }
        return String.valueOf(principal);
    }
    
    public static boolean isLogged() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }
    
    public static IUser iUser() {
        return (IUser) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
