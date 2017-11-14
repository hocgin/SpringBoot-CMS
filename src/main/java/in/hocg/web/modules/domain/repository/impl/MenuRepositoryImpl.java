package in.hocg.web.modules.domain.repository.impl;

import in.hocg.web.modules.domain.Menu;
import in.hocg.web.modules.domain.repository.custom.MenuRepositoryCustom;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
public class MenuRepositoryImpl
        extends BaseMongoCustom<Menu, String>
        implements MenuRepositoryCustom {
    
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
    public List<Menu> findAllByPathRegexOrderByPathDesc(String regexPath) {
        return find(Query.query(Criteria.where("path")
                .regex(regexPath))
                .with(new Sort(new Sort.Order(Sort.Direction.DESC, "path"))));
    }
    
    @Override
    public List<Menu> findAllByIdOrderByPathAes(String... id) {
        return find(Query.query(Criteria.where("id").in(id))
                .with(new Sort(new Sort.Order(Sort.Direction.ASC, "path"))));
    }
}
