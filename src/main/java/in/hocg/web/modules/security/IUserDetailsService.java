package in.hocg.web.modules.security;

import in.hocg.web.modules.domain.User;
import in.hocg.web.modules.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 * 获取用户详细信息
 */
@Service
public class IUserDetailsService implements UserDetailsService{
    UserRepository userRepository;
    
    @Autowired
    public IUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserNameAvailableTrue(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return IUser.toIUser(user);
        }
    }
}
