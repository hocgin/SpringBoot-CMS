package in.hocg.web;

import in.hocg.web.modules.domain.Department;
import in.hocg.web.modules.domain.Role;
import in.hocg.web.modules.domain.repository.DepartmentRepository;
import in.hocg.web.modules.domain.repository.MenuRepository;
import in.hocg.web.modules.domain.repository.RoleRepository;
import in.hocg.web.modules.domain.repository.UserRepository;
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
    private MenuRepository permissionRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Test
    public void testInitRole() {
        Role role = new Role();
        role.setRole(Role.ROLE_USER);
        role.setAvailable(true);
        role.setName("普通用户");
        roleRepository.save(role);
    }
    
    @Test
    public void test() {
        List<Department> allByPathRegexOrderByPathAsc = departmentRepository.findAllByPathRegexOrderByPathDesc(".{4}");
        System.out.println("");
    }
}
