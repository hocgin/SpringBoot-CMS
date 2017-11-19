package in.hocg.web.modules.weather.domain;

import in.hocg.web.modules.base.BaseDomain;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hocgin on 2017/11/19.
 * email: hocgin@gmail.com
 */
@Document(collection = "City")
@Data
public class City extends BaseDomain {
    @Transient
    public static String Document = "City";
    
    @Id
    private String id;
    private String code;
    // 省
    private String province;
    // 市
    private String city;
    // 县
    private String county;
    // 经度
    private String longitude;
    // 纬度
    private String latitude;
}
