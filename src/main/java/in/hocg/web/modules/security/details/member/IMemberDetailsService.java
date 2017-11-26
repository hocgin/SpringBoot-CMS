package in.hocg.web.modules.security.details.member;

import in.hocg.web.lang.utils.RequestKit;
import in.hocg.web.modules.system.domain.Member;
import in.hocg.web.modules.system.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by hocgin on 2017/11/25.
 * email: hocgin@gmail.com
 */
@Service("IMemberDetailsService")
public class IMemberDetailsService implements UserDetailsService {
    private MemberRepository memberRepository;
    private HttpServletRequest request;
    
    @Autowired
    public IMemberDetailsService(MemberRepository memberRepository,
                                 HttpServletRequest request) {
        this.memberRepository = memberRepository;
        this.request = request;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username);
        if (ObjectUtils.isEmpty(member)) {
            // 未找到用户
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        if (!member.getAvailable()) {
            // TODO 被禁止使用
        }
        if (!member.getIsVerifyEmail()) {
            // TODO 邮箱未被校验
        }
    
        member.setLogInIP(RequestKit.getClientIP(request));
        member.setUserAgent(RequestKit.getUserAgent(request));
        member.setLogInAt(new Date());
        memberRepository.save(member);
        return IMember.toIMember(member);
    }
}
