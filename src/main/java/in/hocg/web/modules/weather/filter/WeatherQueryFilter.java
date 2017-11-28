package in.hocg.web.modules.weather.filter;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by hocgin on 2017/11/28.
 * email: hocgin@gmail.com
 */
@Data
public class WeatherQueryFilter extends WeatherParamQueryFilter {
    @NotBlank(message = "Token 为必填, 请前往 http://localhost:8080/ 申请")
    private String token;
}
