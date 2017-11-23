package in.hocg.web.modules.weather.utils;

/**
 * Created by hocgin on 2017/11/22.
 * email: hocgin@gmail.com
 */
public class OpenWeatherMap {
    
    /**
     * 获取当前天气的URL
     * @param appid
     * @param unit
     * @param lang
     * @param lat
     * @param lon
     * @return
     */
    public static String weather(String appid, String unit, String lang, String lon, String lat) {
        return String.format("http://api.openweathermap.org/data/2.5/weather?appid=%s&units=%s&lang=%s&lon=%s&lat=%s", appid, unit, lang, lat, lon);
    }
    
    public static String forecast(String appid, String unit, String lang, String lon, String lat) {
        return String.format("http://api.openweathermap.org/data/2.5/forecast?appid=%s&units=%s&lang=%s&lon=%s&lat=%s", appid, unit, lang, lat, lon);
    }
}
