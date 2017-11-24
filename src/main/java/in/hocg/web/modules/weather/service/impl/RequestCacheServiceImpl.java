package in.hocg.web.modules.weather.service.impl;

import com.google.gson.Gson;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.weather.body.Forecast;
import in.hocg.web.modules.weather.body.Weather;
import in.hocg.web.modules.weather.domain.RequestCache;
import in.hocg.web.modules.weather.domain.repository.RequestCacheRepository;
import in.hocg.web.modules.weather.filter.WeatherParamQueryFilter;
import in.hocg.web.modules.weather.service.RequestCacheService;
import in.hocg.web.modules.weather.utils.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;


/**
 * Created by hocgin on 2017/11/22.
 * email: hocgin@gmail.com
 */
@Service
public class RequestCacheServiceImpl implements RequestCacheService {
    private RequestCacheRepository requestCacheRepository;
    private HttpService httpService;
    private Gson gson;
    @Autowired
    public RequestCacheServiceImpl(RequestCacheRepository requestCacheRepository,
                                   Gson gson,
                                   HttpService httpService) {
        this.requestCacheRepository = requestCacheRepository;
        this.httpService = httpService;
        this.gson = gson;
    }
    
    /**
     * 获取指定参数的天气状况
     * @param units
     * @param lang
     * @param lat
     * @param lon
     * @param checkError
     * @return
     */
    public Weather getCurrentWeather(String units, String lang, String lon, String lat,
                                     CheckError checkError) {
        String param = getParam(units, lang, lon, lat);
        Weather result = null;
        // 1. 查看缓存是否有
        // todo 把创建时间改成dt范围内
        RequestCache todayWeather = requestCacheRepository.findByParamOnToday(param, RequestCache.Type.Current.name());
        // 2. 若无, 去请求并缓存
        if (ObjectUtils.isEmpty(todayWeather)) {
            ResponseEntity<Weather> currentWeather = httpService.getCurrentWeather(param);
            if (!ObjectUtils.isEmpty(currentWeather)) {
                _insertWeather(new Point(Double.valueOf(lon), Double.valueOf(lat)), currentWeather, param);
                result = currentWeather.getBody();
            } else {
                checkError.putError("请求服务器异常");
            }
        } else {
            result = (Weather) todayWeather.getResponse();
        }
        return result;
    }
    
    private String getParam(String units, String lang, String lon, String lat) {
        return String.format("units=%s&lang=%s&lon=%s&lat=%s",
                Optional.ofNullable(units).orElse("metric"),
                Optional.ofNullable(lang).orElse("zh_cn"),
                Optional.ofNullable(lon).orElse(""),
                Optional.ofNullable(lat).orElse(""));
    }
    
    
    public Weather getCurrentWeather(WeatherParamQueryFilter filter, CheckError checkError) {
        return getCurrentWeather(filter.getUnits(), filter.getLang(), filter.getLon(), filter.getLat(), checkError);
    }
    
    
    public Forecast forecast(String units, String lang, String lon, String lat,
                             CheckError checkError) {
        String param = getParam(units, lang, lon, lat);
        Forecast result = null;
        // 1. 查看缓存是否有
        RequestCache todayWeather = requestCacheRepository.findByParamOnToday(param, RequestCache.Type.Forecast.name());
        // 2. 若无, 去请求并缓存
        if (ObjectUtils.isEmpty(todayWeather)) {
            ResponseEntity<String> forecastWeatherString = httpService.getForecastWeather(param);
            Forecast forecastWeather = gson.fromJson(forecastWeatherString.getBody(), Forecast.class);
            if (!ObjectUtils.isEmpty(forecastWeather)) {
                _insertForecast(new Point(Double.valueOf(lon), Double.valueOf(lat)), forecastWeather, param);
                result = forecastWeather;
            } else {
                checkError.putError("请求服务器异常");
            }
        } else {
            result = (Forecast) todayWeather.getResponse();
        }
        return result;
    }
    
    public Forecast forecast(WeatherParamQueryFilter filter, CheckError checkError) {
        return forecast(filter.getUnits(), filter.getLang(), filter.getLon(), filter.getLat(), checkError);
    }
    
    
    /**
     * 插入当前天气缓存
     * @param point
     * @param weatherResponseEntity
     * @param param
     */
    private void _insertWeather(Point point,
                                ResponseEntity<Weather> weatherResponseEntity,
                                String param) {
        Weather body = weatherResponseEntity.getBody();
        RequestCache requestCache = new RequestCache()
                .asCurrent(body);
        requestCache.setPoint(point);
        requestCache.setParam(param);
        requestCache.setDt(body.getDt());
        requestCacheRepository.insert(requestCache);
    }
    
    private void _insertForecast(Point point,
                                 Forecast forecast,
                                 String param) {
        RequestCache requestCache = new RequestCache()
                .asForecast(forecast);
        requestCache.setPoint(point);
        requestCache.setParam(param);
        requestCacheRepository.insert(requestCache);
    }
}
