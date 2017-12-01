package in.hocg.web.modules.system.filter;

import in.hocg.web.lang.utils.StringKit;
import in.hocg.web.modules.base.filter.BaseFilter;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hocgin on 2017/12/1.
 * email: hocgin@gmail.com
 */
@Data
public class SendMailFilter extends BaseFilter {
    @NotBlank(message = "目标为必填")
    private String target; // 0 前台 1 为后台
    private String params; // 参数
    private String defSubject; // 标题
    @Size(min = 1, message = "请选择用户")
    private String[] ids; // 接收者
    
    
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(this.params)) {
            StringKit.lines(this.params).stream()
                    .filter(str -> !StringUtils.isEmpty(str) && str.contains("="))
                    .map(str -> str.split("="))
                    .forEach(map -> {
                        params.put(map[0], map[1]);
                    });
        }
        return params;
    }
    
    public boolean isWeb() {
        return "0".equals(target);
    }
    
    public boolean isAdmin() {
        return "1".equals(target);
    }
}
