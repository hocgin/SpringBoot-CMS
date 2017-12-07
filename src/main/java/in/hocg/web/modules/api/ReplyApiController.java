package in.hocg.web.modules.api;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.api.filter.CommentAddFilter;
import in.hocg.web.modules.api.filter.CommentQueryFilter;
import in.hocg.web.modules.base.body.Page;
import in.hocg.web.modules.base.body.Results;
import in.hocg.web.modules.system.domain.Comment;
import in.hocg.web.modules.system.domain.Member;
import in.hocg.web.modules.system.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2017/12/7.
 * email: hocgin@gmail.com
 */
@RestController
@RequestMapping("/api/v1/reply")
public class ReplyApiController {
    private CommentService commentService;
    
    @Autowired
    public ReplyApiController(CommentService commentService) {
        this.commentService = commentService;
    }
    
    /**
     * 获取文章的顶级评论
     *
     * @param filter
     * @param bindingResult
     * @return
     */
    @PostMapping(value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Results pager(@Validated({CommentQueryFilter.Reply.class}) CommentQueryFilter filter,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        Page<Comment> pager = commentService.pagerReply(filter);
        return Results.success(pager)
                .setMessage("获取评论成功");
    }
    
    /**
     * 获取顶级评论的评论
     *
     * @param filter
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/reply", produces = MediaType.APPLICATION_JSON_VALUE)
    public Results reply(@Validated({CommentQueryFilter.ReplyReply.class}) CommentQueryFilter filter,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        Page<Comment> pager = commentService.pagerReplyReply(filter);
        
        pager.setResult(pager.getResult().stream().peek(comment -> {
            Comment.Content content = comment.getContent();
            Member member = comment.getMember();
            content.setMessage(String.format("回复 @%s:%s", member.getNickname(), content.getMessage()));
        }).collect(Collectors.toList()));
        return Results.success(pager)
                .setMessage("获取评论成功");
    }
    
    /**
     * 进行评论
     *
     * @param filter
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public Results add(@Validated CommentAddFilter filter,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Results.check(bindingResult);
        }
        CheckError checkError = CheckError.get();
        Comment comment = Optional.ofNullable(commentService.insert(filter, checkError))
                .map(cmt -> {
                    if (!ObjectUtils.isEmpty(cmt.getRoot())) {
                        Comment.Content content = cmt.getContent();
                        Member member = cmt.getMember();
                        content.setMessage(String.format("回复 @%s:%s", member.getNickname(), content.getMessage()));
                    }
                    return cmt;
                }).orElse(null);
        return Results.check(checkError, "评论成功")
                .setData(comment);
    }
}
