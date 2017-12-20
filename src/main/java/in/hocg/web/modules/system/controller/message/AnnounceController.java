package in.hocg.web.modules.system.controller.message;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.SecurityKit;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.base.filter.lang.IdsFilter;
import in.hocg.web.modules.system.filter.AnnounceFilter;
import in.hocg.web.modules.system.service.notify.NotifyService;
import in.hocg.web.modules.system.service.notify.UserNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hocgin on 2017/12/20.
 * email: hocgin@gmail.com
 * 公告
 */
@Controller
@RequestMapping("/admin/message/announce")
@PreAuthorize("hasPermission(null, 'message.announce')")
public class AnnounceController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/message/announce/%s";
    private NotifyService notifyService;
    private UserNotifyService userNotifyService;
    @Autowired
    public AnnounceController(NotifyService notifyService,
                              UserNotifyService userNotifyService) {
        this.notifyService = notifyService;
        this.userNotifyService = userNotifyService;
    }
    
    @GetMapping({"/", "index.html"})
    public String index() {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @GetMapping({"add-view.html"})
    public String vAdd() {
        return String.format(BASE_TEMPLATES_PATH, "add-view");
    }
    
    @PostMapping("/data")
    @ResponseBody
    public DataTablesOutput data(@RequestBody DataTablesInput filter) {
        return notifyService.data(filter);
    }
    
    
    @PostMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'message.announce.delete')")
    public Results deletes(@Validated IdsFilter filter) {
        notifyService.deletes(filter.getId());
        return Results.success("删除成功");
    }
    
    @PostMapping("/insert")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'message.announce.add')")
    public Results insert(@Validated AnnounceFilter filter) {
        CheckError checkError = CheckError.get();
        notifyService.createAnnounce(filter.getContent(), SecurityKit.iUser().getId(), checkError);
        return Results.check(checkError, "新增成功");
    }
    
}
