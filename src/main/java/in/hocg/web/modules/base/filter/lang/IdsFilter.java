package in.hocg.web.modules.base.filter.lang;

import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by hocgin on 2017/11/8.
 * email: hocgin@gmail.com
 */
@Data
public class IdsFilter implements Serializable {
    @Size(min = 1, message = "异常值")
    private String[] id;
}
