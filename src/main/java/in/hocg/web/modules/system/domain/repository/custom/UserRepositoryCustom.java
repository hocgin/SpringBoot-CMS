package in.hocg.web.modules.system.domain.repository.custom;

import in.hocg.web.modules.system.domain.user.User;

import java.util.List;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public interface UserRepositoryCustom {
    
    /**
     * Public
     * @param departmentId
     */
    void removeDepartmentField(String... departmentId);
    
    
    List<User> findAllByRole(String... rolesId);
    
    User findByUserName(String username, User.Type type);
    
    
    
    
    
    User findByEmailForMember(String email);
    
    User findOneByTokenForMember(String token);
    
    void resumeTokenForMember();
}
