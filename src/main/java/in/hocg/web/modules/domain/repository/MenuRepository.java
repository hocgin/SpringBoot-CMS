package in.hocg.web.modules.domain.repository;

import in.hocg.web.modules.domain.Menu;
import in.hocg.web.modules.domain.repository.custom.MenuRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
public interface MenuRepository
        extends MongoRepository<Menu, String>,
        MenuRepositoryCustom {
    List<Menu> findAllByParentIn(String... parentIds);
    
    void deleteAllByIdIn(String... ids);
    
    List<Menu> findAllByIdIn(String... ids);
    
    long countByParent(String parent);
    
    /**
     * 用正则匹配查询目录路径的值
     * @param path
     * @return
     */
    List<Menu> findAllByPathRegex(String path);
    
    
    Menu findByPermission(String permission);
}
