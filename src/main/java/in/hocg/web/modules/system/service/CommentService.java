package in.hocg.web.modules.system.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.api.filter.CommentAddFilter;
import in.hocg.web.modules.api.filter.CommentQueryFilter;
import in.hocg.web.modules.base.body.Page;
import in.hocg.web.modules.system.domain.Comment;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

/**
 * Created by hocgin on 2017/12/7.
 * email: hocgin@gmail.com
 */
public interface CommentService {
    Page<Comment> pagerReply(CommentQueryFilter filter);
    Page<Comment> pagerReplyReply(CommentQueryFilter filter);
    
    Comment insert(CommentAddFilter filter, CheckError checkError);
    
    void available(String id, boolean available);
    
    DataTablesOutput data(DataTablesInput input);
    
    void delete(String id, CheckError checkError);
}
