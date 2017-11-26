package in.hocg.web.modules.system.filter;

import in.hocg.web.modules.base.filter.BaseFilter;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.system.domain.Member;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by hocgin on 2017/11/9.
 * email: hocgin@gmail.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MemberFilter extends BaseFilter {
    /**
     * 仅更新拥有
     */
    @NotBlank(message = "ID异常", groups = {Update.class})
    private String id;
    
    /**
     * 更新 与 增加 均拥有
     */
    @Size(max = 8, message = "昵称长度不能超过8个字符", groups = {Insert.class, Update.class})
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5_]+", message = "昵称只允许由中文、数字、英文、下划线组成", groups = {Insert.class, Update.class})
    private String nickname; // 昵称 [用于显示]
    private boolean available = Boolean.TRUE;
    
    /**
     * 仅增加拥有
     */
    @Email(message = "邮箱格式不正确", groups = {Insert.class})
    private String email;    // 邮箱 [必须, 用于重置密码]
    @Size(max = 16, message = "密码长度不能超过16个字符", groups = {Insert.class})
    @NotBlank(message = "密码不能为空", groups = {Insert.class})
    private String password; // 密码
    
    
    public Member get() {
        Member member = new Member();
        member.setNickname(nickname);
        member.setAvailable(available);
        member.setEmail(email);
        member.createdAt();
        return member;
    }
    
    public Member update(Member member) {
        member.setNickname(nickname);
        member.setAvailable(available);
        member.updatedAt();
        return member;
        
    }
}
