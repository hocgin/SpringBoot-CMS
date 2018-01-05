package in.hocg.web.modules.web;

import in.hocg.web.modules.system.service.ArticlesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hocgin on 2018/1/5.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/article")
public class WebArticleController {
    public final String BASE_TEMPLATES_PATH = "/web/article/%s";
    
    @Autowired
    private ArticlesService articlesService;
    
    @GetMapping("/{id}")
    public String index(@PathVariable String id, Model model) {
        model.addAttribute("article", articlesService.findOne(id));
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
}
