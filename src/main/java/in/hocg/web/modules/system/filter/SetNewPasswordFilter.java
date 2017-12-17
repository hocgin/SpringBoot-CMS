package in.hocg.web.modules.system.filter;

import in.hocg.web.modules.base.filter.BaseFilter;
import in.hocg.web.modules.base.filter.group.Insert;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * Created by hocgin on 2017/12/17.
 * email: hocgin@gmail.com
 */
@Data
public class SetNewPasswordFilter extends BaseFilter {
    @NotBlank(message = "异常值")
    private String id;
    @Size(min = 3, max = 16, message = "密码长度3~16")
    @NotBlank(message = "密码不能为空")
    private String newPassword;
}
