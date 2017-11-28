package in.hocg.web.modules.api;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.weather.body.Forecast;
import in.hocg.web.modules.weather.body.Weather;
import in.hocg.web.modules.weather.filter.WeatherQueryFilter;
import in.hocg.web.modules.weather.service.RequestCacheService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hocgin on 2017/11/28.
 * email: hocgin@gmail.com
 */
@RestController
@RequestMapping("/api/v1")
public class ApiController extends BaseController {
    private RequestCacheService requestCacheService;
    
    @Autowired
    public ApiController(RequestCacheService requestCacheService) {
        this.requestCacheService = requestCacheService;
    }
    
    @ApiOperation(value = "当前天气", notes = "获取当前天气",
            consumes = "application/x-www-form-urlencoded")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "token", dataTypeClass = String.class, paramType = "query",
                    value = "通过注册后获得"),
            @ApiImplicitParam(required = true, name = "lat", dataTypeClass = String.class, paramType = "query",
                    value = "纬度(-90.0000~90.0000, 浮点数)"),
            @ApiImplicitParam(required = true, name = "lon", dataTypeClass = String.class, paramType = "query",
                    value = "经度(-180.0000~180.0000, 浮点数)"),
            @ApiImplicitParam(name = "units", dataTypeClass = String.class, paramType = "query",
                    value = "单位(metric/imperial/kelvin, 默认为:metric)"),
            @ApiImplicitParam(name = "lang", dataTypeClass = String.class, paramType = "query",
                    value = "语言(zh_cn/en/..,默认为:zh_cn)")
    })
    @PostMapping("/current")
    @ResponseBody
    public ResponseEntity<Results<Weather>> current(@Validated WeatherQueryFilter filter) {
        CheckError checkError = CheckError.get();
        Weather weather = requestCacheService.currentWeatherUseToken(filter, checkError);
        return Results.check(checkError)
                .setData(weather)
                .setMessage("当前天气")
                .asOkResponseEntity();
    }
    
    @ApiOperation(value = "天气预报", notes = "获取天气预报",
            consumes = "application/x-www-form-urlencoded")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "token", dataTypeClass = String.class, paramType = "query",
                    value = "通过注册后获得"),
            @ApiImplicitParam(required = true, name = "lat", dataTypeClass = String.class, paramType = "query",
                    value = "纬度(-90.0000~90.0000, 浮点数)"),
            @ApiImplicitParam(required = true, name = "lon", dataTypeClass = String.class, paramType = "query",
                    value = "经度(-180.0000~180.0000, 浮点数)"),
            @ApiImplicitParam(name = "units", dataTypeClass = String.class, paramType = "query",
                    value = "单位(metric/imperial/kelvin, 默认为:metric)"),
            @ApiImplicitParam(name = "lang", dataTypeClass = String.class, paramType = "query",
                    value = "语言(zh_cn/en/..,默认为:zh_cn)")
    })
    @PostMapping("/forecast")
    @ResponseBody
    public ResponseEntity<Results<Forecast>> forecast(@Validated WeatherQueryFilter filter) {
        CheckError checkError = CheckError.get();
        Forecast forecast = requestCacheService.forecastUseToken(filter, checkError);
        return Results.check(checkError)
                .setData(forecast)
                .setMessage("天气预报")
                .asOkResponseEntity();
    }
}
