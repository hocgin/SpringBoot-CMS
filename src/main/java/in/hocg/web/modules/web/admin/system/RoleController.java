package in.hocg.web.modules.web.admin.system;

import in.hocg.web.filter.RoleQueryFilter;
import in.hocg.web.filter.RoleUpdateInfoFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.body.response.Results;
import in.hocg.web.modules.domain.Role;
import in.hocg.web.modules.service.RoleService;
import in.hocg.web.modules.service.UserService;
import in.hocg.web.modules.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.ArrayUtils;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/system/role")
public class RoleController extends BaseController {
    private UserService userService;
    private RoleService roleService;
    public final String BASE_TEMPLATES_PATH = "/admin/system/role/%s";
    
    @Autowired
    public RoleController(RoleService roleService,
                          UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }
    
    @GetMapping("/index.html")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String vIndex(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @GetMapping("/add-view.html")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String vAdd(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "add-view");
    }
    
    @GetMapping("/query-modal.html")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String vQuery() {
        return String.format(BASE_TEMPLATES_PATH, "query-modal");
    }
    
    @GetMapping("/update-info-view/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String vUpdateInfo(@PathVariable("id") String id, Model model) {
        model.addAttribute("role", roleService.find(id));
        return String.format(BASE_TEMPLATES_PATH, "update-info-view");
    }
    
    @RequestMapping("/detail/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String vDetail(@PathVariable("id") String id, Model model) {
        Role role = roleService.find(id);
        
        
        model.addAttribute("role", role);
        return "/admin/system/role/detail-modal";
    }
    
    @GetMapping("/update-permission-view/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String vUpdatePermission(@PathVariable("id") String id, Model model) {
        model.addAttribute("role", roleService.find(id));
        return String.format(BASE_TEMPLATES_PATH, "update-permission-view");
    }
    
    @GetMapping("/select-user-view/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String vSelectUser(@PathVariable("id") String id, Model model) {
        Role role = roleService.find(id);
        model.addAttribute("role", role);
        return String.format(BASE_TEMPLATES_PATH, "select-user-view");
    }
    
    
    @RequestMapping("/data")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public DataTablesOutput<Role> data(@RequestBody RoleQueryFilter input) {
        return roleService.data(input);
    }
    
    /**
     * 增加一个角色
     *
     * @param role
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Results insert(Role role,
                          @RequestParam("permissionIds") String[] permissionIds) {
        CheckError checkError = CheckError.get();
        roleService.insert(role, permissionIds, checkError);
        return Results.check(checkError, "增加成功");
    }
    
    @RequestMapping("/update")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Results update(RoleUpdateInfoFilter updateInfoFilter) {
        CheckError checkError = CheckError.get();
        roleService.save(updateInfoFilter, checkError);
        return Results.check(checkError, "修改信息成功");
    }
    
    @PostMapping("/update-permission")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Results updatePermission(@RequestParam("id") String id,
                                    @RequestParam("permissionId[]") String[] permissionIds) {
        CheckError checkError = CheckError.get();
        roleService.save(id, permissionIds, checkError);
        return Results.check(checkError, "分配权限成功");
    }
    
    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Results delete(@RequestParam("id") String[] id) {
        CheckError checkError = CheckError.get();
        roleService.delete(id);
        return Results.check(checkError, "删除成功");
    }
    
    @PostMapping("/available/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Results startById(@PathVariable("id") String id, boolean available) {
        roleService.updateAvailable(id, available);
        return Results.success()
                .setMessage(String.format("%s成功", available ? "开启" : "禁用"));
    }
    
    
    @PostMapping("/add-user")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Results addUser(@RequestParam("role") String roleId,
                           @RequestParam("user[]") String[] userIds) {
        if (StringUtils.isEmpty(roleId)
                || ArrayUtils.isEmpty(userIds)) {
            return Results.error(CheckError.CODE, "数据异常");
        }
        userService.addRoleToUser(roleId, userIds);
        return Results.success().setMessage("分配用户成功");
    }
    
    
    @PostMapping("/remove-user")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Results removeUser(@RequestParam("role") String roleId,
                           @RequestParam("user") String[] userIds) {
        if (StringUtils.isEmpty(roleId)
                || ArrayUtils.isEmpty(userIds)) {
            return Results.error(CheckError.CODE, "数据异常");
        }
        userService.removeRoleFormUser(roleId, userIds);
        return Results.success().setMessage("分配用户成功");
    }
}
