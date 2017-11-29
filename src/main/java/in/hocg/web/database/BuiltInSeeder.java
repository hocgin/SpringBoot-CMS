package in.hocg.web.database;

import in.hocg.web.modules.system.domain.*;
import in.hocg.web.modules.system.domain.repository.*;
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
    private SysMenuRepository sysMenuRepository;
    private VariableRepository variableRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public BuiltInSeeder(
            VariableRepository variableRepository,
            RoleRepository roleRepository, DepartmentRepository departmentRepository, UserRepository userRepository, SysMenuRepository sysMenuRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.sysMenuRepository = sysMenuRepository;
        this.variableRepository = variableRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    public BuiltInSeeder drop() {
        roleRepository.deleteAll();
        departmentRepository.deleteAll();
        userRepository.deleteAll();
        sysMenuRepository.deleteAll();
        variableRepository.deleteAll();
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
        // 系统管理
        SysMenu menu1 = DocumentFactory.menu("系统管理", "0001", "sys", "");
        menu1.setLocation(1);
        menu1 = sysMenuRepository.insert(menu1);
        // 系统管理 - 系统变量管理
        SysMenu menu11 = DocumentFactory.menu("系统变量", "00010001",
                "sys.variable",
                "/admin/system/variable/index.html");
        menu11.setParent(menu1.getId());
        menu11 = sysMenuRepository.insert(menu11);
        SysMenu menu111 = DocumentFactory.data("添加系统变量", "000100010001", "sys.variable.add", menu11.getId());
        menu111 = sysMenuRepository.insert(menu111);
        SysMenu menu112 = DocumentFactory.data("删除系统变量", "000100010002", "sys.variable.delete", menu11.getId());
        menu112 = sysMenuRepository.insert(menu112);
        SysMenu menu113 = DocumentFactory.data("修改系统变量", "000100010003", "sys.variable.edit", menu11.getId());
        menu113 = sysMenuRepository.insert(menu113);
        // 系统管理 - 单位管理
        SysMenu menu12 = DocumentFactory.menu("单位管理", "00010002",
                "sys.department", "/admin/system/department/index.html");
        menu12.setParent(menu1.getId());
        menu12 = sysMenuRepository.insert(menu12);
        SysMenu menu121 = DocumentFactory.data("添加单位", "000100020001", "sys.department.add", menu12.getId());
        menu121 = sysMenuRepository.insert(menu121);
        SysMenu menu122 = DocumentFactory.data("删除单位", "000100020002", "sys.department.delete", menu12.getId());
        menu122 = sysMenuRepository.insert(menu122);
        SysMenu menu123 = DocumentFactory.data("修改单位", "000100020003", "sys.department.edit", menu12.getId());
        menu123 = sysMenuRepository.insert(menu123);
        // 系统管理 - 菜单管理
        SysMenu menu13 = DocumentFactory.menu("菜单管理", "00010003",
                "sys.menu", "/admin/system/menu/index.html");
        menu13.setParent(menu1.getId());
        menu13 = sysMenuRepository.insert(menu13);
        SysMenu menu131 = DocumentFactory.data("添加菜单", "000100030001", "sys.menu.add", menu13.getId());
        menu131 = sysMenuRepository.insert(menu131);
        SysMenu menu132 = DocumentFactory.data("删除菜单", "000100030002", "sys.menu.delete", menu13.getId());
        menu132 = sysMenuRepository.insert(menu132);
        SysMenu menu133 = DocumentFactory.data("修改菜单", "000100030003", "sys.menu.edit", menu13.getId());
        menu133 = sysMenuRepository.insert(menu133);
        // 系统管理 - 角色管理
        SysMenu menu14 = DocumentFactory.menu("角色管理", "00010004",
                "sys.role", "/admin/system/role/index.html");
        menu14.setParent(menu1.getId());
        menu14 = sysMenuRepository.insert(menu14);
        SysMenu menu141 = DocumentFactory.data("添加角色", "000100040001", "sys.role.add", menu14.getId());
        menu141 = sysMenuRepository.insert(menu141);
        SysMenu menu142 = DocumentFactory.data("删除角色", "000100040002", "sys.role.delete", menu14.getId());
        menu142 = sysMenuRepository.insert(menu142);
        SysMenu menu143 = DocumentFactory.data("修改角色", "000100040003", "sys.role.edit", menu14.getId());
        menu143 = sysMenuRepository.insert(menu143);
        // 系统管理 - 用户管理
        SysMenu menu15 = DocumentFactory.menu("用户管理", "00010005",
                "sys.user", "/admin/system/user/index.html");
        menu15.setParent(menu1.getId());
        menu15 = sysMenuRepository.insert(menu15);
        SysMenu menu151 = DocumentFactory.data("添加用户", "000100050001", "sys.user.add", menu15.getId());
        menu151 = sysMenuRepository.insert(menu151);
        SysMenu menu152 = DocumentFactory.data("删除用户", "000100050002", "sys.user.delete", menu15.getId());
        menu152 = sysMenuRepository.insert(menu152);
        SysMenu menu153 = DocumentFactory.data("修改用户", "000100050003", "sys.user.edit", menu15.getId());
        menu153 = sysMenuRepository.insert(menu153);
        // 系统管理 - 文件管理
        SysMenu menu16 = DocumentFactory.menu("文件管理", "00010006", "sys.file", "/admin/system/file/index.html");
        menu16.setParent(menu1.getId());
        menu16 = sysMenuRepository.insert(menu16);
        SysMenu menu161 = DocumentFactory.data("上传文件", "000100060001", "sys.file.add", menu16.getId());
        menu161 = sysMenuRepository.insert(menu161);
        SysMenu menu162 = DocumentFactory.data("删除文件", "000100060002", "sys.file.delete", menu16.getId());
        menu162 = sysMenuRepository.insert(menu162);
        SysMenu menu163 = DocumentFactory.data("修改文件", "000100060003", "sys.file.edit", menu16.getId());
        menu163 = sysMenuRepository.insert(menu163);
        // 系统管理 - 会员管理
        SysMenu menu17 = DocumentFactory.menu("会员管理", "00010007",
                "sys.member", "/admin/system/member/index.html");
        menu17.setParent(menu1.getId());
        menu17 = sysMenuRepository.insert(menu17);
        SysMenu menu171 = DocumentFactory.data("添加会员", "000100070001", "sys.member.add", menu17.getId());
        menu171 = sysMenuRepository.insert(menu171);
        SysMenu menu172 = DocumentFactory.data("删除会员", "000100070002", "sys.member.delete", menu17.getId());
        menu172 = sysMenuRepository.insert(menu172);
        SysMenu menu173 = DocumentFactory.data("修改会员", "000100070003", "sys.member.edit", menu17.getId());
        menu173 = sysMenuRepository.insert(menu173);
    
    
    
        // 系统安全
        String path = "0002";
        SysMenu menu2 = DocumentFactory.menu("系统安全", path, "safety", "");
        menu2.setLocation(2);
        menu2 = sysMenuRepository.insert(menu2);
        // - 日志管理
        SysMenu menu26 = DocumentFactory.menu("系统日志", "00020006",
                "safety.log", "/admin/system/log/index.html");
        menu26.setParent(menu2.getId());
        menu26 = sysMenuRepository.insert(menu26);
        SysMenu menu261 = DocumentFactory.data("清空日志", "000200060001", "safety.log.empty", menu26.getId());
        menu261 = sysMenuRepository.insert(menu261);
    
    
        // 仪表盘
        SysMenu menu3 = DocumentFactory.menu("仪表盘", "0003", "dashboard", "");
        menu3.setLocation(0);
        menu3 = sysMenuRepository.insert(menu3);
        // - 主界面
        SysMenu menu31 = DocumentFactory.menu("主界面", "00030001",
                "dashboard.index", "/admin/dashboard/index.html");
        menu31.setParent(menu3.getId());
        menu31 = sysMenuRepository.insert(menu31);
    
    
        // 天气服务
        SysMenu menu4 = DocumentFactory.menu("天气服务", "0004", "weather", "");
        menu4.setLocation(3);
        menu4 = sysMenuRepository.insert(menu4);
        // - 城市管理
        SysMenu menu41 = DocumentFactory.menu("城市管理", "00040001",
                "weather.city", "/admin/weather/city/index.html");
        menu41.setParent(menu4.getId());
        menu41 = sysMenuRepository.insert(menu41);
        SysMenu menu411 = DocumentFactory.data("添加城市", "000400010001", "weather.city.add", menu41.getId());
        menu411 = sysMenuRepository.insert(menu411);
        SysMenu menu412 = DocumentFactory.data("删除城市", "000400010002", "weather.city.delete", menu41.getId());
        menu412 = sysMenuRepository.insert(menu412);
        SysMenu menu413 = DocumentFactory.data("修改城市", "000400010003", "weather.city.edit", menu41.getId());
        menu413 = sysMenuRepository.insert(menu413);
        
        // 消息系统
        SysMenu menu5 = DocumentFactory.menu("消息系统", "0005", "message", "");
        menu5.setLocation(4);
        menu5 = sysMenuRepository.insert(menu5);
        // - 城市管理
        SysMenu menu51 = DocumentFactory.menu("邮件模版管理", "00050001",
                "message.mail", "/admin/message/mail/index.html");
        menu51.setParent(menu5.getId());
        menu51 = sysMenuRepository.insert(menu51);
        SysMenu menu511 = DocumentFactory.data("添加邮件模版", "000500010001", "message.mail.add", menu51.getId());
        menu511 = sysMenuRepository.insert(menu511);
        SysMenu menu512 = DocumentFactory.data("删除邮件模版", "000500010002", "message.mail.delete", menu51.getId());
        menu512 = sysMenuRepository.insert(menu512);
        SysMenu menu513 = DocumentFactory.data("修改邮件模版", "000500010003", "message.mail.edit", menu51.getId());
        menu513 = sysMenuRepository.insert(menu513);
        
        SysMenu[] role_admin = new SysMenu[]{
                menu1,
                menu11, menu111, menu112, menu113,
                menu12, menu121, menu122, menu123,
                menu13, menu131, menu132, menu133,
                menu14, menu141, menu142, menu143,
                menu15, menu151, menu152, menu153,
                menu16, menu161, menu162, menu163,
                menu17, menu171, menu172, menu173,
                
                menu2,
                menu26, menu261,
                
                menu3,
                menu31,
        
                menu4,
                menu41, menu411, menu412, menu413,
        
                menu5,
                menu51, menu511, menu512, menu513
        };
        
        SysMenu[] role_admin_old = new SysMenu[]{
                menu1,
                menu11, menu12, menu13, menu14, menu15, menu16, menu17,
                menu2,
                menu26,
                menu3,
                menu31,
                menu4,
                menu41,
                menu5,
                menu51
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
        r1.setDescription("内置角色, 不允许分配用户");
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
        
        /**
         * 添加系统变量
         */
        Variable variable = DocumentFactory.variable(Variable.FILE_UPLOAD_DIR, "/Users/hocgin/Desktop/FileUpload", "文件上传目录");
        variableRepository.insert(variable);
        Variable variable2 = DocumentFactory.variable(Variable.HOST, "http://127.0.0.1:8080", "域名");
        variableRepository.insert(variable2);
    }
}
