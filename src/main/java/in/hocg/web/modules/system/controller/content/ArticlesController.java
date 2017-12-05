package in.hocg.web.modules.system.controller.content;

import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.system.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/content/articles")
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
    
    @PostMapping("/data")
    @ResponseBody
    public DataTablesOutput data(@RequestBody DataTablesInput input) {
        return articlesService.data(input);
    }
}
