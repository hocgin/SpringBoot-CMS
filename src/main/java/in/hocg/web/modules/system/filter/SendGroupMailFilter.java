package in.hocg.web.modules.system.filter;

import in.hocg.web.modules.base.filter.BaseFilter;
import in.hocg.web.modules.base.filter.group.Insert;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by hocgin on 2017/12/1.
 * email: hocgin@gmail.com
 */
@Data
public class SendGroupMailFilter extends BaseFilter {
    @NotBlank(message = "群发目标为必填", groups = {Insert.class})
    private String target; // 0 前台 1 为后台
    private String role;    // 权限ID
    private String department; // 单位ID
    private String params; // 参数
    private String defSubject; // 标题
    
    
    public boolean isWeb() {
        return "0".equals(target);
    }
    
    public boolean isAdmin() {
        return "1".equals(target);
    }
}
