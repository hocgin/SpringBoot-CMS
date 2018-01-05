package in.hocg.web.database;

import in.hocg.web.modules.system.domain.*;
import in.hocg.web.modules.system.domain.repository.*;
import in.hocg.web.modules.system.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by hocgin on 2017/11/14.
 * email: hocgin@gmail.com
 */
@Component
public class BuiltInSeeder {
    
    @Value("${dev.variable.host}")
    private String HOST;
    @Value("${dev.variable.file.upload.dir}")
    private String FILE_UPLOAD_DIR;
    @Value("${dev.variable.images.dir}")
    private String IMAGES_DIR;
    
    private RoleRepository roleRepository;
    private DepartmentRepository departmentRepository;
    private UserRepository userRepository;
    private SysMenuRepository sysMenuRepository;
    private VariableRepository variableRepository;
    private SysTaskRepository sysTaskRepository;
    private CommentRepository commentRepository;
    private ArticlesRepository articlesRepository;
    private UserNotifyRepository userNotifyRepository;
    private NotifyRepository notifyRepository;
    private SubscriptionRepository subscriptionRepository;
    private ChannelRepository channelRepository;
    
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public BuiltInSeeder(
            VariableRepository variableRepository,
            SysTaskRepository sysTaskRepository,
            UserNotifyRepository userNotifyRepository,
            RoleRepository roleRepository,
            DepartmentRepository departmentRepository,
            UserRepository userRepository,
            NotifyRepository notifyRepository,
            SubscriptionRepository subscriptionRepository,
            CommentRepository commentRepository,
            SysMenuRepository sysMenuRepository,
            ArticlesRepository articlesRepository,
            ChannelRepository channelRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.sysMenuRepository = sysMenuRepository;
        this.variableRepository = variableRepository;
        this.sysTaskRepository = sysTaskRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.commentRepository = commentRepository;
        this.articlesRepository = articlesRepository;
        this.userNotifyRepository = userNotifyRepository;
        this.notifyRepository = notifyRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.channelRepository = channelRepository;
    }
    
