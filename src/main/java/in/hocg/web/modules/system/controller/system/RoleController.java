package in.hocg.web.modules.system.controller.system;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.tree.TreeKit;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.ResultCode;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update1;
import in.hocg.web.modules.base.filter.group.Update2;
import in.hocg.web.modules.base.filter.lang.IdFilter;
import in.hocg.web.modules.base.filter.lang.IdsFilter;
import in.hocg.web.modules.system.body.JsTreeNode;
import in.hocg.web.modules.system.domain.Role;
import in.hocg.web.modules.system.domain.SysMenu;
import in.hocg.web.modules.system.filter.RoleDataTablesInputFilter;
import in.hocg.web.modules.system.filter.RoleFilter;
import in.hocg.web.modules.system.filter.UserToRoleFilter;
import in.hocg.web.modules.system.service.RoleService;
import in.hocg.web.modules.system.service.SysMenuService;
import in.hocg.web.modules.system.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/system/role")
@PreAuthorize("hasPermission(null, 'sys.role')")
public class RoleController extends BaseController {
    private ManagerService userService;
    private RoleService roleService;
    private SysMenuService sysMenuService;
    public final String BASE_TEMPLATES_PATH = "/admin/system/role/%s";
    
    @Autowired
    public RoleController(RoleService roleService,
                          SysMenuService sysMenuService,
                          ManagerService userService) {
        this.roleService = roleService;
        this.userService = userService;
        this.sysMenuService = sysMenuService;
    }
    
    @GetMapping({"/index.html", "/"})
    public String vIndex(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @GetMapping("/add-view.html")
    public String vAdd(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "add-view");
    }
    
    @GetMapping("/query-modal.html")
    public String vQuery() {
        return String.format(BASE_TEMPLATES_PATH, "query-modal");
    }
    
    @GetMapping("/update-info-view/{id}")
    public String vUpdateInfo(@PathVariable("id") String id, Model model) {
        model.addAttribute("role", roleService.find(id));
        return String.format(BASE_TEMPLATES_PATH, "update-info-view");
    }
    
    @GetMapping("/detail/{id}")
    public String vDetail(@PathVariable("id") String id, Model model) {
        Role role = roleService.find(id);
        model.addAttribute("role", role);
        return "/admin/system/role/detail-modal";
    }
    
    @GetMapping("/update-menu-view/{id}")
    public String vUpdatePermission(@PathVariable("id") String id, Model model) {
        model.addAttribute("role", roleService.find(id));
        return String.format(BASE_TEMPLATES_PATH, "update-menu-view");
    }
    
    @GetMapping("/select-user-view/{id}")
    public String vSelectUser(@PathVariable("id") String id, Model model) {
        Role role = roleService.find(id);
        model.addAttribute("role", role);
        return String.format(BASE_TEMPLATES_PATH, "select-user-view");
    }
    
    
    @RequestMapping("/data")
    @ResponseBody
    public DataTablesOutput<Role> data(@RequestBody RoleDataTablesInputFilter input) {
        return roleService.data(input);
    }
    
    @RequestMapping("/list")
    @ResponseBody
    public Results roles(String department) {
        List<Role> roles = roleService.findByDepartmentAndChildren(department);
        return Results.success(roles);
    }
    
    /**
     * 增加一个角色
     *
     * @param filter
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.role.add')")
    public Results insert(@Validated({Insert.class}) RoleFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        roleService.insert(filter, checkError);
        return Results.check(checkError, "新建角色成功");
    }
    
    @RequestMapping("/update")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.role.edit')")
    public Results update(@Validated({Update1.class}) RoleFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        roleService.updateDescription(filter, checkError);
        return Results.check(checkError, "修改信息成功");
    }
    
    @PostMapping("/update-menu")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.role.edit')")
    public Results updatePermission(@Validated({Update2.class}) RoleFilter filter,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        roleService.updatePermission(filter, checkError);
        return Results.check(checkError, "更新权限成功");
    }
    
    @RequestMapping("/menus-tree")
    @ResponseBody
    public Object getMenusTree(@Validated IdFilter filter,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        Role role = roleService.find(filter.getId());
        if (ObjectUtils.isEmpty(role)) {
            return Results.error(ResultCode.PRECONDITION_FAILED, "未找到角色");
        }
        Collection<String> permissions = Optional.ofNullable(role.getPermissions()).orElse(Collections.emptyList())
                .stream()
                .map(SysMenu::getPermission)
                .collect(Collectors.toList());
        
        // 获取所有权限
        List<JsTreeNode> allNodes = sysMenuService.queryAllOrderByLocationAscAndPathAsc().stream()
                .map(node -> {
                    JsTreeNode treeNode = new JsTreeNode();
                    treeNode.setHasChildren(node.getHasChildren());
                    treeNode.setId(node.getId());
                    treeNode.setParent(node.getParent());
                    JsTreeNode.StateBean state = new JsTreeNode.StateBean();
                    state.setOpened(permissions.contains(node.getPermission()));
                    state.setSelected(permissions.contains(node.getPermission()));
                    treeNode.setState(state);
                    treeNode.setText(node.getName());
                    return treeNode;
                }).collect(Collectors.toList());
        
        // 最后的结果
        List<JsTreeNode> rootNodes = new ArrayList<>();
        for (JsTreeNode node : allNodes) {
            if (StringUtils.isEmpty(node.getParent())) { // 根结点
                rootNodes.add(node);
            }
        }
        // 查找子节点
        for (JsTreeNode node : rootNodes) {
            node.setChildren(TreeKit.getChildX(node.getId(), allNodes));
        }
        return rootNodes;
    }
    
    /**
     * 删除
     *
     * @param filter
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.role.delete')")
    public Results delete(@Validated IdsFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        roleService.delete(checkError, filter.getId());
        return Results.check(checkError, "删除成功");
    }
    
    @PostMapping("/available/{id}")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.role.edit')")
    public Results available(@PathVariable("id") String id, boolean available) {
        roleService.updateAvailable(id, available);
        return Results.success()
                .setMessage(String.format("%s成功", available ? "开启" : "禁用"));
    }
    
    
    @PostMapping("/add-user")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.role.edit')")
    public Results addUser(@Validated UserToRoleFilter filter,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        userService.addRoleToUser(filter.getRole(), filter.getUsers());
        return Results.success()
                .setMessage("分配用户成功");
    }
    
    
    @PostMapping("/remove-user")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.role.edit')")
    public Results removeUser(@Validated UserToRoleFilter filter,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        userService.removeRoleFormUser(filter.getRole(), filter.getUsers());
        return Results.success()
                .setMessage("分配用户成功");
    }
}
