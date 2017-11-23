package in.hocg.web.modules.weather.domain;

import in.hocg.web.modules.base.BaseDomain;
import in.hocg.web.modules.weather.body.Forecast;
import in.hocg.web.modules.weather.body.Weather;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by hocgin on 2017/11/22.
 * email: hocgin@gmail.com
 */
@Data
@Document(collection = "RequestCache")
public class RequestCache extends BaseDomain {
    @Transient
    public static String Document = "RequestCache";
    
    public enum Type {
        Current, Forecast
    }
    
    
    @Id
    private String id;
    /**
     * 天气位置
     * lon, lat
     */
    private Point point;
    /**
     * 请求字符串
     * unit=xx&lang=xx..
     */
    private String param;
    /**
     * 响应数据 对象
     */
    private Object response;
    /**
     * 当前 / 预报 Type
     */
    private String type;
    
    private Long dt;
    /**
     * 使用次数
     */
    private Integer count = 1;
    
    
    public RequestCache asCurrent(Weather weather) {
        setType(Type.Current.name());
        setResponse(weather);
        createdAt();
        return this;
    }
    
    public RequestCache asForecast(Forecast forecast) {
        setType(Type.Forecast.name());
        setResponse(forecast);
        createdAt();
        return this;
    }
    
}
