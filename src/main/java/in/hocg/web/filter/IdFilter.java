package in.hocg.web.filter;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by hocgin on 2017/11/8.
 * email: hocgin@gmail.com
 */
@Data
public class IdFilter {
    @NotEmpty(message = "异常值")
    private String id;
}
