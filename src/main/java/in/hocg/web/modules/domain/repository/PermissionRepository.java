package in.hocg.web.modules.domain.repository;

import in.hocg.web.modules.domain.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
public interface PermissionRepository extends MongoRepository<Permission, String> {
}
