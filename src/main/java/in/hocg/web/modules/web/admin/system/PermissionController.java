package in.hocg.web.modules.web.admin.system;

import in.hocg.web.filter.PermissionFilter;
import in.hocg.web.filter.group.Insert;
import in.hocg.web.filter.group.Update;
import in.hocg.web.filter.lang.IdFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.HtmlUtils;
import in.hocg.web.lang.body.response.Results;
import in.hocg.web.modules.domain.Permission;
import in.hocg.web.modules.service.PermissionService;
import in.hocg.web.modules.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by hocgin on 2017/10/28.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/system/permission")
public class PermissionController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/system/permission/%s";
    
    private PermissionService permissionService;
    private HtmlUtils htmlUtils;
    
    @Autowired
    public PermissionController(PermissionService permissionService,
                                HtmlUtils htmlUtils) {
        this.permissionService = permissionService;
        this.htmlUtils = htmlUtils;
    }
    
    @RequestMapping("/index.html")
    public String vIndex(Model model) {
        Optional<String> html = permissionService.queryRoot()
                .stream()
                .map(this::tr)
                .reduce((a, b) -> a + b);
        model.addAttribute("root", html.orElse(htmlUtils.trCenter(6, "暂无数据")));
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @RequestMapping("/add-view.html")
    public String vAdd(@RequestParam(value = "id", required = false) String parentId, Model model) {
        if (!StringUtils.isEmpty(parentId)) {
            Permission permission = permissionService.findById(parentId);
            model.addAttribute("permission", permission);
        }
        return String.format(BASE_TEMPLATES_PATH, "add-view");
    }
    
    @RequestMapping("/detail/{id}")
    public String vDetail(@PathVariable("id") String detailId, Model model) {
        model.addAttribute("o", permissionService.findById(detailId));
        return String.format(BASE_TEMPLATES_PATH, "detail-modal");
    }
    
    @GetMapping("/{id}")
    public String vUpdate(@PathVariable("id") String permissionId, Model model) {
        Permission permission = permissionService.findById(permissionId);
        model.addAttribute("permission", permission);
        if (!StringUtils.isEmpty(permission.getParent())) {
            model.addAttribute("parent", permissionService.findById(permission.getParent()));
        }
        return String.format(BASE_TEMPLATES_PATH, "update-view");
    }
    
    /**
     * 增加一个权限
     *
     * @param filter
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public Results insert(@Validated({Insert.class}) PermissionFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        permissionService.insert(filter, checkError);
        return Results.check(checkError, "增加成功");
    }
    
    /**
     * 更新
     *
     * @param filter
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public Results update(@Validated({Update.class}) PermissionFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        permissionService.update(filter, checkError);
        return Results.check(checkError, "更新成功");
    }
    
    /**
     * 删除
     *
     * @param filter
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public Results delete(@Validated IdFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        permissionService.delete(filter.getId());
        return Results.success()
                .setMessage("删除成功");
    }
    
    @PostMapping("/children/{id}")
    @ResponseBody
    public Results children(@PathVariable("id") String parentId) {
        Optional<String> html = permissionService.queryChildren(parentId)
                .stream()
                .map(this::tr)
                .reduce((a, b) -> a + b);
        return Results.success(html.orElse(""));
    }
    
    @GetMapping("/tree")
    @ResponseBody
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
    
    @GetMapping("/tree/{id}")
    @ResponseBody
    public Object root(@PathVariable("id") String pid) {
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
    public Results available(@PathVariable("id") String id, boolean available) {
        permissionService.updateAvailable(id, available);
        return Results.success()
                .setMessage(String.format("%s成功", available ? "开启" : "禁用"));
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
                StringUtils.isEmpty(permission.getParent()) ? "" : permission.getParent(),
                String.valueOf(permission.isHasChildren()),
                
                permission.getName(),
                StringUtils.isEmpty(permission.getUrl()) ? htmlUtils.danger("暂无") : permission.getUrl(),
                StringUtils.isEmpty(permission.getType()) ? htmlUtils.danger("暂无") : Permission.type(permission.getType()),
                StringUtils.isEmpty(permission.getPermission()) ? htmlUtils.danger("暂无") : permission.getPermission(),
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
                        String.format("add-view.html?id=%s", permission.getId()),
                        permission.getId(),
                        permission.getId()
                )
        );
    }
}
