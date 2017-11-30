package in.hocg.web.modules.system.controller.system;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.base.filter.lang.IdsFilter;
import in.hocg.web.modules.system.domain.SysTask;
import in.hocg.web.modules.system.filter.SysTaskFilter;
import in.hocg.web.modules.system.service.SysTaskService;
import org.quartz.SchedulerException;
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
 * Created by hocgin on 2017/11/30.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/system/task")
@PreAuthorize("hasPermission(null, 'sys.task')")
public class SysTaskController extends BaseController {
    private String BASE_TEMPLATES_PATH = "/admin/system/task/%s";
    private SysTaskService sysTaskService;
    
    @Autowired
    public SysTaskController(SysTaskService sysTaskService) {
        this.sysTaskService = sysTaskService;
    }
    
    @GetMapping({"/index.html", "/"})
    public String vIndex(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    
    @GetMapping("/add-view.html")
    public String vAdd() {
        return String.format(BASE_TEMPLATES_PATH, "add-view");
    }
    
    
    @GetMapping("/detail/{id}")
    public String vDetail(@PathVariable("id") String detailId, Model model) {
        model.addAttribute("task", sysTaskService.findOne(detailId));
        return String.format(BASE_TEMPLATES_PATH, "detail-modal");
    }
    
    @GetMapping("/update-view/{id}")
    public String vUpdate(@PathVariable("id") String id, Model model) {
        SysTask task = sysTaskService.findOne(id);
        model.addAttribute("task", task);
        return String.format(BASE_TEMPLATES_PATH, "update-view");
    }
    
    @PostMapping("/data")
    @ResponseBody
    public DataTablesOutput data(@RequestBody DataTablesInput filter) {
        return sysTaskService.data(filter);
    }
    
    
    /**
     * 增加
     *
     * @param filter
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.task.add')")
    public Results insert(@Validated({Insert.class}) SysTaskFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        sysTaskService.insert(filter, checkError);
        return Results.check(checkError, "增加成功");
    }
    
    @PostMapping("/update")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.task.edit')")
    public Results update(@Validated({Update.class})  SysTaskFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        sysTaskService.update(filter, checkError);
        return Results.check(checkError, "更新成功");
    }
    
    @PostMapping("/available/{id}")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.task.edit')")
    public Results available(@PathVariable("id") String id, boolean available) {
        CheckError checkError = CheckError.get();
        sysTaskService.available(id, available, checkError);
        return Results.check(checkError, String.format("%s成功", available ? "开启" : "禁用"));
    }
    
    @PostMapping("/resume/{id}")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.task.edit')")
    public Results resume(@PathVariable("id") String id) throws SchedulerException {
        CheckError checkError = CheckError.get();
        sysTaskService.resume(id, checkError);
        return Results.check(checkError, "重置成功");
    }
    
    @PostMapping("/restart/{id}")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.task.edit')")
    public Results restart(@PathVariable("id") String id) {
        CheckError checkError = CheckError.get();
        sysTaskService.restart(id, checkError);
        return Results.check(checkError, "重启成功");
    }
    
    @PostMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.task.delete')")
    public Results delete(@Validated IdsFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        sysTaskService.delete(filter.getId());
        return Results.check(checkError, "删除成功");
    }
    
}
