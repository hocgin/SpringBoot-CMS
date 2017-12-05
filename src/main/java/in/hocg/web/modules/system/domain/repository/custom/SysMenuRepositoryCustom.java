package in.hocg.web.modules.system.domain.repository.custom;

import in.hocg.web.modules.system.domain.SysMenu;

import java.util.List;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 * 自定义接口 与 JPA 区分
 */
public interface SysMenuRepositoryCustom {
    void updateHasChildren(String id, boolean hasChildren);
    
    /**
     * 级联删除
     * @param regexPath
     */
    void deleteAllByPathRegex(String regexPath);
    
    /**
     * 用正则匹配查询目录路径的值, 并进行降序
     *
     * @param regexPath
     * @return
     */
    List<SysMenu> findAllByPathRegexOrderByPathDesc(String regexPath);
    
    
    List<SysMenu> findAllByIdOrderByPathAsc(String... id);
    
    void updateLocation(String... ids);
    
    /**
     * path 00010002 -> 00010001 -> 0001
     * location 0 -> 1 -> 2
     * @return
     */
    List<SysMenu> findAllOrderByLocationAscAndPathAsc();
    
    List<SysMenu> findAllByParentInOrderByLocationAscAndPathAsc(String... parent);
}
