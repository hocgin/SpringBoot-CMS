package in.hocg.web.modules.domain.repository.custom;

import in.hocg.web.modules.domain.Department;

import java.util.List;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 * 自定义接口 与 JPA 区分
 */
public interface DepartmentRepositoryCustom {
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
    List<Department> findAllByPathRegexOrderByPathDesc(String regexPath);
}
