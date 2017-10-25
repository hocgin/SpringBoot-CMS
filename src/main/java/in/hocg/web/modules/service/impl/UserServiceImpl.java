package in.hocg.web.modules.service.impl;

import in.hocg.web.modules.domain.User;
import in.hocg.web.modules.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
@Service
public class UserServiceImpl {
    private UserRepository userRepository;
    
    @Autowired
    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
