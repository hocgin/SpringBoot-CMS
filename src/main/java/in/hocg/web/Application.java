package in.hocg.web;

import in.hocg.web.modules.domain.Department;
import in.hocg.web.modules.domain.Role;
import in.hocg.web.modules.domain.User;
import in.hocg.web.modules.domain.repository.DepartmentRepository;
import in.hocg.web.modules.domain.repository.RoleRepository;
import in.hocg.web.modules.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepositoryFactoryBean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.Date;

@SpringBootApplication
@EnableMongoRepositories(repositoryFactoryBeanClass = DataTablesRepositoryFactoryBean.class)
public class Application {
    final Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${i.mongo.init}")
    private Boolean mongoInit = false;
    
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(Application.class)
                .listeners(new ApplicationListener<ApplicationEvent>() {
                    @Override
                    public void onApplicationEvent(ApplicationEvent event) {
//                        System.out.println(String.format("%s: %s", "监听日志", event));
                    }
                })
                .run(args);
    }
    
    
    /**
     * MongoDB 初始化
     */
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Bean
    CommandLineRunner preLoadMongo() throws Exception {
        return args -> {
            if (mongoInit) {
                departmentRepository.deleteAll();
                roleRepository.deleteAll();
                userRepository.deleteAll();
                logger.info("正在初始化 MongoDB 数据");
                /**
                 * 初始化单位
                 * 内置单位
                 *   - 后台管理
                 *   - 前台管理
                 */
                Department builtInDepartment = new Department();
                builtInDepartment.setCreatedAt(new Date());
                builtInDepartment.setHasChildren(true);
                builtInDepartment.setBuiltIn(true);
                builtInDepartment.setPath("0001");
                builtInDepartment.setName("内置单位");
                builtInDepartment = departmentRepository.insert(builtInDepartment);
                
                Department adminDepartment = new Department();
                adminDepartment.setCreatedAt(new Date());
                adminDepartment.setHasChildren(false);
                adminDepartment.setBuiltIn(true);
                adminDepartment.setPath(String.format("%s%s", "0001", "0001"));
                adminDepartment.setName("后台管理");
                adminDepartment.setParent(builtInDepartment.getId());
                adminDepartment = departmentRepository.insert(adminDepartment);
                
                Department frontDepartment = new Department();
                frontDepartment.setCreatedAt(new Date());
                frontDepartment.setHasChildren(false);
                frontDepartment.setBuiltIn(true);
                frontDepartment.setPath(String.format("%s%s", "0001", "0002"));
                frontDepartment.setName("前台管理");
                frontDepartment.setParent(builtInDepartment.getId());
                frontDepartment = departmentRepository.insert(frontDepartment);
                
                /**
                 * 初始化角色
                 * 内置单位
                 *   - 后台管理
                 *      - 超级管理员
                 *      - 管理员
                 *   - 前台管理
                 *      - 普通用户
                 */
                Role role1 = new Role();
                role1.setBuiltIn(true);
                role1.setRole(Role.ROLE_USER);
                role1.setDescription("内置分组, 普通用户");
                role1.setAvailable(true);
                role1.setName("普通用户");
                role1.setDepartment(frontDepartment);
                role1.setCreatedAt(new Date());
                role1 = roleRepository.insert(role1);
                
                Role role2 = new Role();
                role2.setBuiltIn(true);
                role2.setRole(Role.ROLE_SUPER_ADMIN);
                role2.setDescription("内置分组, 超级管理员");
                role2.setAvailable(true);
                role2.setName("超级管理员");
                role2.setDepartment(adminDepartment);
                role2.setCreatedAt(new Date());
                role2 = roleRepository.insert(role2);
                
                Role role3 = new Role();
                role3.setBuiltIn(true);
                role3.setRole(Role.ROLE_ADMIN);
                role3.setDescription("内置分组, 管理员");
                role3.setAvailable(true);
                role3.setName("管理员");
                role3.setDepartment(adminDepartment);
                role3.setCreatedAt(new Date());
                role3 = roleRepository.insert(role3);
                
                /**
                 * 初始化用户
                 * 内置单位
                 *   - 后台管理
                 *      - 超级管理员
                 *          - admin
                 *      - 管理员
                 *   - 前台管理
                 *      - 普通用户
                 */
                User admin = new User();
                admin.setCreatedAt(new Date());
                admin.setAvailable(true);
                admin.setUsername("admin");
                admin.setRole(Collections.singleton(role3));
                admin.setEmail("hocgin@gmail.com");
                admin.setPassword(bCryptPasswordEncoder.encode("admin"));
                admin = userRepository.insert(admin);
            }
        };
    }
}
