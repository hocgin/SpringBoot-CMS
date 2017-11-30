package in.hocg.web.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.hocg.web.modules.base.BaseDomain;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */

@Document(collection = "SysTask")
@Data
public class SysTask extends BaseDomain {
    @Transient
    public static String Document = "SysTask";
    
    @Id
    private String id;
    
    private String name; // 定时器名称
    
    private String group; // 分组
    
    private Map<String, Object> params; // 参数，不能为null
    
    private String cron; // 定时规则
    
    private String description; // 备注
    
    private String execClass; // 执行类
    
    private Boolean available = Boolean.FALSE;// 是否可用, 默认不可用。
    
    @JsonIgnore
    public String getParamsString() {
        StringBuffer str = new StringBuffer();
        params.forEach((key, val) -> {
            str.append(String.format("%s=%s\n", key, val));
        });
        return str.toString();
    }
}
