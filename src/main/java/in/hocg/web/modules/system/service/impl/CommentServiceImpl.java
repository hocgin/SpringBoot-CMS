package in.hocg.web.modules.system.service.impl;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.SecurityKit;
import in.hocg.web.modules.api.filter.CommentAddFilter;
import in.hocg.web.modules.api.filter.CommentQueryFilter;
import in.hocg.web.modules.base.Base2Service;
import in.hocg.web.modules.base.body.Page;
import in.hocg.web.modules.system.domain.Articles;
import in.hocg.web.modules.system.domain.Comment;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.domain.repository.CommentRepository;
import in.hocg.web.modules.system.filter.CommentDataTablesInputFilter;
import in.hocg.web.modules.system.service.ArticlesService;
import in.hocg.web.modules.system.service.CommentService;
import in.hocg.web.modules.system.service.MemberService;
import in.hocg.web.modules.system.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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
    private MemberService memberService;
    private UserService userService;
    
    @Autowired
    public CommentServiceImpl(ArticlesService articlesService,
                              MemberService memberService,
                              UserService userService) {
        this.articlesService = articlesService;
        this.memberService = memberService;
        this.userService = userService;
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
        User member = userService.findOne(SecurityKit.iUser().getId());
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
        
        // -》 @。@ 评论关键字 黑名单 ？
        
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
    public DataTablesOutput data(CommentDataTablesInputFilter filter) {
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(filter.getOid())) {
            criteria.and("oid").is(filter.getOid());
        }
        if (!StringUtils.isEmpty(filter.getRegexMessage())) {
            criteria.and("content.message").regex(String.format("%s.*", filter.getRegexMessage()));
        }
        if (!StringUtils.isEmpty(filter.getType())) {
            criteria.and("type").is(filter.getType());
        }
        if (!StringUtils.isEmpty(filter.getEmail())) {
            User member = memberService.findByEmail(filter.getEmail());
            if (!ObjectUtils.isEmpty(member)) {
                criteria.and("member.$id").is(new ObjectId(member.getId()));
            }
        }
        return repository.findAll(filter, criteria);
    }
    
    @Override
    public void deleteAllByOidInAndType(String[] id, Integer type) {
        repository.deleteAllByOidInAndType(id, type);
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
            if (!articles.getAllowComments()) {
                checkError.putError("文章不允许评论");
                return false;
            }
            return true;
        } else {
            checkError.putError("评论异常");
        }
        return false;
    }
}
