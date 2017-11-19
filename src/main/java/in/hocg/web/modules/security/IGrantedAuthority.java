package in.hocg.web.modules.security;

import in.hocg.web.modules.system.domain.Role;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 * 权限信息
 */
public class IGrantedAuthority implements GrantedAuthority {
    private final Role role;
    
    public IGrantedAuthority(Role role) {
        this.role = role;
    }
    
    public Role getRole() {
        return role;
    }
    
    public String getAuthority() {
        return role.getRole();
    }
}
