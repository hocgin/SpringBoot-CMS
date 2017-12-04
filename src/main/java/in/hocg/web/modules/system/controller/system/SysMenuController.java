package in.hocg.web.modules.system.controller.system;

import in.hocg.web.modules.system.filter.MenuFilter;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.base.filter.lang.IdFilter;
import in.hocg.web.modules.base.filter.lang.IdsFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.body.LeftMenu;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.lang.iText;
import in.hocg.web.lang.utils.DocumentKit;
import in.hocg.web.modules.system.domain.SysMenu;
import in.hocg.web.modules.system.service.SysMenuService;
import in.hocg.web.modules.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
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
@RequestMapping("/admin/system/menu")
@PreAuthorize("hasPermission(null, 'sys.menu')")
public class SysMenuController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/system/menu/%s";
    
    private SysMenuService sysMenuService;
    private iText htmlUtils;
    
    @Autowired
    public SysMenuController(SysMenuService permissionService,
                             iText htmlUtils) {
        this.sysMenuService = permissionService;
        this.htmlUtils = htmlUtils;
    }
    
    @GetMapping({"/index.html", "/"})
    public String vIndex(Model model) {
        Optional<String> html = sysMenuService.queryRoot()
                .stream()
                .map(this::tr)
                .reduce((a, b) -> a + b);
        model.addAttribute("root", html.orElse(htmlUtils.trCenter(6, "暂无数据")));
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @GetMapping("/add-view.html")
    public String vAdd(@RequestParam(value = "id", required = false) String parentId, Model model) {
        if (!StringUtils.isEmpty(parentId)) {
            SysMenu permission = sysMenuService.findById(parentId);
            model.addAttribute("permission", permission);
        }
        return String.format(BASE_TEMPLATES_PATH, "add-view");
    }
    
    @GetMapping("/detail/{id}")
    public String vDetail(@PathVariable("id") String detailId, Model model) {
        model.addAttribute("o", sysMenuService.findById(detailId));
        return String.format(BASE_TEMPLATES_PATH, "detail-modal");
    }
    
    @GetMapping("/{id}")
    public String vUpdate(@PathVariable("id") String permissionId, Model model) {
        SysMenu permission = sysMenuService.findById(permissionId);
        if (!ObjectUtils.isEmpty(permission)) {
            model.addAttribute("permission", permission);
            if (!StringUtils.isEmpty(permission.getParent())) {
                model.addAttribute("parent", sysMenuService.findById(permission.getParent()));
            }
        }
        return String.format(BASE_TEMPLATES_PATH, "update-view");
    }
    
    @GetMapping("/sort-view.html")
    public String vSort(Model model) {
        List<SysMenu> sysMenus = sysMenuService.queryAllOrderByLocationAscAndPathAsc();
        List<SysMenu> root = new ArrayList<>();
        Map<String, List<SysMenu>> childMenus = new HashMap<>();
        for (SysMenu menu : sysMenus) {
            if (menu.getPath().length() > 4) {
                List<SysMenu> s = childMenus.get(DocumentKit.getParentId(menu.getPath()));
                if (CollectionUtils.isEmpty(s)) {
                    s = new ArrayList<>();
                }
                s.add(menu);
                childMenus.put(DocumentKit.getParentId(menu.getPath()), s);
            } else if (menu.getPath().length() == 4) {
                root.add(menu);
            }
        }
        LeftMenu leftMenu = new LeftMenu();
        leftMenu.setRoot(root);
        leftMenu.setChildren(childMenus);
        model.addAttribute("leftMenu", leftMenu);
        return String.format(BASE_TEMPLATES_PATH, "sort-view");
    }
    
    /**
     * 增加一个权限
     *
     * @param filter
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.menu.add')")
    public Results insert(@Validated({Insert.class}) MenuFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        sysMenuService.insert(filter, checkError);
        return Results.check(checkError, "增加成功");
    }
    
    @RequestMapping("/update-sort")
    @ResponseBody
    public Results updateSort(@Validated IdsFilter filter) {
        sysMenuService.sort(filter.getId());
        return Results.success().setMessage("保存成功");
    }
    
    /**
     * 更新
     *
     * @param filter
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.menu.edit')")
    public Results update(@Validated({Update.class}) MenuFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        sysMenuService.update(filter, checkError);
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
    @PreAuthorize("hasPermission(null, 'sys.menu.delete')")
    public Results delete(@Validated IdFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        sysMenuService.delete(filter.getId());
        return Results.success()
                .setMessage("删除成功");
    }
    
    @PostMapping("/children/{id}")
    @ResponseBody
    public Results children(@PathVariable("id") String parentId) {
        Optional<String> html = sysMenuService.queryChildren(parentId)
                .stream()
                .map(this::tr)
                .reduce((a, b) -> a + b);
        return Results.success(html.orElse(""));
    }
    
    @GetMapping("/tree")
    @ResponseBody
    public Object root() {
        List<Object> result = new ArrayList<>();
        sysMenuService.queryRoot()
                .forEach((o) -> {
                    Map<String, Object> one = new HashMap<>();
                    one.put("id", o.getId());
                    one.put("text", o.getName());
                    one.put("children", o.getHasChildren());
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
        sysMenuService.queryChildren(pid)
                .forEach((o) -> {
                    Map<String, Object> one = new HashMap<>();
                    one.put("id", o.getId());
                    one.put("text", o.getName());
                    one.put("children", o.getHasChildren());
                    one.put("permission", o.getPermission());
                    one.put("name", o.getName());
                    result.add(one);
                });
        return result;
    }
    
    @PostMapping("/available/{id}")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.menu.edit')")
    public Results available(@PathVariable("id") String id, boolean available) {
        sysMenuService.updateAvailable(id, available);
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
    public String tr(SysMenu permission) {
        String html = "<tr data-tt-id=\"%s\" data-tt-parent-id=\"%s\" data-tt-branch=\"%s\">\n" +
                "                                        <td>%s%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "     </tr>";
        return String.format(html,
                permission.getId(),
                StringUtils.isEmpty(permission.getParent()) ? "" : permission.getParent(),
                String.valueOf(permission.getHasChildren()),
                
                String.format("<span class=\"%s\"><span>", StringUtils.isEmpty(permission.getIcon()) ? "" : permission.getIcon()),
                permission.getName(),
                StringUtils.isEmpty(permission.getUrl()) ? htmlUtils.danger("暂无") : permission.getUrl(),
                StringUtils.isEmpty(permission.getType()) ? htmlUtils.danger("暂无") : SysMenu.type(permission.getType()),
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
                                "                    <li><a href=\"/admin/system/menu/%s\" pjax-data>修改</a></li>\n" +
                                "                    <li><a href=\"javascript:;;\" onclick=\"allRequest.deleteById(%s)\">删除</a></li>\n" +
                                "                    <li class=\"divider\"></li>\n" +
                                "                    <li><a href=\"/admin/system/menu/%s\" data-pjax>添加子权限</a></li>\n" +
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
