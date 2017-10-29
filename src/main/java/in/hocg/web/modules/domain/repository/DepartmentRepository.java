package in.hocg.web.modules.domain.repository;

import in.hocg.web.modules.domain.Department;
import in.hocg.web.modules.domain.repository.custom.DepartmentCustom;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by hocgin on 2017/10/28.
 * email: hocgin@gmail.com
 */
public interface DepartmentRepository extends MongoRepository<Department, String>,
        DataTablesRepository<Department, String>,
        DepartmentCustom {
    void deleteAllByIdIn(String... id);
    
    List<Department> findAllByIdIn(String... ids);
    
    List<Department> findAllByParentIn(String... parentId);
}
