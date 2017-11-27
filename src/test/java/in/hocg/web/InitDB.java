package in.hocg.web;

import in.hocg.web.modules.system.domain.Department;
import in.hocg.web.modules.system.domain.repository.DepartmentRepository;
import in.hocg.web.modules.system.domain.repository.RoleRepository;
import in.hocg.web.modules.system.domain.repository.SysMenuRepository;
import in.hocg.web.modules.system.domain.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InitDB {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SysMenuRepository permissionRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Test
    public void testInitRole() {
    }
    
    @Test
    public void test() {
        List<Department> allByPathRegexOrderByPathAsc = departmentRepository.findAllByPathRegexOrderByPathDesc("^%s.{4}$");
        System.out.println("");
    }
}
