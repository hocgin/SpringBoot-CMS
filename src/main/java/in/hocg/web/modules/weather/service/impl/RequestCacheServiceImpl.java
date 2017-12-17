package in.hocg.web.modules.weather.service.impl;

import com.google.gson.Gson;
import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.RequestKit;
import in.hocg.web.modules.system.domain.user.Member;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.service.MemberService;
import in.hocg.web.modules.weather.body.Forecast;
import in.hocg.web.modules.weather.body.Location;
import in.hocg.web.modules.weather.body.Weather;
import in.hocg.web.modules.weather.domain.RequestCache;
import in.hocg.web.modules.weather.domain.repository.RequestCacheRepository;
import in.hocg.web.modules.weather.filter.WeatherParamQueryFilter;
import in.hocg.web.modules.weather.filter.WeatherQueryFilter;
import in.hocg.web.modules.weather.service.RequestCacheService;
import in.hocg.web.modules.weather.utils.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * Created by hocgin on 2017/11/22.
 * email: hocgin@gmail.com
 */
@Service
public class RequestCacheServiceImpl implements RequestCacheService {
    private RequestCacheRepository requestCacheRepository;
    private MemberService memberService;
    private HttpService httpService;
    private Gson gson;
    
    @Autowired
    public RequestCacheServiceImpl(RequestCacheRepository requestCacheRepository,
                                   MemberService memberService,
                                   Gson gson,
                                   HttpService httpService) {
        this.requestCacheRepository = requestCacheRepository;
        this.memberService = memberService;
        this.httpService = httpService;
        this.gson = gson;
    }
    
    /**
     * 获取指定参数的天气状况
     *
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
            ResponseEntity<String> currentWeatherStr = httpService.getCurrentWeather(param);
            Weather currentWeather = gson.fromJson(currentWeatherStr.getBody(), Weather.class);
            if (!ObjectUtils.isEmpty(currentWeather)) {
                _insertWeather(new Point(Double.valueOf(lon), Double.valueOf(lat)), currentWeather, param);
                result = currentWeather;
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
    
    @Override
    public Weather currentWeather(WeatherParamQueryFilter filter, CheckError checkError) {
        return getCurrentWeather(filter.getUnits(), filter.getLang(), filter.getLon(), filter.getLat(), checkError);
    }
    
    @Override
    public Location getLocation(String ip,
                                CheckError checkError) {
        RequestCache requestCache = requestCacheRepository.findTopByParamAndType(ip, RequestCache.Type.Location.name());
        if (ObjectUtils.isEmpty(requestCache)) {
            ResponseEntity<String> locationString = httpService.getLocation(ip);
            Location location = gson.fromJson(locationString.getBody(), Location.class);
            if (!ObjectUtils.isEmpty(location)
                    && 0 == location.getStatus()) {
                _insertLocation(location, ip);
                return location;
            } else {
                checkError.putError("请求服务器异常");
                return null;
            }
        }
        return ((Location) requestCache.getResponse());
    }
    
    @Override
    public List<RequestCache> findWeatherRequestData(Date createdDateBetween, Date createdDateAnd) {
        String[] inType = {
                RequestCache.Type.Current.name(),
                RequestCache.Type.Forecast.name()
        };
        if (ObjectUtils.isEmpty(createdDateBetween) || ObjectUtils.isEmpty(createdDateAnd)) {
            return requestCacheRepository.findAllByTypeIn(inType);
        }
        return requestCacheRepository.findAllByTypeInAndCreatedAtBetween(inType, createdDateBetween, createdDateAnd);
    }
    
    @Override
    public Weather currentWeatherUseToken(WeatherQueryFilter filter, CheckError checkError) {
        if (_checkToken(filter, checkError)) {
            if (StringUtils.isEmpty(filter.getLat())
                    || StringUtils.isEmpty(filter.getLon())) {
                Location location = getLocation(RequestKit.getClientIP(RequestKit.get()), checkError);
                if (!checkError.isPass()) {
                    checkError.putError("无法自动定位位置");
                    return null;
                }
                filter.setLon(location.getContent().getPoint().getX());
                filter.setLat(location.getContent().getPoint().getY());
            }
            return currentWeather(filter, checkError);
        }
        return null;
    }
    
    /**
     * 检验用户 Token
     *
     * @param filter
     * @param checkError
     * @return
     */
    private static final int MAX_COUNT = 2000;
    
    private boolean _checkToken(WeatherQueryFilter filter, CheckError checkError) {
        User member = memberService.findOneByToken(filter.getToken());
        if (!ObjectUtils.isEmpty(member)) {
            Member.Token token = member.getToken();
            if (member.getAvailable()
                    && token.getAvailable()) {
                long count = token.getCount();
                if (count < MAX_COUNT) {
                    token.setCount(count + 1);
                    memberService.update(member);
                    return true;
                } else {
                    checkError.putError(String.format("该Token本月使用次数已达到上限(%d), 请联系客服.", MAX_COUNT));
                    return false;
                }
            } else {
                checkError.putError("该用户或Token被禁止");
                return false;
            }
        } else {
            checkError.putError("Token 异常或失效");
        }
        return false;
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
    
    @Override
    public Forecast forecastUseToken(WeatherQueryFilter filter, CheckError checkError) {
        if (_checkToken(filter, checkError)) {
            if (StringUtils.isEmpty(filter.getLat())
                    || StringUtils.isEmpty(filter.getLon())) {
                Location location = getLocation(RequestKit.getClientIP(RequestKit.get()), checkError);
                if (!checkError.isPass()) {
                    checkError.putError("无法自动定位位置");
                    return null;
                }
                filter.setLon(location.getContent().getPoint().getX());
                filter.setLat(location.getContent().getPoint().getY());
            }
            return forecast(filter, checkError);
        }
        return null;
    }
    
    
    /**
     * 插入当前天气缓存
     *
     * @param point
     * @param weather
     * @param param
     */
    private void _insertWeather(Point point,
                                Weather weather,
                                String param) {
        RequestCache requestCache = new RequestCache()
                .asRequestCache(weather);
        requestCache.setPoint(point);
        requestCache.setParam(param);
        requestCache.setDt(weather.getDt());
        requestCacheRepository.insert(requestCache);
    }
    
    private void _insertForecast(Point point,
                                 Forecast forecast,
                                 String param) {
        RequestCache requestCache = new RequestCache()
                .asRequestCache(forecast);
        requestCache.setPoint(point);
        requestCache.setParam(param);
        requestCacheRepository.insert(requestCache);
    }
    
    private void _insertLocation(Location location,
                                 String param) {
        RequestCache requestCache = new RequestCache()
                .asRequestCache(location);
        requestCache.setParam(param);
        requestCacheRepository.insert(requestCache);
    }
}
