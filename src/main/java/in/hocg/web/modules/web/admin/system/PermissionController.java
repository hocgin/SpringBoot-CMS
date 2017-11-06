package in.hocg.web.modules.web.admin.system;

import in.hocg.web.lang.body.response.Results;
import in.hocg.web.modules.domain.Permission;
import in.hocg.web.modules.service.PermissionService;
import in.hocg.web.modules.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by hocgin on 2017/10/28.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/system/permission")
public class PermissionController extends BaseController {
    
    private PermissionService permissionService;
    
    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }
    
    @RequestMapping("/index.html")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String vIndex(Model model) {
        Optional<String> html = permissionService.queryRoot()
                .stream()
                .map(this::tr)
                .reduce((a, b) -> a + b);
        model.addAttribute("root", html.orElse(""));
        return "/admin/system/permission/index";
    }
    
    @RequestMapping("/add-view.html")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String vAdd(@RequestParam(value = "parent-id", required = false) String parentId, Model model) {
        if (!StringUtils.isEmpty(parentId)) {
            Permission permission = permissionService.findById(parentId);
            model.addAttribute("o", permission);
        }
        return "/admin/system/permission/add-view";
    }
    
    @RequestMapping("/detail/{detail-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String vDetail(@PathVariable("detail-id") String detailId, Model model) {
        model.addAttribute("o", permissionService.findById(detailId));
        return "/admin/system/permission/detail-modal";
    }
    
    @GetMapping("/{permission-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String vUpdate(@PathVariable("permission-id") String permissionId, Model model) {
        Permission permission = permissionService.findById(permissionId);
        model.addAttribute("o", permission);
        if (!StringUtils.isEmpty(permission.getParent())) {
            model.addAttribute("parent", permissionService.findById(permission.getParent()));
        }
        return "/admin/system/permission/update-view";
    }
    
    /**
     * 增加一个权限
     *
     * @param permission
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Results insert(Permission permission) {
        permissionService.insert(permission);
        return Results.success()
                .setMessage("增加成功");
    }
    
    /**
     * 更新
     *
     * @param permission
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Results update(Permission permission) {
        permissionService.update(permission);
        return Results.success()
                .setMessage("更新成功");
    }
    
    /**
     * 删除
     *
     * @param id 数组
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Results delete(String id) {
        permissionService.delete(id);
        return Results.success("删除成功");
    }
    
    @PostMapping("/children/{parentId}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Results children(@PathVariable("parentId") String parentId) {
        Optional<String> html = permissionService.queryChildren(parentId)
                .stream()
                .map(this::tr)
                .reduce((a, b) -> a + b);
        return Results.success(html.orElse(""));
    }
    
    @GetMapping("/tree")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object root() {
        List<Object> result = new ArrayList<>();
        permissionService.queryRoot()
                .forEach((o) -> {
                    Map<String, Object> one = new HashMap<>();
                    one.put("id", o.getId());
                    one.put("text", o.getName());
                    one.put("children", o.isHasChildren());
                    one.put("permission", o.getPermission());
                    one.put("name", o.getName());
                    result.add(one);
                });
        return result;
    }
    
    @GetMapping("/tree/{pid}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object root(@PathVariable("pid") String pid) {
        List<Object> result = new ArrayList<>();
        permissionService.queryChildren(pid)
                .forEach((o) -> {
                    Map<String, Object> one = new HashMap<>();
                    one.put("id", o.getId());
                    one.put("text", o.getName());
                    one.put("children", o.isHasChildren());
                    one.put("permission", o.getPermission());
                    one.put("name", o.getName());
                    result.add(one);
                });
        return result;
    }
    
    @PostMapping("/available/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Results stopById(@PathVariable("id") String id, boolean available) {
        permissionService.updateAvailable(id, available);
        return Results.success()
                .setMessage(String.format("%s成功", available? "开启": "禁用"));
    }
    
    /**
     * 渲染树形表格
     * todo 后续优化 。。。
     *
     * @param permission
     * @return
     */
    public String tr(Permission permission) {
        String html = "<tr data-tt-id=\"%s\" data-tt-parent-id=\"%s\" data-tt-branch=\"%s\">\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "     </tr>";
        return String.format(html,
                permission.getId(),
                permission.getParent() == null ? "" : permission.getParent(),
                String.valueOf(permission.isHasChildren()),
                
                permission.getName(),
                permission.getUrl() == null ? "无路径" : permission.getUrl(),
                permission.getType() == null ? "暂无类型" : permission.getType(),
                permission.getPermission() == null ? "暂无权限标示" : permission.getPermission(),
                String.format("<i id=\"js-%s\" class=\"fa fa-circle %s\"></i>",
                        permission.getId(),
                        permission.getAvailable() ? "text-success" : "text-danger"),
                String.format("<div class=\"btn-group\">\n" +
                                "                  <button type=\"button\" class=\"btn btn-default btn-flat\">操作</button>\n" +
                                "                  <button type=\"button\" class=\"btn btn-default btn-flat dropdown-toggle\" data-toggle=\"dropdown\">\n" +
                                "                    <span class=\"caret\"></span>\n" +
                                "                    <span class=\"sr-only\">Toggle Dropdown</span>\n" +
                                "                  </button>\n" +
                        
                                "                  <ul class=\"dropdown-menu\" role=\"menu\">\n" +
                                "                    <li><a href=\"/admin/system/permission/%s\" pjax-data>修改</a></li>\n" +
                                "                    <li><a href=\"javascript:;;\" onclick=\"allRequest.deleteById(%s)\">删除</a></li>\n" +
                                "                    <li class=\"divider\"></li>\n" +
                                "                    <li><a href=\"/admin/system/permission/%s\" data-pjax>添加子权限</a></li>\n" +
                                "                    <li class=\"divider\"></li>\n" +
                                "                    <li><a href=\"javascript:;;\" onclick=\"allRequest.available('%s', 1)\">启用</a></li>\n" +
                                "                    <li><a href=\"javascript:;;\" onclick=\"allRequest.available('%s', 0)\">禁用</a></li>\n" +
                                "                  </ul>\n" +
                                "                </div>",
                        permission.getId(),
                        String.format("['%s']", permission.getId()),
                        String.format("add-view.html?parent-id=%s", permission.getId()),
                        permission.getId(),
                        permission.getId()
                )
        );
    }
}
