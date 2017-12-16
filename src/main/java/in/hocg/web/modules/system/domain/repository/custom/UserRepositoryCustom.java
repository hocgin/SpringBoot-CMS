package in.hocg.web.modules.system.domain.repository.custom;

import in.hocg.web.modules.system.domain.user.User;

import java.util.List;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public interface UserRepositoryCustom {
    
    /**
     * 公有方法
     */
    void removeDepartmentField(String... departmentId);
    
    List<User> findAllByRole(String... rolesId);
    
    User findByUserName(String username, User.Type type);
    
    
    /**
     * 会员用户
     * @return
     */
    
    User findByEmailForMember(String email);
    
    User findOneByTokenForMember(String token);
    
    void resumeTokenForMember();
}
