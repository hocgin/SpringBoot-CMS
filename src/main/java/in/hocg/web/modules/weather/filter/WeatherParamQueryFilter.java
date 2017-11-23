package in.hocg.web.modules.weather.filter;

import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * Created by hocgin on 2017/11/23.
 * email: hocgin@gmail.com
 */
@Data
public class WeatherParamQueryFilter {
    String units;
    String lang;
    
    @Pattern(regexp = "^[\\-\\+]?([0-8]?\\d{1}\\.\\d{1,5}|90\\.0{1,5})$", message = "纬度不符合要求")
    String lat;
    @Pattern(regexp = "^[\\-\\+]?(0?\\d{1,2}\\.\\d{1,5}|1[0-7]?\\d{1}\\.\\d{1,5}|180\\.0{1,5})$", message = "经度不符合要求")
    String lon;
    
}
