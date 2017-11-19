package in.hocg.web.modules.admin.domain.repository.custom;

import in.hocg.web.modules.admin.domain.User;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public interface UserRepositoryCustom {
    User findByUserNameAvailableTrue(String username);
    
    void removeDepartmentField(String... departmentId);
}
