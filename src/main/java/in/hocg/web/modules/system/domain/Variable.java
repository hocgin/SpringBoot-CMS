package in.hocg.web.modules.system.domain;

import in.hocg.web.modules.base.BaseDomain;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
@Data
@Document(collection = "Variable")
public class Variable extends BaseDomain {
    @Id
    private String id;
    private String key;
    private String value;
    private String note;
    
    private Boolean builtIn = Boolean.FALSE; // 是否为内置, 默认False
}
