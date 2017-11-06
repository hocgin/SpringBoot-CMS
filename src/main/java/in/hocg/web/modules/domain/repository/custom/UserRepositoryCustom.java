package in.hocg.web.modules.domain.repository.custom;

import in.hocg.web.modules.domain.User;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public interface UserRepositoryCustom {
    User findByUserNameAvailableTrue(String username);
}
