package in.hocg.web.modules.service.impl;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.Role;
import in.hocg.web.modules.domain.User;
import in.hocg.web.modules.domain.repository.RoleRepository;
import in.hocg.web.modules.domain.repository.UserRepository;
import in.hocg.web.modules.security.IUser;
import in.hocg.web.modules.security.JwtTokenUtil;
import in.hocg.web.modules.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
@Service
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil tokenUtil;
    
    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           UserDetailsService userDetailsService,
                           RoleRepository roleRepository,
                           JwtTokenUtil tokenUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.roleRepository = roleRepository;
        this.tokenUtil = tokenUtil;
    }
    
    @Override
    public User register(User user, CheckError checkError) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            checkError.put("error", "用户已存在");
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = user.getPassword();
        user.setPassword(encoder.encode(rawPassword));
        user.setLastPasswordResetAt(new Timestamp(new Date().getTime()));
        // 分配用户权限
        user.setRole(Collections.singleton(roleRepository.findTopByRole(Role.ROLE_USER)));
        return userRepository.insert(user);
    }
    
    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return tokenUtil.generateToken(userDetails);
    }
    
    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring("Bearer ".length());
        String username = tokenUtil.getUsernameFromToken(token);
        IUser user = (IUser) userDetailsService.loadUserByUsername(username);
        if (tokenUtil.canTokenBeRefreshed(token,
                user.getLastPasswordResetDate())){
            return tokenUtil.refreshToken(token);
        }
        return oldToken;
    }
}
