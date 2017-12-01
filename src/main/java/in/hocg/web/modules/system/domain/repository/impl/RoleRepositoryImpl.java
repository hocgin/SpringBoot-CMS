package in.hocg.web.modules.system.domain.repository.impl;

import com.mongodb.DBRef;
import in.hocg.web.modules.system.domain.SysMenu;
import in.hocg.web.modules.system.domain.Role;
import in.hocg.web.modules.system.domain.repository.custom.RoleRepositoryCustom;
import in.hocg.web.modules.base.BaseMongoCustom;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public class RoleRepositoryImpl
        extends BaseMongoCustom<Role, String>
        implements RoleRepositoryCustom {
    
    @Override
    public void removePermissionForAllRole(String... id) {
        ObjectId[] objectIds = Arrays.stream(id)
                .map(ObjectId::new)
                .toArray(ObjectId[]::new);
        DBRef[] dbRefs = Arrays.stream(objectIds)
                .map(i -> new DBRef(SysMenu.Document, i))
                .toArray(DBRef[]::new);
        
        Query query = Query.query(Criteria.where("permissions.$id").in(objectIds));
        Update update = new Update().pullAll("permissions", dbRefs);
        
        updateMulti(query, update);
    }
    
    @Override
    public List<Role> findAllByPath(String path) {
        return findAll().stream()
                .filter(role -> role.getDepartment().getPath().startsWith(path))
                .collect(Collectors.toList());
    }
}
