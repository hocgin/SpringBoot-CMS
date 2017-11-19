package in.hocg.web.modules.admin.domain.repository;

import in.hocg.web.modules.admin.domain.SysMenu;
import in.hocg.web.modules.admin.domain.repository.custom.SysMenuRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
public interface SysMenuRepository
        extends MongoRepository<SysMenu, String>,
        SysMenuRepositoryCustom {
    List<SysMenu> findAllByParentIn(String... parentIds);
    
    void deleteAllByIdIn(String... ids);
    
    List<SysMenu> findAllByIdIn(String... ids);
    
    long countByParent(String parent);
    
    /**
     * 用正则匹配查询目录路径的值
     *
     * @param path
     * @return
     */
    List<SysMenu> findAllByPathRegex(String path);
    
    
    SysMenu findByPermission(String permission);
}
