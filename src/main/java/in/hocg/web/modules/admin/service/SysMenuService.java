package in.hocg.web.modules.admin.service;

import in.hocg.web.modules.admin.filter.MenuFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.admin.domain.SysMenu;

import java.util.List;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
public interface SysMenuService {
    void insert(MenuFilter filter, CheckError checkError);
    void delete(String id);
    void update(MenuFilter filter, CheckError checkError);
    
    List<SysMenu> queryChildren(String parentId);
    
    List<SysMenu> queryRoot();
    
    SysMenu findById(String id);
    
    List<SysMenu> queryById(String... id);
    
    List<SysMenu> queryAllByIdOrderByPathAsc(String... id);
    
    
    void updateAvailable(String id, boolean b);
    
    void sort(String... ids);
    
    List<SysMenu> queryAllOrderByLocationAscAndPathAsc();
}
