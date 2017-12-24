package in.hocg.web.modules.im.filter;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by hocgin on 2017/12/24.
 * email: hocgin@gmail.com
 */
@Data
public class ChatLogQueryFilter {
    @NotEmpty(message = "不能为空")
    private String id;
    @NotEmpty(message = "不能为空")
    private String type;
    private int size = 5;
    private int page = 1;
}
