package in.hocg.web.modules.im.filter;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

/**
 * Created by hocgin on 2017/12/24.
 * email: hocgin@gmail.com
 */
@Data
public class FindFilter implements Serializable{
    @NotEmpty(message = "不能为空")
    private String type;
    @NotEmpty(message = "不能为空")
    private String value;
    private int page = 1;
    private int size = 10;
    
    public boolean isFindUser() {
        return "friend".equals(type);
    }
}
