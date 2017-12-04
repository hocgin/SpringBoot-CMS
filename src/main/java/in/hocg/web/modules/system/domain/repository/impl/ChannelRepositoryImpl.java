package in.hocg.web.modules.system.domain.repository.impl;

import in.hocg.web.modules.base.BaseMongoCustom;
import in.hocg.web.modules.system.domain.Channel;
import in.hocg.web.modules.system.domain.repository.custom.ChannelRepositoryCustom;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
public class ChannelRepositoryImpl
        extends BaseMongoCustom<Channel, String>
        implements ChannelRepositoryCustom {
    
    @Override
    public void updateHasChildren(String id, boolean hasChildren) {
        updateMulti(Query.query(Criteria.where("id").is(id)),
                Update.update("hasChildren", hasChildren));
    }
    
    @Override
    public void deleteAllByPathRegex(String regexPath) {
        findAllAndRemove(Query.query(Criteria.where("path")
                .regex(regexPath)));
    }
    
    @Override
    public List<Channel> findAllByPathRegexOrderByPathDesc(String regexPath) {
        return find(Query.query(Criteria.where("path")
                .regex(regexPath))
                .with(new Sort(new Sort.Order(Sort.Direction.DESC, "path"))));
    }
    
    @Override
    public List<Channel> findAllByIdOrderByPathAsc(String... id) {
        return find(Query.query(Criteria.where("id").in(id))
                .with(new Sort(new Sort.Order(Sort.Direction.ASC, "path"))));
    }
    
    @Override
    public List<Channel> findAll() {
        return super.findAll();
    }
    
    @Override
    public void updateLocation(String... ids) {
        findAndModify(new Query(), Update.update("location", 0));
        for (int i = 0; i < ids.length; i++) {
            updateFirst(Query.query(Criteria.where("id").is(ids[i])),
                    Update.update("location", i));
        }
    }
    
    @Override
    public List<Channel> findAllOrderByLocationAscAndPathAsc() {
        return find(new Query().with(new Sort(
                new Sort.Order(Sort.Direction.ASC, "location"),
                new Sort.Order(Sort.Direction.ASC, "path"))));
    }
    
    @Override
    public List<Channel> findAllByParentInOrderByLocationAscAndPathAsc(String... parent) {
    
        return find(Query.query(Criteria.where("parent").in(parent)).with(new Sort(
                new Sort.Order(Sort.Direction.ASC, "location"),
                new Sort.Order(Sort.Direction.ASC, "path"))));
    }
}
