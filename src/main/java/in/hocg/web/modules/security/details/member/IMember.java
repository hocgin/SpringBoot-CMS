package in.hocg.web.modules.security.details.member;

import in.hocg.web.modules.security.IGrantedAuthority;
import in.hocg.web.modules.system.domain.Member;
import in.hocg.web.modules.system.domain.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    
    
    @Override
    public String toString() {
        return username;
    }
    
    @Override
    public int hashCode() {
        return username.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }
    
    public static IMember toIMember(Member member) {
        return new IMember(member.getEmail(),
                member.getNickname(),
                member.getPassword(),
                getGrantedAuthority(member.getRole()),
                member.getLastPasswordResetAt());
    }
    
    private static List<? extends GrantedAuthority> getGrantedAuthority(Collection<Role> roles) {
        return CollectionUtils.isEmpty(roles) ? Collections.emptyList() : roles.stream()
                .map(IGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
