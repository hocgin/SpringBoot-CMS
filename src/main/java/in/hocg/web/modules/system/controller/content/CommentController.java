package in.hocg.web.modules.system.controller.content;

import in.hocg.web.modules.base.BaseController;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.system.domain.Comment;
import in.hocg.web.modules.system.domain.Member;
import in.hocg.web.modules.system.filter.CommentDataTablesInputFilter;
import in.hocg.web.modules.system.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Controller
@RequestMapping("/admin/content/comment")
public class CommentController extends BaseController {
    public final String BASE_TEMPLATES_PATH = "/admin/content/comment/%s";
    
    private CommentService commentService;
    
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    
    @GetMapping({"/index.html", "/"})
    public String vIndex(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "index");
    }
    
    @GetMapping("/query-modal.html")
    public String vQueryModal() {
        return String.format(BASE_TEMPLATES_PATH, "query-modal");
    }
    
    @GetMapping("/add-view.html")
    public String vAdd(Model model) {
        return String.format(BASE_TEMPLATES_PATH, "add-view");
    }

    @GetMapping("/detail/{id}")
    public String vDetail(@PathVariable("id") String id, Model model) {
        Comment comment = commentService.findOne(id);
        Comment.Content content = comment.getContent();
        if (!content.getMembers().isEmpty()) {
            Member member = comment.getMember();
            content.setMessage(String.format("回复 @%s:%s", member.getNickname(), content.getMessage()));
        }
        model.addAttribute("comment", comment);
        return String.format(BASE_TEMPLATES_PATH, "detail-modal");
    }
    
// 浏览目标，例如: 文章(ID:1212), 跳转到文章(ID:1212)页面
//    @GetMapping("/browser/{id}")
//    public String vBrowser(@PathVariable("id") String id, Model model) throws IOException {
//        model.addAttribute("comment", commentService.findOne(id));
//        return String.format(BASE_TEMPLATES_PATH, "browser-view");
//    }
    
    @PostMapping("/data")
    @ResponseBody
    public DataTablesOutput data(@RequestBody CommentDataTablesInputFilter input) {
        return commentService.data(input);
    }
    
    
    @PostMapping("/available/{id}")
    @ResponseBody
//    @PreAuthorize("hasPermission(null, 'content.comment.edit')")
    public Results available(@PathVariable("id") String id, boolean available) {
        commentService.available(id, available);
        return Results.success()
                .setMessage(String.format("%s成功", available ? "开启" : "禁用"));
    }
    
}
