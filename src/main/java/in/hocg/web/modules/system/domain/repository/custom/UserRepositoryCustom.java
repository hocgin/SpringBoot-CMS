package in.hocg.web.modules.system.domain.repository.custom;

import in.hocg.web.modules.system.domain.User;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public interface UserRepositoryCustom {
    User findByUserNameAvailableTrue(String username);
    
    void removeDepartmentField(String... departmentId);
}
