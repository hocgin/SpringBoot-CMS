package in.hocg.web.modules.domain.repository;

import in.hocg.web.modules.domain.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
public interface PermissionRepository extends MongoRepository<Permission, String> {
    List<Permission> findAllByParentIn(String... parentIds);
    
    void deleteAllByIdIn(String... ids);
}
