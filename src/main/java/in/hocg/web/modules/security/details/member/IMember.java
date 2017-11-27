package in.hocg.web.modules.security.details.member;

import in.hocg.web.modules.system.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 * 会员细节信息
 */
public class IMember implements UserDetails {
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Date lastPasswordResetAt;
    
    private IMember(String username,
                    String password,
                    Collection<? extends GrantedAuthority> authorities,
                    Date lastPasswordResetAt) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.lastPasswordResetAt = lastPasswordResetAt;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return username;
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
    
    public Date getLastPasswordResetAt() {
        return lastPasswordResetAt;
    }
    
    public static IMember toIMember(Member custom) {
        return new IMember(custom.getEmail(),
                custom.getPassword(),
                getGrantedAuthority(custom.getRole()),
                custom.getLastPasswordResetAt());
    }
    
    private static List<? extends GrantedAuthority> getGrantedAuthority(Collection<String> roles) {
        return CollectionUtils.isEmpty(roles) ? Collections.emptyList() : roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
