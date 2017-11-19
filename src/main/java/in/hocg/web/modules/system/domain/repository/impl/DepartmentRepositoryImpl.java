package in.hocg.web.modules.system.domain.repository.impl;

import in.hocg.web.modules.system.domain.Department;
import in.hocg.web.modules.system.domain.repository.custom.DepartmentRepositoryCustom;
import in.hocg.web.modules.base.BaseMongoCustom;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
public class DepartmentRepositoryImpl
        extends BaseMongoCustom<Department, String>
        implements DepartmentRepositoryCustom {
    
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
    public List<Department> findAllByPathRegexOrderByPathDesc(String regexPath) {
        return find(Query.query(Criteria.where("path")
                .regex(regexPath))
                .with(new Sort(new Sort.Order(Sort.Direction.DESC, "path"))));
    }
}
