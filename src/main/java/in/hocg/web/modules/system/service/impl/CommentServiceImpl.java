package in.hocg.web.modules.system.service.impl;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.SecurityKit;
import in.hocg.web.modules.api.filter.CommentAddFilter;
import in.hocg.web.modules.api.filter.CommentQueryFilter;
import in.hocg.web.modules.base.Base2Service;
import in.hocg.web.modules.base.body.Page;
import in.hocg.web.modules.system.domain.Articles;
import in.hocg.web.modules.system.domain.Comment;
import in.hocg.web.modules.system.domain.Member;
import in.hocg.web.modules.system.domain.repository.CommentRepository;
import in.hocg.web.modules.system.service.ArticlesService;
import in.hocg.web.modules.system.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * Created by hocgin on 2017/12/7.
 * email: hocgin@gmail.com
 */
@Service
public class CommentServiceImpl
        extends Base2Service<Comment, String, CommentRepository>
        implements CommentService {
    private ArticlesService articlesService;
    
    @Autowired
    public CommentServiceImpl(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }
    
    @Override
    public Page<Comment> pagerReply(CommentQueryFilter filter) {
        return repository.pagerReply(filter.getOid(), filter.getType(),
                filter.getPage(),
                filter.getSize());
    }
    
    @Override
    public Page<Comment> pagerReplyReply(CommentQueryFilter filter) {
        return repository.pagerReplyReply(filter.getOid(), filter.getType(), filter.getRoot(),
                filter.getPage(),
                filter.getSize());
    }
    
    @Override
    public Comment insert(CommentAddFilter filter, CheckError checkError) {
        Member member = SecurityKit.iMember().getMember();
        if (ObjectUtils.isEmpty(member)) {
            checkError.putError("请先登陆");
            return null;
        }
        Comment comment = filter.get();
        comment.setMember(member);
        if (!Comment.Type.exits(filter.getType())) {
            checkError.putError("类型异常");
            return null;
        }
        // 检查评论目标的限制
        if (!_checkOid(filter.getOid(), filter.getType(), checkError)) {
            return null;
        }
        
        // 如果是 回复评论
        if (!ObjectUtils.isEmpty(filter.getRoot())
                && !ObjectUtils.isEmpty(filter.getParent())) {
            // 根级评论
            Comment replyReply = repository.findOneReply(filter.getOid(), filter.getType(),
                    filter.getRoot());
            replyReply.setRcount(replyReply.getRcount() + 1);
            repository.save(replyReply);
            
            // 当为根级评论的评论时, 获取根级评论的评论
            if (!Objects.equals(filter.getRoot(), filter.getParent())) {
                replyReply = repository.findOneReplyReply(filter.getOid(), filter.getType(),
                        filter.getRoot(),
                        filter.getParent());
            }
            // 当回复的评论不存在
            if (ObjectUtils.isEmpty(replyReply)) {
                checkError.putError("评论异常");
                return null;
            }
            comment.getContent().addMember(replyReply.getMember());
            comment.setParent(filter.getParent());
            comment.setRoot(filter.getRoot());
        }
        
        // todo 评论信息 黑名单 ？
        
        return repository.insert(comment);
    }
    
    @Override
    public void available(String id, boolean available) {
        Comment comment = repository.findOne(id);
        if (!ObjectUtils.isEmpty(comment)) {
            comment.setAvailable(available);
            comment.updatedAt();
            repository.save(comment);
        }
    }
    
    @Override
    public DataTablesOutput data(DataTablesInput input) {
        return repository.findAll(input);
    }
    
    @Override
    public void delete(String id, CheckError checkError) {
        Comment comment = findOne(id);
        if (!ObjectUtils.isEmpty(comment)) {
            // todo 及联删除 子 评论？ 未完成
//            repository.deleteAllByRootOrParentAndOidAndType();
            repository.delete(id);
        }
    }
    
    /**
     * 检查评论目标的限制
     *
     * @param oid
     * @param type
     * @param checkError
     * @return true 为通过检查
     */
    private boolean _checkOid(String oid, Integer type, CheckError checkError) {
        if (Comment.Type.Article.getCode().equals(type)) {
            Articles articles = articlesService.findOne(oid);
            if (ObjectUtils.isEmpty(articles)
                    || !articles.getAvailable()) {
                checkError.putError("文章不存在或已被删除");
                return false;
            }
            // todo if 判断是否允许评论
            return true;
        } else {
            checkError.putError("评论异常");
        }
        return false;
    }
}
