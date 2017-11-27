package in.hocg.web.modules.web;

import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.filter.lang.IdFilter;
import in.hocg.web.modules.weather.domain.City;
import in.hocg.web.modules.weather.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by hocgin on 2017/11/23.
 * email: hocgin@gmail.com
 */
@Controller
public class IndexController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/web/%s";
    private CityService cityService;
    
    @Autowired
    public IndexController(CityService cityService) {
        this.cityService = cityService;
    }
    
    @RequestMapping({"/", "/index.html"})
    public String vIndex(@RequestParam(required = false) String city, Model model) {
        if (!StringUtils.isEmpty(city)) {
            City obj = cityService.findById(city);
            if (!ObjectUtils.isEmpty(obj)) {
                model.addAttribute("city", String.format("%s, %s, %s", obj.getProvince(), obj.getCity(), obj.getCounty()));
                model.addAttribute("cityId", obj.getId());
                model.addAttribute("lon", obj.getLongitude());
                model.addAttribute("lat", obj.getLatitude());
            }
        }
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @GetMapping("/login.html")
    public String login() {
        return String.format(BASE_TEMPLATES_PATH, "login");
    }
    
    @GetMapping("/login-modal.html")
    public String vLoginModal() {
        return String.format(BASE_TEMPLATES_PATH, "login-modal");
    }
    
    
    @GetMapping("/register-modal.html")
    public String vRegisterModal() {
        return String.format(BASE_TEMPLATES_PATH, "register-modal");
    }
}
