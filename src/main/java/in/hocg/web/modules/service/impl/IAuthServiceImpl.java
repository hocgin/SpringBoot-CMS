package in.hocg.web.modules.service.impl;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.repository.RoleRepository;
import in.hocg.web.modules.domain.repository.UserRepository;
import in.hocg.web.modules.security.JwtTokenUtil;
import in.hocg.web.modules.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */

@Deprecated
@Service
public class IAuthServiceImpl implements IAuthService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil tokenUtil;
    
    @Autowired
    public IAuthServiceImpl(UserRepository userRepository,
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
    public String login(String username, String password, CheckError checkError) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        if (ObjectUtils.isEmpty(token.getDetails())) {
            checkError.putError("用户名或密码错误");
            return null;
        }
        final Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return tokenUtil.generateToken(userDetails);
    }
}
