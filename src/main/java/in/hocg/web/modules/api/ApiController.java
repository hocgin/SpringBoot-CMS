package in.hocg.web.modules.api;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.weather.body.Forecast;
import in.hocg.web.modules.weather.body.Weather;
import in.hocg.web.modules.weather.filter.WeatherQueryFilter;
import in.hocg.web.modules.weather.service.RequestCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hocgin on 2017/11/28.
 * email: hocgin@gmail.com
 */
@RestController
@RequestMapping("/api/v1/weather")
@Api(value = "天气 API", description = "天气 API")
public class ApiController extends BaseController {
    private RequestCacheService requestCacheService;
    
    @Autowired
    public ApiController(RequestCacheService requestCacheService) {
        this.requestCacheService = requestCacheService;
    }
    
    @ApiOperation(value = "当前天气", notes = "获取当前天气, 默认自动定位",
            consumes = "application/x-www-form-urlencoded")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "token", dataTypeClass = String.class, paramType = "query",
                    value = "通过注册后获得"),
            @ApiImplicitParam(required = false, name = "lat", dataTypeClass = String.class, paramType = "query",
                    value = "纬度(-90.0000~90.0000, 浮点数)"),
            @ApiImplicitParam(required = false, name = "lon", dataTypeClass = String.class, paramType = "query",
                    value = "经度(-180.0000~180.0000, 浮点数)"),
            @ApiImplicitParam(name = "units", dataTypeClass = String.class, paramType = "query",
                    value = "单位(metric/imperial/kelvin, 默认为:metric)"),
            @ApiImplicitParam(name = "lang", dataTypeClass = String.class, paramType = "query",
                    value = "语言(zh_cn/en/..,默认为:zh_cn)")
    })
    @RequestMapping(value = "/current", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<Results<Weather>> current(@Validated WeatherQueryFilter filter,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult)
                    .asOkResponseEntity();
        }
        CheckError checkError = CheckError.get();
        Weather weather = requestCacheService.currentWeatherUseToken(filter, checkError);
        return Results.check(checkError, "当前天气")
                .setData(weather)
                .asOkResponseEntity();
    }
    
    @ApiOperation(value = "天气预报", notes = "获取天气预报, 默认自动定位",
            consumes = "application/x-www-form-urlencoded")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, name = "token", dataTypeClass = String.class, paramType = "query",
                    value = "通过注册后获得"),
            @ApiImplicitParam(required = false, name = "lat", dataTypeClass = String.class, paramType = "query",
                    value = "纬度(-90.0000~90.0000, 浮点数)"),
            @ApiImplicitParam(required = false, name = "lon", dataTypeClass = String.class, paramType = "query",
                    value = "经度(-180.0000~180.0000, 浮点数)"),
            @ApiImplicitParam(name = "units", dataTypeClass = String.class, paramType = "query",
                    value = "单位(metric/imperial/kelvin, 默认为:metric)"),
            @ApiImplicitParam(name = "lang", dataTypeClass = String.class, paramType = "query",
                    value = "语言(zh_cn/en/..,默认为:zh_cn)")
    })
    
    @RequestMapping(value = "/forecast", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<Results<Forecast>> forecast(@Validated WeatherQueryFilter filter,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult)
                    .asOkResponseEntity();
        }
        CheckError checkError = CheckError.get();
        Forecast forecast = requestCacheService.forecastUseToken(filter, checkError);
        return Results.check(checkError, "天气预报")
                .setData(forecast)
                .asOkResponseEntity();
    }
}
