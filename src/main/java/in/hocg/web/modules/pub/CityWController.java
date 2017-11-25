package in.hocg.web.modules.pub;

import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.ResultCode;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.weather.domain.City;
import in.hocg.web.modules.weather.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by hocgin on 2017/11/25.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/public/city")
public class CityWController extends BaseController {
    
    private CityService cityService;
    
    @Autowired
    public CityWController(CityService cityService) {
        this.cityService = cityService;
    }
    
    @PostMapping("/search")
    @ResponseBody
    public Results search(@RequestParam("q") String q) {
        if (StringUtils.isEmpty(q)) {
            return Results.error(ResultCode.VERIFICATION_FAILED, "参数不能为空");
        }
        List<City> cities = cityService.searchForCity(q);
        return Results.success(cities);
    }
}
