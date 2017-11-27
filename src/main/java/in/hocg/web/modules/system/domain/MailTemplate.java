package in.hocg.web.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.hocg.web.modules.base.BaseDomain;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Map;

/**
 * Created by hocgin on 2017/11/26.
 * email: hocgin@gmail.com
 * 邮件模版
 */
@Data
public class MailTemplate extends BaseDomain {
    @Id
    private String id;
    private String name; // 模版的名称
    private Map<String, Object> param; // 参数名
    private IFile template; // 模版文件id iFile
    private String defSubject; // 默认标题
    private String description; // 描述
    
    @JsonIgnore
    public String getParamString() {
        StringBuffer str = new StringBuffer();
        param.forEach((key, val) -> {
            str.append(String.format("%s=%s\n", key, val));
        });
        return str.toString();
    }
}
