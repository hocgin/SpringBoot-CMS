package in.hocg.web.modules.weather.utils;

/**
 * Created by hocgin on 2017/11/22.
 * email: hocgin@gmail.com
 */
public class OpenWeatherMap {
    
    /**
     * 获取当前天气的URL
     * @param appid
     * @param param
     * @return
     */
    public static String weather(String appid, String param) {
        return String.format("http://api.openweathermap.org/data/2.5/weather?appid=%s&%s", appid, param);
    }
    
    public static String forecast(String appid, String param) {
        return String.format("http://api.openweathermap.org/data/2.5/forecast?appid=%s&%s", appid, param);
    }
}
