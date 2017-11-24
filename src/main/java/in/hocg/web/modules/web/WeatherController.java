package in.hocg.web.modules.web;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.weather.body.Forecast;
import in.hocg.web.modules.weather.body.Weather;
import in.hocg.web.modules.weather.filter.WeatherParamQueryFilter;
import in.hocg.web.modules.weather.service.RequestCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hocgin on 2017/11/23.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/public/weather")
public class WeatherController extends BaseController{
    private RequestCacheService service;
    
    @Autowired
    public WeatherController(RequestCacheService service) {
        this.service = service;
    }
    
    @RequestMapping("/current")
    @ResponseBody
    public Results current(WeatherParamQueryFilter filter) {
        CheckError checkError = CheckError.get();
        Weather weather = service.getCurrentWeather(filter, checkError);
        return Results.check(checkError)
                .setData(weather)
                .setMessage("当前天气");
    }
    
    @RequestMapping("/forecast")
    @ResponseBody
    public Results forecast(WeatherParamQueryFilter filter) {
        CheckError checkError = CheckError.get();
        Forecast forecast = service.forecast(filter, checkError);
        return Results.check(checkError)
                .setData(forecast)
                .setMessage("天气预测");
    }
    
}
