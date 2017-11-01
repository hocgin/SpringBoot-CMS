package in.hocg.web.modules.domain.repository;

import in.hocg.web.modules.domain.Permission;
import in.hocg.web.modules.domain.repository.custom.PermissionRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
public interface PermissionRepository
        extends MongoRepository<Permission, String>,
        PermissionRepositoryCustom {
    List<Permission> findAllByParentIn(String... parentIds);
    
    void deleteAllByIdIn(String... ids);
    
    List<Permission> findAllByIdIn(String... ids);
    
    long countByParent(String parent);
    
    /**
     * 用正则匹配查询目录路径的值
     * @param path
     * @return
     */
    List<Permission> findAllByPathRegex(String path);
}
