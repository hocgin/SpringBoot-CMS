package in.hocg.web.filter.lang;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by hocgin on 2017/11/8.
 * email: hocgin@gmail.com
 */
@Data
public class IdsFilter implements Serializable {
    @Size(min = 1, message = "异常值")
    @NotBlank(message = "异常值")
    private String[] id;
}
