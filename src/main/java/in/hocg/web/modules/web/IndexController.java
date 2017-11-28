package in.hocg.web.modules.web;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.system.filter.MemberFilter;
import in.hocg.web.modules.system.service.MemberService;
import in.hocg.web.modules.weather.domain.City;
import in.hocg.web.modules.weather.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hocgin on 2017/11/23.
 * email: hocgin@gmail.com
 */
@Controller
public class IndexController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/web/%s";
    private CityService cityService;
    private MemberService memberService;
    
    @Autowired
    public IndexController(CityService cityService,
                           MemberService memberService) {
        this.cityService = cityService;
        this.memberService = memberService;
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
    
    @PostMapping("/register")
    @ResponseBody
    public Results register(@Validated MemberFilter filter,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        memberService.insert(filter, checkError);
        return Results.check(checkError)
                .setMessage("注册成功, 请检查邮箱");
    }
}
