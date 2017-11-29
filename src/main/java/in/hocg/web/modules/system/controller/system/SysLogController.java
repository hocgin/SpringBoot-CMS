package in.hocg.web.modules.system.controller.system;

import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.system.domain.SysLog;
import in.hocg.web.modules.system.filter.SysLogDataTablesInputFilter;
import in.hocg.web.modules.system.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/system/log")
@PreAuthorize("hasPermission(null, 'safety.log')")
public class SysLogController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/system/log/%s";
    
    private SysLogService sysLogService;
    @Autowired
    public SysLogController(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }
    
    @GetMapping({"/index.html", "/"})
    String vIndex() {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @GetMapping("/query-modal.html")
    public String vQueryModal(Model model) {
        model.addAttribute("froms", SysLog.From.values());
        model.addAttribute("tags", sysLogService.getTags());
        return String.format(BASE_TEMPLATES_PATH, "query-modal");
    }
    
    @PostMapping("/data")
    @ResponseBody
    public DataTablesOutput data(@RequestBody SysLogDataTablesInputFilter filter) {
        return sysLogService.data(filter);
    }
    
    @PostMapping("/empty")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'safety.log.empty')")
    public Results empty() {
        sysLogService.empty();
        return Results.success().setMessage("清空成功");
    }
    
}
