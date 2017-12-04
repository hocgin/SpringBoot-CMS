package in.hocg.web.modules.system.controller.system;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.PageKit;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.base.filter.lang.IdsFilter;
import in.hocg.web.modules.system.domain.ShortUrl;
import in.hocg.web.modules.system.filter.ShortUrlFilter;
import in.hocg.web.modules.system.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/system/short-url")
public class ShortUrlController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/system/short-url/%s";
    private ShortUrlService shortUrlService;
    
    @Autowired
    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }
    
    @GetMapping({"/index.html", "/"})
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
        ShortUrl shortUrl = shortUrlService.findOne(id);
        model.addAttribute("shortUrl", shortUrl);
        return String.format(BASE_TEMPLATES_PATH, "update-view");
    }
    
    
    @PostMapping("/data")
    @ResponseBody
    public DataTablesOutput data(@RequestBody DataTablesInput input) {
        return shortUrlService.data(input);
    }
    
    
    @PostMapping("/insert")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.short-url.add')")
    public Results insert(@Validated(Insert.class) ShortUrlFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        shortUrlService.insert(filter, checkError);
        return Results.check(checkError, "新增成功");
    }
    
    @PostMapping("/update")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.short-url.edit')")
    public Results update(@Validated(Update.class) ShortUrlFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        shortUrlService.update(filter, checkError);
        return Results.check(checkError, "修改成功");
    }
    
    @PostMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.short-url.delete')")
    public Results delete(@Validated IdsFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        shortUrlService.delete(filter.getId(), checkError);
        return Results.check(checkError, "删除成功");
    }
    
    
    
    @PostMapping("/available/{id}")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'sys.short-url.edit')")
    public Results available(@PathVariable("id") String id, boolean available) {
        shortUrlService.available(id, available);
        return Results.success()
                .setMessage(String.format("%s成功", available ? "开启" : "禁用"));
    }
    
    
    @RequestMapping("/u/{code}")
    public String u(@PathVariable("code") String code) {
        ShortUrl shortUrl = shortUrlService.findOneByCode(code);
        if (ObjectUtils.isEmpty(shortUrl)) {
            return PageKit.v404;
        }
        return String.format("redirect:%s", shortUrl.getOriginalUrl());
    }
}
