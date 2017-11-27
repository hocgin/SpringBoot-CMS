package in.hocg.web.modules.system.controller.message;

import in.hocg.web.global.component.MailService;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.base.filter.lang.IdsFilter;
import in.hocg.web.modules.system.domain.MailTemplate;
import in.hocg.web.modules.system.filter.MailTemplateDataTablesInputFilter;
import in.hocg.web.modules.system.filter.MailTemplateFilter;
import in.hocg.web.modules.system.service.MailTemplateService;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by hocgin on 2017/11/26.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/message/mail")
public class MailTemplateController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/message/mail/%s";
    
    private MailTemplateService mailTemplateService;
    private MailService mailService;
    
    public MailTemplateController(MailTemplateService mailTemplateService,
                                  MailService mailService) {
        this.mailTemplateService = mailTemplateService;
        this.mailService = mailService;
    }
    
    @GetMapping({"/index.html", "/"})
    public String vIndex(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    
    @GetMapping("/add-view.html")
    public String vAdd(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "add-view");
    }
    
    @GetMapping("/query-modal.html")
    public String vQueryModal() {
        return String.format(BASE_TEMPLATES_PATH, "query-modal");
    }
    
    @GetMapping("/update-view/{id}")
    public String vUpdate(@PathVariable("id") String id, Model model) {
        MailTemplate mailTemplate = mailTemplateService.find(id);
        model.addAttribute("mailTemplate", mailTemplate);
        return String.format(BASE_TEMPLATES_PATH, "update-view");
    }
    
    @PostMapping("/data")
    @ResponseBody
    public DataTablesOutput<MailTemplate> data(@RequestBody MailTemplateDataTablesInputFilter filter) {
        return mailTemplateService.data(filter);
    }
    /**
     * 删除
     *
     * @param filter
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'message.mail.delete')")
    public Results delete(@Validated IdsFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        mailTemplateService.delete(checkError, filter.getId());
        return Results.check(checkError, "删除成功");
    }
    
    @RequestMapping("/insert")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'message.mail.add')")
    public Results insert(@Validated({Insert.class}) MailTemplateFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        mailTemplateService.insert(filter, checkError);
        return Results.check(checkError, "增加成功");
    }
    
    @RequestMapping("/detail/{id}")
    public String vDetail(@PathVariable("id") String id, Model model) throws IOException {
        MailTemplate mailTemplate = mailTemplateService.find(id);
        model.addAttribute("mailTemplate", mailTemplate);
        model.addAttribute("content", mailService.thymeleaf(mailTemplate.getTemplate().getPath(), mailTemplate.getParam()));
        return String.format(BASE_TEMPLATES_PATH, "detail-modal");
    }
    
    @RequestMapping("/update")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'message.mail.edit')")
    public Results update(@Validated({Update.class}) MailTemplateFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        mailTemplateService.update(filter, checkError);
        return Results.check(checkError, "修改成功");
    }
}
