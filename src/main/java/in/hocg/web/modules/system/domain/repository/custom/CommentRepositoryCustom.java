package in.hocg.web.modules.system.domain.repository.custom;

import in.hocg.web.modules.base.body.Page;
import in.hocg.web.modules.system.domain.Comment;

import javax.validation.constraints.NotNull;

/**
 * Created by hocgin on 2017/12/7.
 * email: hocgin@gmail.com
 */
public interface CommentRepositoryCustom {
    Comment findOneReplyReply(@NotNull String oid, @NotNull Integer type,
                              String root, String id);
    
    Comment findOneReply(@NotNull String oid, @NotNull Integer type,
                         String id);
    
    
    Page<Comment> pagerReply(@NotNull String oid, @NotNull Integer type, @NotNull int page, @NotNull int size);
    
    Page<Comment> pagerReplyReply(String oid, Integer type, String root, Integer page, Integer size);
}
