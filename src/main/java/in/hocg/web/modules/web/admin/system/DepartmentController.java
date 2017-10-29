package in.hocg.web.modules.web.admin.system;

import in.hocg.web.lang.body.response.Results;
import in.hocg.web.modules.domain.Department;
import in.hocg.web.modules.service.DepartmentService;
import in.hocg.web.modules.service.impl.DepartmentServiceImpl;
import in.hocg.web.modules.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by hocgin on 2017/10/28.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/system/department")
public class DepartmentController extends BaseController {
    
    private DepartmentService departmentService;
    
    @Autowired
    public DepartmentController(DepartmentServiceImpl departmentService) {
        this.departmentService = departmentService;
    }
    
    @RequestMapping("/index.html")
    public String vIndex(Model model) {
        Optional<String> html = departmentService.queryRoot()
                .stream()
                .map(this::tr)
                .reduce((a, b) -> a + b);
        model.addAttribute("root", html.orElse(""));
        return "/admin/system/department/index";
    }
    
    @RequestMapping("/add-view.html")
    public String vAdd(@RequestParam(value = "parent-id", required = false) String parentId, Model model) {
        if (parentId != null) {
            Department department = departmentService.findById(parentId);
            model.addAttribute("o", department);
        }
        return "/admin/system/department/add-view";
    }
    
    @RequestMapping("/detail/{detail-id}")
    public String vDetail(@PathVariable("detail-id") String detailId, Model model) {
        model.addAttribute("o", departmentService.findById(detailId));
        return "/admin/system/department/detail-modal";
    }
    
    @GetMapping("/{department-id}")
    public String vUpdate(@PathVariable("department-id") String departmentId, Model model) {
        Department department = departmentService.findById(departmentId);
        model.addAttribute("o", department);
        if (department.getParent() != null) {
            model.addAttribute("parent", departmentService.findById(department.getParent()));
        }
        return "/admin/system/department/update-view";
    }
    
    /**
     * 增加一个部门
     *
     * @param department
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    public Results insert(Department department) {
        departmentService.insert(department);
        return Results.success()
                .setMessage("增加成功");
    }
    
    /**
     * 更新
     *
     * @param department
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public Results update(Department department) {
        departmentService.update(department);
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
    public Results delete(String id) {
        departmentService.delete(id);
        return Results.success("删除成功");
    }
    
    @PostMapping("/children/{parentId}")
    @ResponseBody
    public Results children(@PathVariable("parentId") String parentId) {
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
    
    @GetMapping("/tree/{pid}")
    @ResponseBody
    public Object root(@PathVariable("pid") String pid) {
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
     * todo 后续优化 。。。
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
                department.getParent() == null ? "" : department.getParent(),
                String.valueOf(department.isHasChildren()),
                department.getName(),
                department.getDescription() == null ? "暂无描述" : department.getDescription(),
                department.getPhone() == null ? "暂无联系方式" : department.getPhone(),
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
                        String.format("add-view.html?parent-id=%s", department.getId())
                )
        );
    }
}
