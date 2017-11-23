package in.hocg.web.modules.weather.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.weather.body.Forecast;
import in.hocg.web.modules.weather.body.Weather;
import in.hocg.web.modules.weather.filter.WeatherParamQueryFilter;

/**
 * Created by hocgin on 2017/11/22.
 * email: hocgin@gmail.com
 */
public interface RequestCacheService {
    Weather getCurrentWeather(WeatherParamQueryFilter filter, CheckError checkError);
    
    Forecast forecast(WeatherParamQueryFilter filter, CheckError checkError);
}
