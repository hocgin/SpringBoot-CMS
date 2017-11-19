package in.hocg.web.modules.admin.controller.system;

import in.hocg.web.lang.body.response.Results;
import in.hocg.web.modules.admin.service.SysLogService;
import in.hocg.web.modules.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/system/log")
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
    
    @PostMapping("/data")
    @ResponseBody
    public DataTablesOutput data(@RequestBody DataTablesInput filter) {
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
