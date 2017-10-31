package in.hocg.web.modules.domain.repository.impl;

import in.hocg.web.modules.domain.Permission;
import in.hocg.web.modules.domain.repository.custom.PermissionRepositoryCustom;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
public class PermissionRepositoryImpl
        extends BaseMongoCustom<Permission, String>
        implements PermissionRepositoryCustom {
    
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
    public List<Permission> findAllByPathRegexOrderByPathDesc(String regexPath) {
        return find(Query.query(Criteria.where("path")
                .regex(regexPath))
                .with(new Sort(new Sort.Order(Sort.Direction.DESC, "path"))));
    }
}
