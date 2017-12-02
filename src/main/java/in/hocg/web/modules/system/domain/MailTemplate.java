package in.hocg.web.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.hocg.web.modules.base.BaseDomain;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Created by hocgin on 2017/11/26.
 * email: hocgin@gmail.com
 * 邮件模版
 */
@Data
@Document(collection = "MailTemplate")
public class MailTemplate extends BaseDomain {
    @Id
    private String id;
    private String name; // 模版的名称
    private Map<String, Object> param; // 参数名
    private String defSubject; // 默认标题
    private String description; // 描述
    private String templateString; // 模版内容
    private Map<String, IFile> images; // 嵌入图片
    private Map<String, IFile> files; // 附件
    
    
    @JsonIgnore
    public String getParamString() {
        StringBuffer str = new StringBuffer();
        param.forEach((key, val) -> {
            str.append(String.format("%s=%s\n", key, val));
        });
        return str.toString();
    }
}
