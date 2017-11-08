package in.hocg.web.modules.web.admin.system;

import in.hocg.web.filter.VariableFilter;
import in.hocg.web.filter.lang.IdsFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.body.response.Results;
import in.hocg.web.modules.domain.Variable;
import in.hocg.web.modules.service.VariableService;
import in.hocg.web.modules.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/system/variable")
public class VariableController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/system/variable/%s";
    private VariableService variableService;
    
    @Autowired
    public VariableController(VariableService variableService) {
        this.variableService = variableService;
    }
    
    @GetMapping("/index.html")
    public String vIndex(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @GetMapping("/add-view.html")
    public String vAdd() {
        return String.format(BASE_TEMPLATES_PATH, "add-view");
    }
    
    @GetMapping("/query-modal.html")
    public String vQueryModal() {
        return String.format(BASE_TEMPLATES_PATH, "query-modal");
    }
    
    @GetMapping("/update-view/{id}")
    public String vUpdate(@PathVariable("id") String id, Model model) {
        Variable variable = variableService.findById(id);
        model.addAttribute("variable", variable);
        return String.format(BASE_TEMPLATES_PATH, "update-view");
    }
    
    @PostMapping("/data")
    @ResponseBody
    public DataTablesOutput data(@RequestBody DataTablesInput input) {
        return variableService.data(input);
    }
    
    @PostMapping("/insert")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public Results insert(@Validated VariableFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        variableService.insert(filter, checkError);
        return Results.check(checkError, "新增成功");
    }
    
    @PostMapping("/update")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public Results update(@Validated VariableFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        variableService.update(filter, checkError);
        return Results.check(checkError, "更新成功");
    }
    
    @PostMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public Results delete(@Validated IdsFilter filter) {
        variableService.delete(filter.getId());
        return Results.success().setMessage("删除成功");
    }
}
