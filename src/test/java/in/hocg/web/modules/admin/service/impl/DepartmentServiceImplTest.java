package in.hocg.web.modules.admin.service.impl;

import in.hocg.web.modules.admin.domain.Department;
import in.hocg.web.modules.admin.service.DepartmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by hocgin on 2017/10/28.
 * email: hocgin@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DepartmentServiceImplTest {
    
    @Autowired
    DepartmentService departmentService;
    
    @Test
    public void queryRoot() throws Exception {
        List<Department> departments = departmentService.queryRoot();
        System.out.println(departments.size());
    }
    
}