package in.hocg.web.modules.security.details.member;

import in.hocg.web.modules.system.domain.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 * 会员细节信息
 */
@Data
public class IMember implements UserDetails {
    private final String username;
    private final String nickname;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Date lastPasswordResetAt;
    
    private IMember(String username,
                    String nickname,
                    String password,
                    Collection<? extends GrantedAuthority> authorities,
                    Date lastPasswordResetAt) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.authorities = authorities;
        this.lastPasswordResetAt = lastPasswordResetAt;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
                custom.getNickname(),
                custom.getPassword(),
                getGrantedAuthority(custom.getRole()),
                custom.getLastPasswordResetAt());
    }
    
    private static List<? extends GrantedAuthority> getGrantedAuthority(String roles) {
        return Collections.singletonList(new SimpleGrantedAuthority(roles));
    }
}
