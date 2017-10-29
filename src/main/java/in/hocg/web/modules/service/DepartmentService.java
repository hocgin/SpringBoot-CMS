package in.hocg.web.modules.service;

import in.hocg.web.modules.domain.Department;

import java.util.List;

/**
 * Created by hocgin on 2017/10/28.
 * email: hocgin@gmail.com
 */
public interface DepartmentService {
    void insert(Department department);
    void delete(String id);
    void update(Department department);
    List<Department> queryChildren(String parentId);
    List<Department> queryRoot();
    
    Department findById(String id);
}
