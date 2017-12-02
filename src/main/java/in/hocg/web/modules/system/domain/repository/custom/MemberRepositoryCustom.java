package in.hocg.web.modules.system.domain.repository.custom;

import in.hocg.web.modules.system.domain.Member;

import java.util.List;

/**
 * Created by hocgin on 2017/11/25.
 * email: hocgin@gmail.com
 */
public interface MemberRepositoryCustom {
    
    Member findByEmailAvailableTrue(String email);
    Member findByEmail(String email);
    
    Member findOneByToken(String token);
    
    void resumeToken();
    
    List<Member> findAllByRole(String... roleIds);
}
