package in.hocg.web.modules.service;

import in.hocg.web.filter.DepartmentFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.Department;

import java.util.List;

/**
 * Created by hocgin on 2017/10/28.
 * email: hocgin@gmail.com
 */
public interface DepartmentService {
    void insert(DepartmentFilter filter, CheckError checkError);
    void delete(String id, CheckError checkError);
    void update(DepartmentFilter department, CheckError checkError);
    List<Department> queryChildren(String parentId);
    List<Department> queryRoot();
    
    Department findById(String id);
}
