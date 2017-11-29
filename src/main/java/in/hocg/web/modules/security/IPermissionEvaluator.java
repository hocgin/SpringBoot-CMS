package in.hocg.web.modules.security;

import in.hocg.web.modules.system.domain.SysMenu;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by hocgin on 2017/10/26.
 * email: hocgin@gmail.com
 */
@Component
public class IPermissionEvaluator implements PermissionEvaluator {
    
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            IGrantedAuthority iGrantedAuthority = (IGrantedAuthority) authority;
            if (ObjectUtils.isEmpty(iGrantedAuthority.getRole())
                    || CollectionUtils.isEmpty(iGrantedAuthority.getRole().getPermissions())) {
                return false;
            }
            for (SysMenu permissionObject : iGrantedAuthority.getRole().getPermissions()) {
                if (Objects.equals(permissionObject.getPermission(), permission)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
