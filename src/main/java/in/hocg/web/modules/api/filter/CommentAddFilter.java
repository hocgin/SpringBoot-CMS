package in.hocg.web.modules.api.filter;

import in.hocg.web.modules.system.domain.Comment;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by hocgin on 2017/12/7.
 * email: hocgin@gmail.com
 */
@Data
public class CommentAddFilter {
    /**
     * 评论的对象
     */
    @NotNull(message = "评论异常")
    private String oid;
    @NotNull(message = "评论异常")
    private Integer type;
    
    
    private String root;   // 顶级评论的ID
    private String parent;  // 评论评论的ID
    
    /**
     * 评论内容
     */
    @NotEmpty(message = "请发送3到1000字且非纯表情的评论")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$", message = "内容不合法")
    private String message;
    
    public Comment get() {
        Comment comment = new Comment();
        comment.setOid(oid);
        comment.setType(type);
        Comment.Content content = new Comment.Content();
        content.setMessage(message);
        comment.setContent(content);
        comment.setAvailable(true);
        comment.createdAt();
        return comment;
    }
}
