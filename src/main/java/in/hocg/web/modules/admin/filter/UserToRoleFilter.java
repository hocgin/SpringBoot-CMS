package in.hocg.web.modules.admin.filter;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by hocgin on 2017/11/9.
 * email: hocgin@gmail.com
 * - 添加用户到某角色中
 * - 移除用户从某角色中
 */
@Data
public class UserToRoleFilter implements Serializable{
    
    @NotBlank(message = "角色ID异常")
    private String role;
    @Size(min = 1, message = "用户ID异常")
    private String[] users;
}
