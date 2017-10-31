package in.hocg.web.modules.web.admin.system;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.body.response.Results;
import in.hocg.web.modules.domain.Role;
import in.hocg.web.modules.service.RoleService;
import in.hocg.web.modules.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/system/role")
public class RoleController extends BaseController {
    private RoleService roleService;
    
    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    
    @RequestMapping("/index.html")
    public String vIndex(Model model) {
        return "/admin/system/role/index";
    }
    @RequestMapping("/add-view.html")
    public String vAdd(Model model) {
        return "/admin/system/role/add-view";
    }
    
    @RequestMapping("/data")
    @ResponseBody
    public DataTablesOutput data(DataTablesInput input) {
        DataTablesOutput<Role> data = roleService.data(input);
        data.setDraw(0);
        return data;
    }
    
    
    /**
     * 增加一个角色
     *
     * @param role
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    public Results insert(Role role,
                          @RequestParam("permissionIds") String[] permissionIds) {
        CheckError checkError = CheckError.get();
        roleService.insert(role, permissionIds, checkError);
        return Results.check(checkError, "增加成功");
    }
    
    /**
     * 增加一个角色
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Results delete(@RequestParam("id") String[] id) {
        CheckError checkError = CheckError.get();
        roleService.delete(id);
        return Results.check(checkError, "删除成功");
    }
    
    @PostMapping("/available/{id}")
    @ResponseBody
    public Results startById(@PathVariable("id") String id, boolean available) {
        roleService.updateAvailable(id, available);
        return Results.success()
                .setMessage(String.format("%s成功", available? "开启": "禁用"));
    }
    
    
    @RequestMapping("/detail/{detail-id}")
    public String vDetail(@PathVariable("detail-id") String detailId, Model model) {
//        model.addAttribute("o", roleService.find(detailId));
        return "/admin/system/role/detail-modal";
    }
}
