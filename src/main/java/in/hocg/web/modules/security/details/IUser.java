package in.hocg.web.modules.security.details;

import in.hocg.web.modules.security.IGrantedAuthority;
import in.hocg.web.modules.system.domain.Role;
import in.hocg.web.modules.system.domain.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 * 用户细节信息
 */
@Data
public class IUser implements UserDetails {
    private final Collection<? extends GrantedAuthority> authorities;
    private final String id;
    private final User user;
    
    public IUser(User user) {
        this.id = user.getId();
        this.user = user;
        this.authorities = getGrantedAuthority(user.getRole());
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
    
    
    @Override
    public String toString() {
        return id;
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }
    
    
    private static List<? extends GrantedAuthority> getGrantedAuthority(Collection<Role> roles) {
        return CollectionUtils.isEmpty(roles) ? Collections.emptyList() : roles.stream()
                .map(IGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
