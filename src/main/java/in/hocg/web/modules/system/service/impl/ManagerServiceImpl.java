package in.hocg.web.modules.system.service.impl;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.RequestKit;
import in.hocg.web.lang.utils.tree.Node;
import in.hocg.web.lang.utils.tree.TreeKit;
import in.hocg.web.modules.system.domain.Department;
import in.hocg.web.modules.system.domain.Role;
import in.hocg.web.modules.system.domain.SysMenu;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.domain.repository.UserRepository;
import in.hocg.web.modules.system.filter.UserDataTablesInputFilter;
import in.hocg.web.modules.system.filter.UserFilter;
import in.hocg.web.modules.system.service.DepartmentService;
import in.hocg.web.modules.system.service.RoleService;
import in.hocg.web.modules.system.service.ManagerService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 * 管理员
 */
@Service
public class ManagerServiceImpl implements ManagerService {
    private UserRepository userRepository;
    private DepartmentService departmentService;
    private RoleService roleService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private HttpServletRequest request;
    
    @Autowired
    ManagerServiceImpl(UserRepository userRepository,
                       RoleService roleService,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       DepartmentService departmentService,
                       HttpServletRequest request) {
        this.userRepository = userRepository;
        this.departmentService = departmentService;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.request = request;
    }
    
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    @Override
    public Collection<Role> findRoleByUser(String id) {
        User user = userRepository.findOne(id);
        if (ObjectUtils.isEmpty(user)) {
            return Collections.emptyList();
        }
        return user.getRole();
    }
    
    @Override
    public Collection<SysMenu> findSysMenuByUser(String id) {
        Collection<Role> roles = findRoleByUser(id);
        // 一般角色处理
        HashSet<SysMenu> menus = new HashSet<>();
        roles.forEach(role -> {
            Optional.ofNullable(role.getPermissions()).ifPresent(permissions ->
                    permissions.stream()
                    // 过滤掉未开启菜单
                    .filter(SysMenu::getAvailable)
                    // 过滤掉按钮菜单
                    .filter(sysMenu -> sysMenu.getType() != 1)
                    .forEach(menus::add));
        });
        return menus.stream()
                .sorted(Comparator.comparing(SysMenu::getLocation)
                        .thenComparing(SysMenu::getPath))
                .collect(Collectors.toList());
    }
    
    /**
     * 获取左侧用户权限内的菜单
     *
     * @param id
     * @return
     */
    @Override
    public List<Node<SysMenu>> getLeftMenu(String id) {
        
        Collection<SysMenu> allNodes = findSysMenuByUser(id);
        // 最后的结果
        List<Node<SysMenu>> rootNodes = new ArrayList<>();
        for (SysMenu menu : allNodes) {
            if (StringUtils.isEmpty(menu.getParent())) { // 根结点
                Node<SysMenu> node = new Node<>();
                node.setNode(menu);
                rootNodes.add(node);
            }
        }
        // 查找子节点
        for (Node<SysMenu> node : rootNodes) {
            node.setChildren(TreeKit.getChild(node.getNode().getId(), allNodes));
        }
        return rootNodes;
    }
    
    @Override
    public List<User> findAllByRoles(String... rolesId) {
        return userRepository.findAllByRole(rolesId);
    }
    
    @Override
    public DataTablesOutput<User> data(UserDataTablesInputFilter input) {
        Criteria criteria = new Criteria();
        List<Criteria> andOperator = new ArrayList<>();
        List<Criteria> orOperator = new ArrayList<>();
        if (!StringUtils.isEmpty(input.getDepartment())) {
            andOperator.add(Criteria.where("department.$id").is(new ObjectId(input.getDepartment())));
        }
        if (!StringUtils.isEmpty(input.getRole())) {
            andOperator.add(Criteria.where("role.$id").is(new ObjectId(input.getRole())));
        }
        if (!StringUtils.isEmpty(input.getNotRole())) {
            andOperator.add(Criteria.where("role.$id").ne(new ObjectId(input.getNotRole())));
        }
        if (!StringUtils.isEmpty(input.getRegexNicknameOrUsername())) {
            orOperator.add(Criteria.where("username").regex(String.format("%s.*", input.getRegexNicknameOrUsername())));
            orOperator.add(Criteria.where("nickname").regex(String.format("%s.*", input.getRegexNicknameOrUsername())));
        }
        if (!ObjectUtils.isEmpty(input.getIds())) {
            andOperator.add(Criteria.where("_id").in(input.getIds()));
        }
        if (!ObjectUtils.isEmpty(input.getNoIds())) {
            andOperator.add(Criteria.where("_id").nin(input.getNoIds()));
        }
        andOperator.add(Criteria.where("type").is(User.Type.Manager.getCode()));
    
    
        
        
        if (!andOperator.isEmpty()) {
            criteria.andOperator(andOperator.toArray(new Criteria[]{}));
        }
        if (!orOperator.isEmpty()) {
            criteria.orOperator(orOperator.toArray(new Criteria[]{}));
        }
        DataTablesOutput<User> all = userRepository.findAll(input, criteria);
        all.setDraw(0);
        return all;
    }
    
