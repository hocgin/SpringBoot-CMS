package in.hocg.web.database;

import in.hocg.web.modules.domain.Department;
import in.hocg.web.modules.domain.Menu;
import in.hocg.web.modules.domain.Role;
import in.hocg.web.modules.domain.User;
import in.hocg.web.modules.domain.repository.DepartmentRepository;
import in.hocg.web.modules.domain.repository.MenuRepository;
import in.hocg.web.modules.domain.repository.RoleRepository;
import in.hocg.web.modules.domain.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by hocgin on 2017/11/14.
 * email: hocgin@gmail.com
 */
@Component
public class BuiltInSeeder {
    private RoleRepository roleRepository;
    private DepartmentRepository departmentRepository;
    private UserRepository userRepository;
    private MenuRepository menuRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public BuiltInSeeder(RoleRepository roleRepository, DepartmentRepository departmentRepository, UserRepository userRepository, MenuRepository menuRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.menuRepository = menuRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    public BuiltInSeeder drop() {
        roleRepository.deleteAll();
        departmentRepository.deleteAll();
        userRepository.deleteAll();
        menuRepository.deleteAll();
        return this;
    }
    
    public void init() {
        /**
         * 初始化单位
         * 内置单位
         *   - 后台管理
         *   - 前台管理
         */
        Department d1 = DocumentFactory.department("内置单位", "0001");
        d1 = departmentRepository.insert(d1);
        
        Department d11 = DocumentFactory.department("后台管理", "00010001");
        d11.setParent(d1.getId());
        d11.setHasChildren(false);
        d11 = departmentRepository.insert(d11);
        
        Department d12 = DocumentFactory.department("前台管理", "00010002");
        d12.setParent(d1.getId());
        d12.setHasChildren(false);
        d12 = departmentRepository.insert(d12);
        
        /**
         * 菜单
         * 系统管理
         *   - 系统参数
         *   - 单位管理
         *   - 菜单管理
         *   - 角色管理
         *   - 用户管理
         * 系统安全
         *   - 系统日志
         */
        Menu menu1 = DocumentFactory.menu("系统管理", "0001", "sys", "");
        menu1 = menuRepository.insert(menu1);
        
        // - 系统变量管理
        Menu menu11 = DocumentFactory.menu("系统变量", "00010001",
                "sys.variable",
                "/admin/system/variable/index.html");
        menu11.setParent(menu1.getId());
        menu11 = menuRepository.insert(menu11);
        Menu menu111 = DocumentFactory.data("添加系统变量", "000100010001", "sys.variable.add", menu11.getId());
        menu111 = menuRepository.insert(menu111);
        Menu menu112 = DocumentFactory.data("删除系统变量", "000100010002", "sys.variable.delete", menu11.getId());
        menu112 = menuRepository.insert(menu112);
        Menu menu113 = DocumentFactory.data("修改系统变量", "000100010003", "sys.variable.edit", menu11.getId());
        menu113 = menuRepository.insert(menu113);
        
        // - 单位管理
        Menu menu12 = DocumentFactory.menu("单位管理", "00010002",
                "sys.department", "/admin/system/department/index.html");
        menu12.setParent(menu1.getId());
        menu12 = menuRepository.insert(menu12);
        Menu menu121 = DocumentFactory.data("添加单位", "000100020001", "sys.department.add", menu12.getId());
        menu121 = menuRepository.insert(menu121);
        Menu menu122 = DocumentFactory.data("删除单位", "000100020002", "sys.department.delete", menu12.getId());
        menu122 = menuRepository.insert(menu122);
        Menu menu123 = DocumentFactory.data("修改单位", "000100020003", "sys.department.edit", menu12.getId());
        menu123 = menuRepository.insert(menu123);
        
        // - 菜单管理
        Menu menu13 = DocumentFactory.menu("菜单管理", "00010003",
                "sys.menu", "/admin/system/menu/index.html");
        menu13.setParent(menu1.getId());
        menu13 = menuRepository.insert(menu13);
        Menu menu131 = DocumentFactory.data("添加菜单", "000100030001", "sys.menu.add", menu13.getId());
        menu131 = menuRepository.insert(menu131);
        Menu menu132 = DocumentFactory.data("删除菜单", "000100030002", "sys.menu.delete", menu13.getId());
        menu132 = menuRepository.insert(menu132);
        Menu menu133 = DocumentFactory.data("修改菜单", "000100030003", "sys.menu.edit", menu13.getId());
        menu133 = menuRepository.insert(menu133);
        
        // - 角色管理
        Menu menu14 = DocumentFactory.menu("角色管理", "00010004",
                "sys.role", "/admin/system/role/index.html");
        menu14.setParent(menu1.getId());
        menu14 = menuRepository.insert(menu14);
        Menu menu141 = DocumentFactory.data("添加角色", "000100040001", "sys.role.add", menu14.getId());
        menu141 = menuRepository.insert(menu141);
        Menu menu142 = DocumentFactory.data("删除角色", "000100040002", "sys.role.delete", menu14.getId());
        menu142 = menuRepository.insert(menu142);
        Menu menu143 = DocumentFactory.data("修改角色", "000100040003", "sys.role.edit", menu14.getId());
        menu143 = menuRepository.insert(menu143);
        
        // - 用户管理
        Menu menu15 = DocumentFactory.menu("用户管理", "00010005",
                "sys.user", "/admin/system/user/index.html");
        menu15.setParent(menu1.getId());
        menu15 = menuRepository.insert(menu15);
        Menu menu151 = DocumentFactory.data("添加用户", "000100050001", "sys.user.add", menu15.getId());
        menu151 = menuRepository.insert(menu151);
        Menu menu152 = DocumentFactory.data("删除用户", "000100050002", "sys.user.delete", menu15.getId());
        menu152 = menuRepository.insert(menu152);
        Menu menu153 = DocumentFactory.data("修改用户", "000100050003", "sys.user.edit", menu15.getId());
        menu153 = menuRepository.insert(menu153);
    
    
        // 系统安全
        Menu menu2 = DocumentFactory.menu("系统安全", "0002", "sys", "");
        menu2 = menuRepository.insert(menu2);
        // - 日志管理 todo 后续增加查询功能
        Menu menu26 = DocumentFactory.menu("系统日志", "00020006",
                "sys.log", "/admin/system/log/index.html");
        menu26.setParent(menu2.getId());
        menu26 = menuRepository.insert(menu26);
        Menu menu261 = DocumentFactory.data("清空日志", "000200060001", "sys.log.empty", menu26.getId());
        menu261 = menuRepository.insert(menu261);
        
        
        
        // 仪表盘
        Menu menu3 = DocumentFactory.menu("仪表盘", "0002", "dashboard", "");
        menu3 = menuRepository.insert(menu3);
        // - 主界面
        Menu menu31 = DocumentFactory.menu("主界面", "00020001",
                "dashboard.index", "/admin/dashboard/index.html");
        menu31.setParent(menu2.getId());
        menu31 = menuRepository.insert(menu31);
        
        Menu[] role_admin = new Menu[]{
                menu1,
                menu11, menu111, menu112, menu113,
                menu12, menu121, menu122, menu123,
                menu13, menu131, menu132, menu133,
                menu14, menu141, menu142, menu143,
                menu15, menu151, menu152, menu153,
                menu2,
                menu26, menu261,
                menu3,
                menu31
        };
    
        Menu[] role_admin_old = new Menu[]{
                menu1,
                menu11, menu12, menu13, menu14, menu15,
                menu2,
                menu26,
                menu3,
                menu31
        };
        
        /**
         * 初始化角色
         * 内置单位
         *   - 后台管理
         *      - 管理员
         *   - 前台管理
         *      - 普通用户
         */
        Role r1 = DocumentFactory.role(Role.ROLE_USER, "普通用户", d12);
        r1 = roleRepository.insert(r1);
        Role r2 = DocumentFactory.role(Role.ROLE_ADMIN, "管理员", d11);
        r2.setPermissions(Arrays.asList(role_admin));
        r2 = roleRepository.insert(r2);
        Role r3 = DocumentFactory.role("ROLE_ADMIN_OLD", "假管理员", d11);
        r3.setPermissions(Arrays.asList(role_admin_old));
        r3.setBuiltIn(false);
        r3 = roleRepository.insert(r3);
        
        /**
         * 初始化用户
         * 内置单位
         *   - 后台管理
         *      - 管理员
         *          - admin
         *   - 前台管理
         *      - 普通用户
         */
        User user = DocumentFactory.user("admin",
                Collections.singleton(r2),
                "hocgin@gmail.com",
                bCryptPasswordEncoder.encode("admin"));
        user = userRepository.insert(user);
        
        User user2 = DocumentFactory.user("adm1n",
                Collections.singleton(r3),
                "adm1n@gmail.com",
                bCryptPasswordEncoder.encode("adm1n"));
        user2.setBuiltIn(false);
        user2 = userRepository.insert(user2);
        
        
    }
}
