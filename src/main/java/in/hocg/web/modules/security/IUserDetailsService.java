package in.hocg.web.modules.security;

import in.hocg.web.lang.utils.RequestKit;
import in.hocg.web.modules.domain.User;
import in.hocg.web.modules.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 * 获取 后台用户 详细信息
 */
@Service
public class IUserDetailsService implements UserDetailsService{
    private UserRepository userRepository;
    private HttpServletRequest request;
    
    @Autowired
    public IUserDetailsService(UserRepository userRepository,
                               HttpServletRequest request) {
        this.userRepository = userRepository;
        this.request = request;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserNameAvailableTrue(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            user.setLogInIP(RequestKit.getClientIP(request));
            user.setUserAgent(RequestKit.getUserAgent(request));
            user.setLogInAt(new Date());
            userRepository.save(user);
            return IUser.toIUser(user);
        }
    }
}
