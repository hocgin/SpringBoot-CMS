package in.hocg.web.modules.api.filter;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by hocgin on 2017/12/7.
 * email: hocgin@gmail.com
 */
@Data
public class CommentQueryFilter {
    public interface Reply {}
    
    public interface ReplyReply {}
    
    /**
     * 评论的对象
     */
    @NotNull(message = "获取评论列表异常", groups = {Reply.class, ReplyReply.class})
    private String oid;
    @NotNull(message = "获取评论列表异常", groups = {Reply.class, ReplyReply.class})
    private Integer type;
    
    @NotNull(message = "获取评论列表异常", groups = {ReplyReply.class})
    private String root;
    
    /**
     * 分页
     */
    private Integer size = 10;
    private Integer page = 1;
}
