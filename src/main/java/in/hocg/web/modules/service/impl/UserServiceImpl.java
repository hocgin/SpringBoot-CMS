package in.hocg.web.modules.service.impl;

import in.hocg.web.filter.UserDataTablesInputFilter;
import in.hocg.web.filter.UserFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.body.response.LeftMenu;
import in.hocg.web.lang.utils.DocumentKit;
import in.hocg.web.lang.utils.RequestKit;
import in.hocg.web.modules.domain.Department;
import in.hocg.web.modules.domain.Menu;
import in.hocg.web.modules.domain.Role;
import in.hocg.web.modules.domain.User;
import in.hocg.web.modules.domain.repository.UserRepository;
import in.hocg.web.modules.service.DepartmentService;
import in.hocg.web.modules.service.RoleService;
import in.hocg.web.modules.service.UserService;
import org.bson.types.ObjectId;
import org.codehaus.groovy.util.StringUtil;
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

/**
 * Created by hocgin on 2017/10/25.
 * email: hocgin@gmail.com
 */
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private DepartmentService departmentService;
    private RoleService roleService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private HttpServletRequest request;
    
    @Autowired
    UserServiceImpl(UserRepository userRepository,
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
    public Collection<Menu> findMenuByUser(String id) {
        Collection<Role> roles = findRoleByUser(id);
        // 一般角色处理
        HashSet<Menu> menus = new HashSet<>();
        roles.forEach(role -> {
            Collection<Menu> menuCollection = role.getPermissions();
            if (!CollectionUtils.isEmpty(menuCollection)) {
                menus.addAll(menuCollection);
            }
        });
        return menus;
    }
    
    /**
     * 获取左侧用户权限内的菜单
     * @param id
     * @return
     */
    @Override
    public LeftMenu getLeftMenu(String id) {
        Collection<Menu> menus = findMenuByUser(id);
        List<Menu> root = new ArrayList<>();
        Map<String, List<Menu>> childMenus = new HashMap<>();
        for (Menu menu : menus) {
            if (!menu.getAvailable()) { // 如果被关闭的话, 跳过
                continue;
            }
            if (menu.getPath().length() > 4) {
                List<Menu> s = childMenus.get(DocumentKit.getParentId(menu.getPath()));
                if (CollectionUtils.isEmpty(s)) {
                    s = new ArrayList<>();
                }
                s.add(menu);
                childMenus.put(DocumentKit.getParentId(menu.getPath()), s);
            } else if (menu.getPath().length() == 4) {
                root.add(menu);
            }
        }
        LeftMenu leftMenu = new LeftMenu();
        leftMenu.setRoot(root);
        leftMenu.setChildren(childMenus);
        return leftMenu;
    }
    
    @Override
    public DataTablesOutput<User> data(UserDataTablesInputFilter input) {
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(input.getDepartment())) {
            criteria.andOperator(Criteria.where("department.$id").is(new ObjectId(input.getDepartment())));
        }
        if (!StringUtils.isEmpty(input.getRole())) {
            criteria.andOperator(Criteria.where("role.$id").is(new ObjectId(input.getRole())));
        }
        if (!StringUtils.isEmpty(input.getNotRole())) {
            criteria.andOperator(Criteria.where("role.$id").ne(new ObjectId(input.getNotRole())));
        }
        if (!StringUtils.isEmpty(input.getRegexNicknameOrUsername())) {
            criteria.orOperator(
                    Criteria.where("username").regex(String.format("%s.*", input.getRegexNicknameOrUsername())),
                    Criteria.where("nickname").regex(String.format("%s.*", input.getRegexNicknameOrUsername()))
            );
        }
        DataTablesOutput<User> all = userRepository.findAll(input, criteria);
        all.setDraw(0);
        return all;
    }
    
    @Override
    public void delete(CheckError checkError, String... id) {
        for (User user : userRepository.findAllByIdIn(id)) {
            if (user.getBuiltIn()) {
                checkError.putError("删除失败, 含有内置对象");
                return;
            }
        }
        userRepository.deleteAllByIdIn(id);
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
        if (!ObjectUtils.isEmpty(userRepository.findByUsername(filter.getUsername()))) {
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
        return userRepository.findAllByIdIn(ids);
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
            userRepository.findAllByIdIn(userIds)
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
            userRepository.findAllByIdIn(userIds)
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
