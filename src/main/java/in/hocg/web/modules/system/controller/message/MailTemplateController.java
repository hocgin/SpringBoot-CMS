package in.hocg.web.modules.system.controller.message;

import in.hocg.web.global.component.MailService;
import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.iText;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.base.filter.lang.IdFilter;
import in.hocg.web.modules.base.filter.lang.IdsFilter;
import in.hocg.web.modules.system.domain.MailTemplate;
import in.hocg.web.modules.system.filter.MailTemplateDataTablesInputFilter;
import in.hocg.web.modules.system.filter.MailTemplateFilter;
import in.hocg.web.modules.system.filter.SendGroupMailFilter;
import in.hocg.web.modules.system.filter.SendMailFilter;
import in.hocg.web.modules.system.service.MailTemplateService;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
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
@PreAuthorize("hasPermission(null, 'message.mail')")
public class MailTemplateController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/message/mail/%s";
    
    private MailTemplateService mailTemplateService;
    private MailService mailService;
    private iText iText;
    
    public MailTemplateController(MailTemplateService mailTemplateService,
                                  MailService mailService,
                                  iText iText) {
        this.mailTemplateService = mailTemplateService;
        this.mailService = mailService;
        this.iText = iText;
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
    
    @GetMapping("/send-group-view/{id}")
    public String vSendGroup(@PathVariable("id") String id, Model model) {
        MailTemplate template = mailTemplateService.find(id);
        model.addAttribute("template", template);
        return String.format(BASE_TEMPLATES_PATH, "send-group-view");
    }
    
    @GetMapping("/send-user-view/{id}")
    public String vSendUser(@PathVariable("id") String id, Model model) {
        MailTemplate template = mailTemplateService.find(id);
        model.addAttribute("template", template);
        return String.format(BASE_TEMPLATES_PATH, "send-user-view");
    }
    
    @GetMapping("/send-member-view/{id}")
    public String vSendMember(@PathVariable("id") String id, Model model) {
        MailTemplate template = mailTemplateService.find(id);
        model.addAttribute("template", template);
        return String.format(BASE_TEMPLATES_PATH, "send-member-view");
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
    
    @GetMapping("/detail/{id}")
    public String vDetail(@PathVariable("id") String id, Model model) throws IOException {
        MailTemplate mailTemplate = mailTemplateService.find(id);
        model.addAttribute("mailTemplate", mailTemplate);
        return String.format(BASE_TEMPLATES_PATH, "detail-modal");
    }
    
    @GetMapping("/browser/{id}")
    public String vBrowser(@PathVariable("id") String id, Model model) throws IOException {
        MailTemplate mailTemplate = mailTemplateService.find(id);
        String content;
        if (ObjectUtils.isEmpty(mailTemplate)
                || !mailTemplate.getTemplate().exists()) {
            content = iText.danger("模版文件异常");
        } else {
            content = mailService.thymeleaf(mailTemplate.getTemplate().getPath(), mailTemplate.getParam());
        }
        model.addAttribute("content", content);
        model.addAttribute("mailTemplate", mailTemplate);
        return String.format(BASE_TEMPLATES_PATH, "browser-modal");
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
    
    @PostMapping("/send-group")
    @ResponseBody
    public Results sendGroup(@Validated IdFilter idFilter,
                             @Validated SendGroupMailFilter filter,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        mailTemplateService.sendGroup(idFilter.getId(), filter, checkError);
        return Results.check(checkError, "发送成功");
    }
    
    @PostMapping("/send")
    @ResponseBody
    public Results send(@Validated IdFilter idFilter,
                             @Validated SendMailFilter filter,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        mailTemplateService.send(idFilter.getId(), filter, checkError);
        return Results.check(checkError, "发送成功");
    }
    
}
