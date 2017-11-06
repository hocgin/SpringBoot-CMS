package in.hocg.web.modules.domain;

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
}