    public BuiltInSeeder drop() {
        // 角色
        roleRepository.deleteAll();
        // 单位
        departmentRepository.deleteAll();
        // 用户
        userRepository.deleteAll();
        // 系统菜单
        sysMenuRepository.deleteAll();
        // 变量
        variableRepository.deleteAll();
        // 定时任务
        sysTaskRepository.deleteAll();
        // 评论
        commentRepository.deleteAll();
        // 文章
        articlesRepository.deleteAll();
    
        userNotifyRepository.deleteAll();
        subscriptionRepository.deleteAll();
        notifyRepository.deleteAll();
    
        channelRepository.deleteAll();
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
        // 系统管理 - 管理员列表
        SysMenu menu15 = DocumentFactory.menu("管理员列表", "00010005",
                "sys.manager", "/admin/system/manager/index.html");
        menu15.setParent(menu1.getId());
        menu15 = sysMenuRepository.insert(menu15);
        SysMenu menu151 = DocumentFactory.data("添加管理员", "000100050001", "sys.manager.add", menu15.getId());
        menu151 = sysMenuRepository.insert(menu151);
        SysMenu menu152 = DocumentFactory.data("删除管理员", "000100050002", "sys.manager.delete", menu15.getId());
        menu152 = sysMenuRepository.insert(menu152);
        SysMenu menu153 = DocumentFactory.data("修改管理员", "000100050003", "sys.manager.edit", menu15.getId());
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
        // 系统管理 - 定时任务
        SysMenu menu18 = DocumentFactory.menu("定时任务", "00010008",
                "sys.task", "/admin/system/task/index.html");
        menu18.setParent(menu1.getId());
        menu18 = sysMenuRepository.insert(menu18);
        SysMenu menu181 = DocumentFactory.data("添加任务", "000100080001", "sys.task.add", menu18.getId());
        menu181 = sysMenuRepository.insert(menu181);
        SysMenu menu182 = DocumentFactory.data("删除任务", "000100080002", "sys.task.delete", menu18.getId());
        menu182 = sysMenuRepository.insert(menu182);
        SysMenu menu183 = DocumentFactory.data("修改任务", "000100080003", "sys.task.edit", menu18.getId());
        menu183 = sysMenuRepository.insert(menu183);
        // 系统管理 - 短链管理
        SysMenu menu19 = DocumentFactory.menu("短链管理", "00010009",
                "sys.short-url", "/admin/system/short-url/index.html");
        menu19.setParent(menu1.getId());
        menu19 = sysMenuRepository.insert(menu19);
        SysMenu menu191 = DocumentFactory.data("添加短链", "000100090001", "sys.short-url.add", menu19.getId());
        menu191 = sysMenuRepository.insert(menu191);
        SysMenu menu192 = DocumentFactory.data("删除短链", "000100090002", "sys.short-url.delete", menu19.getId());
        menu192 = sysMenuRepository.insert(menu192);
        SysMenu menu193 = DocumentFactory.data("修改短链", "000100090003", "sys.short-url.edit", menu19.getId());
        menu193 = sysMenuRepository.insert(menu193);
        
        
        // 系统安全
        String path = "0002";
        SysMenu menu2 = DocumentFactory.menu("系统安全", path, "safety", "");
        menu2.setLocation(2);
        menu2 = sysMenuRepository.insert(menu2);
        // - 日志管理
        SysMenu menu21 = DocumentFactory.menu("系统日志", "00020001",
                "safety.log", "/admin/system/log/index.html");
        menu21.setParent(menu2.getId());
        menu21 = sysMenuRepository.insert(menu21);
        SysMenu menu211 = DocumentFactory.data("清空日志", "000200010001", "safety.log.empty", menu21.getId());
        menu211 = sysMenuRepository.insert(menu211);
        
        
        // 仪表盘
        SysMenu menu3 = DocumentFactory.menu("仪表盘", "0003", "dashboard", "");
        menu3.setLocation(0);
        menu3 = sysMenuRepository.insert(menu3);
        // - 主界面
        SysMenu menu31 = DocumentFactory.menu("主界面", "00030001",
                "dashboard.index", "/admin/dashboard/index.html");
        menu31.setParent(menu3.getId());
        menu31.setHasChildren(false);
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
        // - 邮件模版管理
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
        SysMenu menu514 = DocumentFactory.data("发送邮件", "000500010003", "message.mail.send", menu51.getId());
        menu514 = sysMenuRepository.insert(menu514);
        // - 公告管理
        SysMenu menu52 = DocumentFactory.menu("公告管理", "00050002",
                "message.announce", "/admin/message/announce/index.html");
        menu52.setParent(menu5.getId());
        menu52 = sysMenuRepository.insert(menu52);
        SysMenu menu521 = DocumentFactory.data("添加公告", "000500020001", "message.announce.add", menu52.getId());
        menu521 = sysMenuRepository.insert(menu521);
        SysMenu menu522 = DocumentFactory.data("删除公告", "000500020002", "message.announce.delete", menu52.getId());
        menu522 = sysMenuRepository.insert(menu522);
        
        
        // 内容管理
        SysMenu menu6 = DocumentFactory.menu("内容管理", "0006", "content", "");
        menu6.setLocation(5);
        menu6 = sysMenuRepository.insert(menu6);
        // - 栏目管理
        SysMenu menu61 = DocumentFactory.menu("栏目管理", "00060001",
                "content.channel", "/admin/content/channel/index.html");
        menu61.setParent(menu6.getId());
        menu61 = sysMenuRepository.insert(menu61);
        SysMenu menu611 = DocumentFactory.data("添加栏目", "000600010001", "content.channel.add", menu61.getId());
        menu611 = sysMenuRepository.insert(menu611);
        SysMenu menu612 = DocumentFactory.data("删除栏目", "000600010002", "content.channel.delete", menu61.getId());
        menu612 = sysMenuRepository.insert(menu612);
        SysMenu menu613 = DocumentFactory.data("修改栏目", "000600010003", "content.channel.edit", menu61.getId());
        menu613 = sysMenuRepository.insert(menu613);
        // - 文章管理
        SysMenu menu62 = DocumentFactory.menu("文章管理", "00060002",
                "content.articles", "/admin/content/articles/index.html");
        menu62.setParent(menu6.getId());
        menu62 = sysMenuRepository.insert(menu62);
        SysMenu menu621 = DocumentFactory.data("添加文章", "000600020001", "content.articles.add", menu62.getId());
        menu621 = sysMenuRepository.insert(menu621);
        SysMenu menu622 = DocumentFactory.data("删除文章", "000600020002", "content.articles.delete", menu62.getId());
        menu622 = sysMenuRepository.insert(menu622);
        SysMenu menu623 = DocumentFactory.data("修改文章", "000600020003", "content.articles.edit", menu62.getId());
        menu623 = sysMenuRepository.insert(menu623);
        // - 评论管理
        SysMenu menu63 = DocumentFactory.menu("评论管理", "00060003",
                "content.comment", "/admin/content/comment/index.html");
        menu63.setParent(menu6.getId());
        menu63 = sysMenuRepository.insert(menu63);
        SysMenu menu631 = DocumentFactory.data("修改评论", "000600030001", "content.comment.edit", menu63.getId());
        menu631 = sysMenuRepository.insert(menu631);


//        SysMenu menu81 = DocumentFactory.data("添加文章", "0008", "ROLE_ACTUATOR", null);
//        menu811 = sysMenuRepository.insert(menu811);
        
        SysMenu[] role_admin = new SysMenu[]{
                menu1,
                menu11, menu111, menu112, menu113,
                menu12, menu121, menu122, menu123,
                menu13, menu131, menu132, menu133,
                menu14, menu141, menu142, menu143,
                menu15, menu151, menu152, menu153,
                menu16, menu161, menu162, menu163,
                menu17, menu171, menu172, menu173,
                menu18, menu181, menu182, menu183,
                menu19, menu191, menu192, menu193,
                
                menu2,
                menu21, menu211,
                
                menu3,
                menu31,
                
                menu4,
                menu41, menu411, menu412, menu413,
                
                menu5,
                menu51, menu511, menu512, menu513, menu514,
                menu52, menu521, menu522,
                
                
                menu6,
                // - 栏目管理
                menu61, menu611, menu612, menu613,
                menu62, menu621, menu622, menu623,
                menu63, menu631
        };
        
        SysMenu[] role_admin_old = new SysMenu[]{
                menu1, menu11, menu12, menu13, menu14, menu15, menu16, menu17, menu18, menu19,
                menu2, menu21,
                menu3, menu31,
                menu4, menu41,
                menu5, menu51, menu52,
                menu6, menu61, menu62, menu63
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
        Role r4 = DocumentFactory.role(Role.ROLE_ACTUATOR, "监控人员", d11);
        r4 = roleRepository.insert(r4);
        
        /**
         * 初始化用户
         * 内置单位
         *   - 后台管理
         *      - 管理员
         *          - admin
         *   - 前台管理
         *      - 普通用户
         */
        User user = DocumentFactory.manager("admin",
                Arrays.asList(r2, r4),
                "hocgin@gmail.com",
                bCryptPasswordEncoder.encode("admin"));
        user.setNickname("admin");
        user = userRepository.insert(user);
        
        User user2 = DocumentFactory.manager("adm1n",
                Collections.singleton(r3),
                "adm1n@gmail.com",
                bCryptPasswordEncoder.encode("adm1n"));
        user2.setNickname("adm1n");
        user2.setBuiltIn(false);
        user2 = userRepository.insert(user2);
        
        /**
         * 添加系统变量
         */
        Variable variable = DocumentFactory.variable(Variable.FILE_UPLOAD_DIR, FILE_UPLOAD_DIR, "文件上传目录");
        variableRepository.insert(variable);
        Variable variable2 = DocumentFactory.variable(Variable.HOST, HOST, "域名");
        variableRepository.insert(variable2);
        Variable variable3 = DocumentFactory.variable(Variable.IMAGES_DIR, IMAGES_DIR, "公共图片目录");
        variableRepository.insert(variable3);
        
        
        /**
         * 定时任务
         */
        SysTask task = DocumentFactory.sysTask("会员Token每月自动重置",
                "0 0 0 1 * ? *",
                "in.hocg.web.job.ResumeMemberTokenJob", new HashMap<>());
        sysTaskRepository.insert(task);
        
        /**
         * 会员
         */
        User hocgin = DocumentFactory.member("hocgin",
                Collections.singleton(r1),
                "578797748@qq.com",
                bCryptPasswordEncoder.encode("123"));
        hocgin.setUsername("hocgin");
        userRepository.insert(hocgin);
    
        /**
         * 栏目
         * zixun
         */
        Channel channel = DocumentFactory.channel("资讯", "ZiXun", "001");
        channelRepository.insert(channel);
    
    
        Articles article = DocumentFactory.article(channel, "测试", "测试内容", "测试简介");
        articlesRepository.insert(article);
    
        Variable variable4 = DocumentFactory.variable(Variable.DEV_INIT_MONGO, Boolean.FALSE.toString(), "初始化 Mongo 数据, true 为开启。");
        variable4.setBuiltIn(false);
        variableRepository.insert(variable4);
        
    }
}
