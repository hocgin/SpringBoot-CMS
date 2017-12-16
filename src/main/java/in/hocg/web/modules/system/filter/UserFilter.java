package in.hocg.web.modules.system.filter;

import in.hocg.web.modules.base.filter.BaseFilter;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.system.domain.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Optional;

/**
 * Created by hocgin on 2017/11/9.
 * email: hocgin@gmail.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserFilter  extends BaseFilter {
    /**
     * 仅更新拥有
     */
    @NotBlank(message = "ID异常", groups = {Update.class})
    private String id;
    
    /**
     * 更新 与 增加 均拥有
     */
    @Size(max = 8, message = "昵称长度不能超过8个字符", groups = {Insert.class, Update.class})
    @Pattern(regexp = "[0-9a-zA-Z\\u4e00-\\u9fa5_]*", message = "昵称只允许由中文、英文、下划线组成", groups = {Insert.class, Update.class})
    private String nickname; // 昵称 [用于显示]
    @NotBlank(message = "邮箱不能为空", groups = {Insert.class, Update.class})
    @Email(message = "邮箱格式不正确", groups = {Insert.class, Update.class})
    private String email;    // 邮箱 [必须, 用于重置密码]
    @NotBlank(message = "所属单位不能为空", groups = {Insert.class, Update.class})
    private String departmentId; // 所属单位
    private Boolean available = Boolean.FALSE; // 是否可用, 默认保留, 不分配。
    
    /**
     * 仅增加拥有
     */
    @Size(max = 8, message = "用户名长度不能超过8个字符", groups = {Insert.class})
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5_]+", message = "用户名只允许由中文、英文、下划线组成", groups = {Insert.class})
    @NotBlank(message = "用户名不能为空", groups = {Insert.class})
    private String username; // 账号 [必须, 唯一, 用于登陆]
    @Size(max = 16, message = "密码长度不能超过16个字符", groups = {Insert.class})
    @NotBlank(message = "密码不能为空", groups = {Insert.class})
    private String password; // 密码
    
    
    public User get() {
        User user = new User();
        user.setNickname(Optional.ofNullable(nickname).orElse(username));
        user.setAvailable(available);
        user.setEmail(email);
        
        user.setCreatedAt(new Date());
        return user;
    }
    
    public User update(User user) {
        user.setNickname(Optional.ofNullable(nickname).orElse(username));
        user.setEmail(email);
        user.setAvailable(available);
        
        user.setUpdatedAt(new Date());
        return user;
        
    }
}
