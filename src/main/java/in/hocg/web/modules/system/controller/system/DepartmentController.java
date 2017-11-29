package in.hocg.web.modules.system.controller.system;

import in.hocg.web.modules.system.filter.DepartmentFilter;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.base.filter.lang.IdFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.iText;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.system.domain.Department;
import in.hocg.web.modules.system.service.DepartmentService;
import in.hocg.web.modules.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/admin/system/department")
@PreAuthorize("hasPermission(null, 'sys.department')")
public class DepartmentController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/system/department/%s";
    
    private DepartmentService departmentService;
    private iText htmlUtils;
    
    @Autowired
    public DepartmentController(DepartmentService departmentService,
                                iText htmlUtils) {
        this.departmentService = departmentService;
        this.htmlUtils = htmlUtils;
    }
    
    @GetMapping({"/index.html", "/"})
    public String vIndex(Model model) {
        Optional<String> html = departmentService.queryRoot()
                .stream()
                .map(this::tr)
                .reduce((a, b) -> a + b);
        
        model.addAttribute("root", html.orElse(htmlUtils.trCenter(4, "暂无数据")));
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @GetMapping("/add-view.html")
    public String vAdd(@RequestParam(value = "id", required = false) String parentId, Model model) {
        if (!StringUtils.isEmpty(parentId)) {
            Department department = departmentService.findById(parentId);
            model.addAttribute("department", department);
        }
        return String.format(BASE_TEMPLATES_PATH, "add-view");
    }
    
    @GetMapping("/detail/{id}")
    public String vDetail(@PathVariable("id") String departmentId, Model model) {
        model.addAttribute("department", departmentService.findById(departmentId));
        return String.format(BASE_TEMPLATES_PATH, "detail-modal");
    }
    
    @GetMapping("/{id}")
    public String vUpdate(@PathVariable("id") String departmentId, Model model) {
        Department department = departmentService.findById(departmentId);
        model.addAttribute("department", department);
        if (!StringUtils.isEmpty(department.getParent())) {
            model.addAttribute("parent", departmentService.findById(department.getParent()));
        }
        return String.format(BASE_TEMPLATES_PATH, "update-view");
    }
    
    /**
     * 增加一个单位/部门
     *
     * @param filter
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.department.add')")
    public Results insert(@Validated(value = Insert.class) DepartmentFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        departmentService.insert(filter, checkError);
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
    @PreAuthorize("hasPermission(null, 'sys.department.edit')")
    public Results update(@Validated(value = Update.class) DepartmentFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        departmentService.update(filter, checkError);
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
    @PreAuthorize("hasPermission(null, 'sys.department.delete')")
    public Results delete(@Validated IdFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        departmentService.delete(filter.getId(), checkError);
        return Results.check(checkError, "删除成功");
    }
    
    @PostMapping("/children/{id}")
    @ResponseBody
    public Results children(@PathVariable("id") String parentId) {
        Optional<String> html = departmentService.queryChildren(parentId)
                .stream()
                .map(this::tr)
                .reduce((a, b) -> a + b);
        return Results.success(html.orElse(""));
    }
    
    @GetMapping("/tree")
    @ResponseBody
    public Object root() {
        List<Object> result = new ArrayList<>();
        departmentService.queryRoot()
                .forEach((o) -> {
                    Map<String, Object> one = new HashMap<>();
                    one.put("id", o.getId());
                    one.put("text", o.getName());
                    one.put("children", o.isHasChildren());
                    result.add(one);
                });
        return result;
    }
    
    @GetMapping("/tree/{id}")
    @ResponseBody
    public Object root(@PathVariable("id") String pid) {
        List<Object> result = new ArrayList<>();
        departmentService.queryChildren(pid)
                .forEach((o) -> {
                    Map<String, Object> one = new HashMap<>();
                    one.put("id", o.getId());
                    one.put("text", o.getName());
                    one.put("children", o.isHasChildren());
                    result.add(one);
                });
        return result;
    }
    
    /**
     * 渲染树形表格
     * todo 后续优化(丑陋, 但是因为不想前端也写一遍..)
     *
     * @param department
     * @return
     */
    public String tr(Department department) {
        String html = "<tr data-tt-id=\"%s\" data-tt-parent-id=\"%s\" data-tt-branch=\"%s\">\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "                                        <td>%s</td>\n" +
                "     </tr>";
        return String.format(html,
                department.getId(),
                ObjectUtils.isEmpty(department.getParent()) ? "" : department.getParent(),
                String.valueOf(department.isHasChildren()),
                department.getName(),
                ObjectUtils.isEmpty(department.getDescription()) ? htmlUtils.danger("暂无") : department.getDescription(),
                ObjectUtils.isEmpty(department.getPhone()) ? htmlUtils.danger("暂无") : department.getPhone(),
                String.format("<div class=\"btn-group\">\n" +
                                "                  <button type=\"button\" class=\"btn btn-default btn-flat\">操作</button>\n" +
                                "                  <button type=\"button\" class=\"btn btn-default btn-flat dropdown-toggle\" data-toggle=\"dropdown\">\n" +
                                "                    <span class=\"caret\"></span>\n" +
                                "                    <span class=\"sr-only\">Toggle Dropdown</span>\n" +
                                "                  </button>\n" +
                                "                  <ul class=\"dropdown-menu\" role=\"menu\">\n" +
                                "                    <li><a class=\"js-modal\" href=\"/admin/system/department/detail/%s\" data-target=\"#js-detail-modal\" data-toggle=\"modal\">查看</a></li>\n" +
                                "                    <li><a href=\"/admin/system/department/%s\" pjax-data>修改</a></li>\n" +
                                "                    <li><a href=\"javascript:;;\" onclick=\"allRequest.deleteSystemVars(%s)\">删除</a></li>\n" +
                                "                    <li class=\"divider\"></li>\n" +
                                "                    <li><a href=\"/admin/system/department/%s\" data-pjax>添加子单位</a></li>\n" +
                                "                  </ul>\n" +
                                "                </div>",
                        department.getId(),
                        department.getId(),
                        String.format("['%s']", department.getId()),
                        String.format("add-view.html?id=%s", department.getId())
                )
        );
    }
}
