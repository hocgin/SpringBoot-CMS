package in.hocg.web.modules.web.admin.system;

import in.hocg.web.filter.UserInsertFilter;
import in.hocg.web.filter.UserQueryFilter;
import in.hocg.web.filter.UserUpdateFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.body.response.Results;
import in.hocg.web.modules.domain.User;
import in.hocg.web.modules.service.UserService;
import in.hocg.web.modules.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by hocgin on 2017/11/2.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/system/user")
public class UserController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/system/user/%s";
    
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    
    @GetMapping("/index.html")
    public String vIndex(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @GetMapping("/query-modal.html")
    public String vQueryModal() {
        return String.format(BASE_TEMPLATES_PATH, "query-modal");
    }
    
    @PostMapping("/data")
    @ResponseBody
    public DataTablesOutput<User> data(@RequestBody UserQueryFilter input) {
        return userService.data(input);
    }
    
    @GetMapping("/add-view.html")
    public String vAdd(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "add-view");
    }
    
    @GetMapping("/update-view/{id}")
    public String vUpdate(@PathVariable("id") String id, Model model) {
        User user = userService.find(id);
        model.addAttribute("user", user);
        return String.format(BASE_TEMPLATES_PATH, "update-view");
    }
    
    
    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Results delete(@RequestParam("id") String[] id) {
        CheckError checkError = CheckError.get();
        userService.delete(id);
        return Results.check(checkError, "删除成功");
    }
    
    /**
     * 新建角色
     *
     * @param filter
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    public Results insert(@Valid UserInsertFilter filter) {
        CheckError checkError = CheckError.get();
        userService.insert(filter, checkError);
        return Results.check(checkError, "增加成功");
    }
    
    @PostMapping("/available/{id}")
    @ResponseBody
    public Results startById(@PathVariable("id") String id, boolean available) {
        userService.updateAvailable(id, available);
        return Results.success()
                .setMessage(String.format("%s成功", available ? "开启" : "禁用"));
    }
    
    
    @RequestMapping("/detail/{id}")
    public String vDetail(@PathVariable("id") String id, Model model) {
        User user = userService.find(id);
        
        
        model.addAttribute("user", user);
        return String.format(BASE_TEMPLATES_PATH, "detail-modal");
    }
    
    @RequestMapping("/update")
    @ResponseBody
    public Results update(@Valid UserUpdateFilter filter) {
        CheckError checkError = CheckError.get();
        userService.update(filter, checkError);
        return Results.check(checkError, "修改信息成功");
    }
    
}