    @Override
    public void delete(CheckError checkError, String... id) {
        for (User user : userRepository.findAllByIdInAndTypeIs(id, User.Type.Manager.getCode())) {
            if (user.getBuiltIn()) {
                checkError.putError("删除失败, 含有内置对象");
                return;
            }
        }
        userRepository.deleteAllByIdInAndTypeIs(id, User.Type.Manager.getCode());
    }
    
    @Override
    public void insert(UserFilter filter, CheckError checkError) {
        User user = filter.get();
        
        // 检测单位是否存在
        Department department = departmentService.findById(filter.getDepartmentId());
        if (ObjectUtils.isEmpty(department)) {
            checkError.putError("所属单位异常");
            return;
        }
        user.setDepartment(department);
        
        // 检测用户名是否已被使用
        if (!ObjectUtils.isEmpty(userRepository.findByUsernameAndTypeIs(filter.getUsername(), User.Type.Manager.getCode()))) {
            checkError.putError("用户名已被使用, 请更换");
            return;
        }
        user.setUsername(filter.getUsername());
        user.setSignUpIP(RequestKit.getClientIP(request));
        // 密码加密
        user.setPassword(bCryptPasswordEncoder.encode(filter.getPassword()));
        userRepository.save(user);
    }
    
    @Override
    public void updateAvailable(String id, boolean b) {
        User user = userRepository.findOne(id);
        if (!ObjectUtils.isEmpty(user)) {
            user.setAvailable(b);
            userRepository.save(user);
        }
    }
    
    @Override
    public User find(String id) {
        return userRepository.findOne(id);
    }
    
    @Override
    public List<User> findAllById(String... ids) {
        return userRepository.findAllByIdInAndTypeIs(ids, User.Type.Manager.getCode());
    }
    
    @Override
    public void update(UserFilter filter, CheckError checkError) {
        
        User user = userRepository.findOne(filter.getId());
        if (ObjectUtils.isEmpty(user)) {
            checkError.putError("用户异常");
            return;
        }
        
        // 检测所属单位是否存在
        Department department = departmentService.findById(filter.getDepartmentId());
        if (ObjectUtils.isEmpty(department)) {
            checkError.putError("所属单位异常");
            return;
        }
        user.setDepartment(department);
        
        userRepository.save(filter.update(user));
    }
    
    @Override
    public void update(User user) {
        userRepository.save(user);
    }
    
    @Override
    public void addRoleToUser(String roleId, String... userIds) {
        Role role = roleService.find(roleId);
        if (!ObjectUtils.isEmpty(role)) {
            userRepository.findAllByIdInAndTypeIs(userIds, User.Type.Manager.getCode())
                    .forEach(user -> {
                        Collection<Role> roles = user.getRole();
                        if (CollectionUtils.isEmpty(roles)) {
                            roles = new ArrayList<>();
                        }
                        if (!roles.contains(role)) {
                            roles.add(role);
                            user.setRole(roles);
                            userRepository.save(user);
                        }
                    });
        }
    }
    
    @Override
    public void removeRoleFormUser(String roleId, String... userIds) {
        Role role = roleService.find(roleId);
        if (!ObjectUtils.isEmpty(role)) {
            userRepository.findAllByIdInAndTypeIs(userIds, User.Type.Manager.getCode())
                    .forEach(user -> {
                        Collection<Role> roles = user.getRole();
                        if (CollectionUtils.isEmpty(roles)) {
                            roles = new ArrayList<>();
                        }
                        if (roles.contains(role)) {
                            roles.remove(role);
                            user.setRole(roles);
                            userRepository.save(user);
                        }
                    });
        }
    }
    
    @Override
    public void removeDepartmentField(String... DepartmentId) {
        userRepository.removeDepartmentField(DepartmentId);
    }
}
