package in.hocg.web.modules.weather.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.weather.body.Forecast;
import in.hocg.web.modules.weather.body.Location;
import in.hocg.web.modules.weather.body.Weather;
import in.hocg.web.modules.weather.filter.WeatherParamQueryFilter;
import in.hocg.web.modules.weather.filter.WeatherQueryFilter;

/**
 * Created by hocgin on 2017/11/22.
 * email: hocgin@gmail.com
 */
public interface RequestCacheService {
    Weather currentWeather(WeatherParamQueryFilter filter, CheckError checkError);
    
    Weather currentWeatherUseToken(WeatherQueryFilter filter, CheckError checkError);
    
    Forecast forecast(WeatherParamQueryFilter filter, CheckError checkError);
    
    Forecast forecastUseToken(WeatherQueryFilter filter, CheckError checkError);
    
    Location getLocation(String ip,
                         CheckError checkError);
}
