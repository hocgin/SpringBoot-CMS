package in.hocg.web.modules.system.domain.repository.impl;

import in.hocg.web.modules.base.BaseMongoCustom;
import in.hocg.web.modules.base.body.Page;
import in.hocg.web.modules.system.domain.Comment;
import in.hocg.web.modules.system.domain.repository.custom.CommentRepositoryCustom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.validation.constraints.NotNull;

/**
 * Created by hocgin on 2017/12/5.
 * email: hocgin@gmail.com
 */
public class CommentRepositoryImpl
        extends BaseMongoCustom<Comment, String>
        implements CommentRepositoryCustom {
    
    /**
     * 查找根评论的子评论
     * @param oid
     * @param type
     * @param root
     * @param id
     * @return
     */
    @Override
    public Comment findOneReplyReply(@NotNull String oid, @NotNull Integer type,
                                     String root, String id) {
        Criteria criteria = Criteria.where("oid").is(oid)
                .and("type").is(type)
                .and("root").is(root)
                .and("id").is(id)
                .and("available").is(true);
        Query query = Query.query(criteria);
        return findOne(query);
    }
    
    /**
     * 查找根评论
     * @param oid
     * @param type
     * @param id
     * @return
     */
    @Override
    public Comment findOneReply(@NotNull String oid, @NotNull Integer type,
                                     String id) {
        Criteria criteria = Criteria.where("oid").is(oid)
                .and("type").is(type)
                .and("id").is(id)
                .and("available").is(true);
        Query query = Query.query(criteria);
        return findOne(query);
    }
    
    /**
     * 查询 目标对象的可见评论, 进行分页
     * @param oid
     * @param type
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Comment> pagerReply(@NotNull String oid, @NotNull Integer type, @NotNull int page, @NotNull int size) {
        Criteria criteria = Criteria.where("oid").is(oid)
                .and("type").is(type)
                .and("root").is(null)
                .and("available").is(true);
        return super.pageX(Query.query(criteria), (page - 1) < 0 ? 0 : (page - 1), size);
    }
    
    @Override
    public Page<Comment> pagerReplyReply(String oid, Integer type, String root, Integer page, Integer size) {
        Criteria criteria = Criteria.where("oid").is(oid)
                .and("type").is(type)
                .and("root").is(root)
                .and("available").is(true);
        return super.pageX(Query.query(criteria), (page - 1) < 0 ? 0 : (page - 1), size);
    }
}
