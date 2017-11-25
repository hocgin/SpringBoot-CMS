package in.hocg.web.modules.base;

import com.mongodb.WriteResult;
import in.hocg.web.lang.utils.Clazz;
import in.hocg.web.modules.base.body.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
public abstract class BaseMongoCustom<T, ID extends Serializable> {
    final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 实体类
     */
    private Class entityClass;
    private Class idClass;
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    public BaseMongoCustom() {
        entityClass = Clazz.getTypeParam(this.getClass(), 0);
        idClass = Clazz.getTypeParam(this.getClass(), 1);
    }
    
    public MongoTemplate mongoTemplate() {
        return mongoTemplate;
    }
    
    public void save(T entry) {
        mongoTemplate().save(entry);
    }
    
    public WriteResult remove(Query query) {
        doLog(query);
        return mongoTemplate().remove(query, getEntityClass());
    }
    
    public WriteResult updateFirst(Query query, Update update) {
        return mongoTemplate().updateFirst(query, update, getEntityClass());
    }
    
    public WriteResult updateMulti(Query query, Update update) {
        return mongoTemplate().updateMulti(query, update, getEntityClass());
    }
    
    public List<T> findAll() {
        return mongoTemplate().findAll(getEntityClass());
    }
    
    public List<T> find(Query query) {
        doLog(query);
        return mongoTemplate().find(query, getEntityClass());
    }
    
    public List<T> findAllAndRemove(Query query) {
        return mongoTemplate().findAllAndRemove(query, getEntityClass());
    }
    
    public T findAndRemove(Query query) {
        return mongoTemplate().findAndRemove(query, getEntityClass());
    }
    
    public T findAndModify(Query query, Update update) {
        return mongoTemplate().findAndModify(query, update, getEntityClass());
    }
    
    public T findOne(Query query) {
        doLog(query);
        return mongoTemplate().findOne(query, getEntityClass());
    }
    
    public T findById(Object id) {
        return mongoTemplate().findById(id, getEntityClass());
    }
    
    public long count(Query query) {
        doLog(query);
        return mongoTemplate().count(query, getEntityClass());
    }
    
    public boolean exists(Query query) {
        doLog(query);
        return mongoTemplate().exists(query, getEntityClass());
    }
    
    /**
     * 分页
     *
     * @param query 查询条件
     * @param page  请求页数, 0 为第一页
     * @param size  每页数量
     * @return
     */
    public List<T> page(Query query, int page, int size) {
        query = query.skip(page * size).limit(size);
        doLog(query);
        return find(query);
    }
    
    /**
     * 分页扩展
     *
     * @param query 查询条件
     * @param page  请求页数, 0 为第一页
     * @param size  每页数量
     * @return
     */
    public Page pageX(Query query, int page, int size) {
        return Page.New(size, count(query), page + 1, page(query, page, size));
    }
    
    
    public Class<T> getEntityClass() {
        return entityClass;
    }
    
    public void doLog(Query query) {
        logger.info("查询NoSQL: {}", query.toString());
    }
}
