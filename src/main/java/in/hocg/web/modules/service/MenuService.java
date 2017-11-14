package in.hocg.web.modules.service;

import in.hocg.web.filter.MenuFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.Menu;

import java.util.List;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
public interface MenuService {
    void insert(MenuFilter filter, CheckError checkError);
    void delete(String id);
    void update(MenuFilter filter, CheckError checkError);
    
    List<Menu> queryChildren(String parentId);
    
    List<Menu> queryRoot();
    
    Menu findById(String id);
    
    List<Menu> queryById(String... id);
    List<Menu> queryAllByIdOrderByPathAes(String... id);
    
    void updateAvailable(String id, boolean b);
}
