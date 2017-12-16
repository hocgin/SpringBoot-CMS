package in.hocg.web.modules.system.domain.repository.custom;

import in.hocg.web.modules.system.domain.user.User;

import java.util.List;

/**
 * Created by hocgin on 2017/11/25.
 * email: hocgin@gmail.com
 */
public interface MemberRepositoryCustom {
    
    List<User> findAllByRoleForMember(String... roleIds);
}
