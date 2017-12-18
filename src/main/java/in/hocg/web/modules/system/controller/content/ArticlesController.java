package in.hocg.web.modules.system.controller.content;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update;
import in.hocg.web.modules.base.filter.lang.IdsFilter;
import in.hocg.web.modules.system.domain.Articles;
import in.hocg.web.modules.system.filter.ArticlesFilter;
import in.hocg.web.modules.system.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/content/articles")
@PreAuthorize("hasPermission(null, 'content.articles')")
public class ArticlesController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/content/articles/%s";
    
    private ArticlesService articlesService;
    
    @Autowired
    public ArticlesController(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }
    
    @GetMapping({"/index.html", "/"})
    public String vIndex(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    
    @GetMapping("/add-view.html")
    public String vAdd(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "add-view");
    }
    
    @GetMapping("/select-channel-modal.html")
    public String vSelectChannelModal(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "select-channel-modal");
    }
    
    @GetMapping("/update-view/{id}")
    public String vUpdate(@PathVariable("id") String id, Model model) {
        
        Articles articles = articlesService.findOne(id);
        model.addAttribute("articles", articles);
        return String.format(BASE_TEMPLATES_PATH, "update-view");
    }
    
    @GetMapping("/detail/{id}")
    public String vDetail(@PathVariable("id") String id, Model model) {
        model.addAttribute("articles", articlesService.findOne(id));
        return String.format(BASE_TEMPLATES_PATH, "detail-modal");
    }
    
    @GetMapping("/browser/{id}")
    public String vBrowser(@PathVariable("id") String id, Model model) throws IOException {
        model.addAttribute("articles", articlesService.findOne(id));
        return String.format(BASE_TEMPLATES_PATH, "browser-view");
    }
    
    @PostMapping("/data")
    @ResponseBody
    public DataTablesOutput data(@RequestBody DataTablesInput input) {
        return articlesService.data(input);
    }
    
    
    /**
     * 增加
     *
     * @param filter
     * @return
     */
    @RequestMapping("/insert")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'content.articles.add')")
    public Results insert(@Validated({Insert.class}) ArticlesFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        articlesService.insert(filter, checkError);
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
    @PreAuthorize("hasPermission(null, 'content.articles.edit')")
    public Results update(@Validated({Update.class}) ArticlesFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        articlesService.update(filter, checkError);
        return Results.check(checkError, "更新成功");
    }
    
    
    @PostMapping("/available/{id}")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'content.articles.edit')")
    public Results available(@PathVariable("id") String id, boolean available) {
        articlesService.available(id, available);
        return Results.success()
                .setMessage(String.format("%s成功", available ? "开启" : "禁用"));
    }
    
    
    @PostMapping("/allow-comments/{id}")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'content.articles.edit')")
    public Results allowComments(@PathVariable("id") String id, boolean allowComments) {
        articlesService.allowComments(id, allowComments);
        return Results.success()
                .setMessage(String.format("%s成功", allowComments ? "开启" : "禁用"));
    }
    
    
    /**
     * 删除
     *
     * @param filter
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasPermission(null, 'content.articles.delete')")
    public Results delete(@Validated IdsFilter filter,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        articlesService.deletes(filter.getId(), checkError);
        return Results.check(checkError, "删除成功");
    }
}
