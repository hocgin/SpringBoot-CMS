package in.hocg.web.modules.weather.filter;

import in.hocg.web.modules.base.filter.BaseFilter;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.weather.domain.City;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

/**
 * Created by hocgin on 2017/11/21.
 * email: hocgin@gmail.com
 */
@Data
public class CityFilter extends BaseFilter {
    /**
     * 仅更新拥有
     */
    @NotEmpty(message = "ID异常", groups = {Update.class})
    protected String id;
    
    
    /**
     * 更新 与 增加 均拥有
     */
    @Pattern(regexp = "^[0-9]+", message = "城市码必须为数字", groups = {Update.class, Insert.class})
    private String code;
    // 省
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]+", message = "省必须为中文", groups = {Update.class, Insert.class})
    private String province;
    // 市
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]+", message = "市必须为中文", groups = {Update.class, Insert.class})
    private String city;
    // 县
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]+", message = "县必须为中文", groups = {Update.class, Insert.class})
    private String county;
    // 经度
    @Pattern(regexp = "^[\\-\\+]?(0?\\d{1,2}\\.\\d{1,5}|1[0-7]?\\d{1}\\.\\d{1,5}|180\\.0{1,5})$", message = "经度不符合要求", groups = {Update.class, Insert.class})
    private String longitude;
    // 纬度
    @Pattern(regexp = "^[\\-\\+]?([0-8]?\\d{1}\\.\\d{1,5}|90\\.0{1,5})$", message = "纬度不符合要求", groups = {Update.class, Insert.class})
    private String latitude;
    
    public City get() {
        City city = new City();
        city.setCity(this.city);
        city.setCode(this.code);
        city.setCounty(this.county);
        city.setLatitude(this.latitude);
        city.setLongitude(this.longitude);
        city.setProvince(this.province);
        city.createdAt();
        return city;
    }
    
    public City update(City city) {
        city.setCity(this.city);
        city.setCode(this.code);
        city.setCounty(this.county);
        city.setLatitude(this.latitude);
        city.setLongitude(this.longitude);
        city.setProvince(this.province);
        city.updatedAt();
        return city;
    }
}
