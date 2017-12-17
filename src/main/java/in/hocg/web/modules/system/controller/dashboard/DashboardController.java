package in.hocg.web.modules.system.controller.dashboard;

import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.system.body.EChartMapData;
import in.hocg.web.modules.weather.body.Forecast;
import in.hocg.web.modules.weather.body.Weather;
import in.hocg.web.modules.weather.service.RequestCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2017/11/14.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasPermission(null, 'dashboard')")
public class DashboardController extends BaseController {
    private RequestCacheService requestCacheService;
    
    @Autowired
    public DashboardController(RequestCacheService requestCacheService) {
        this.requestCacheService = requestCacheService;
    }
    
    public final String BASE_TEMPLATES_PATH = "/admin/dashboard/%s";
    
    @GetMapping({"/index.html", "/"})
    public String index() {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @RequestMapping({"/weatherRequestData"})
    @ResponseBody
    public Results weatherRequestData(@RequestParam(value = "from", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
                                      @RequestParam(value = "to", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate) {
        Map<String, EChartMapData> data = new HashMap<>();
        requestCacheService.findWeatherRequestData(fromDate, toDate)
                .forEach(requestCache -> {
                    EChartMapData eChartMapData;
                    String cityName;
                    switch (requestCache.getType()) {
                        case "Current":
                            Weather current = (Weather) requestCache.getResponse();
                            cityName = current.getName();
                            if (data.containsKey(cityName)) {
                                eChartMapData = data.get(cityName);
                                eChartMapData.set3Value((int) (eChartMapData.get3Value() + requestCache.getCount()));
                            } else {
                                eChartMapData = new EChartMapData(cityName,
                                        current.getCoord().getLon(),
                                        current.getCoord().getLat(),
                                        requestCache.getCount());
                            }
                            data.put(cityName, eChartMapData);
                            break;
                        case "Forecast":
                            Forecast forecast = (Forecast) requestCache.getResponse();
                            cityName = forecast.getCity().getName();
                            if (data.containsKey(cityName)) {
                                eChartMapData = data.get(cityName);
                                eChartMapData.set3Value((int) (eChartMapData.get3Value() + requestCache.getCount()));
                            } else {
                                eChartMapData = new EChartMapData(cityName,
                                        forecast.getCity().getCoord().getLon(),
                                        forecast.getCity().getCoord().getLat(),
                                        requestCache.getCount());
                            }
                            data.put(cityName, eChartMapData);
                            break;
                        default:
                    }
                });
        return Results.success(data.values().stream()
                .sorted(Comparator.comparing(EChartMapData::get3Value).reversed())
                .collect(Collectors.toList()));
    }
}
