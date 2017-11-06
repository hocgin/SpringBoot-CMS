package in.hocg.web.modules.service.impl;

import in.hocg.web.filter.UserInsertFilter;
import in.hocg.web.filter.UserQueryFilter;
import in.hocg.web.filter.UserUpdateFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.Department;
import in.hocg.web.modules.domain.Role;
import in.hocg.web.modules.domain.User;
import in.hocg.web.modules.domain.repository.UserRepository;
import in.hocg.web.modules.service.DepartmentService;
import in.hocg.web.modules.service.RoleService;
import in.hocg.web.modules.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    
    @Autowired
    UserServiceImpl(UserRepository userRepository,
                    RoleService roleService,
                    BCryptPasswordEncoder bCryptPasswordEncoder,
                    DepartmentService departmentService) {
        this.userRepository = userRepository;
        this.departmentService = departmentService;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    @Override
    public DataTablesOutput<User> data(UserQueryFilter input) {
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
    public void delete(String... id) {
        userRepository.deleteAllByIdIn(id);
    }
    
    @Override
    public void insert(UserInsertFilter filter, CheckError checkError) {
        String id = filter.getDepartmentId();
        Department department = departmentService.findById(id);
        if (ObjectUtils.isEmpty(department)) {
            checkError.putError("所属单位异常");
            return;
        }
        User u = userRepository.findByUsername(filter.getUsername());
        if (!ObjectUtils.isEmpty(u)) {
            checkError.putError("用户名已存在");
            return;
        }
        User user = filter.getInsertUser();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setDepartment(department);
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
    public void update(UserUpdateFilter filter, CheckError checkError) {
        String id = filter.getId();
        User user = userRepository.findOne(id);
        if (ObjectUtils.isEmpty(user)) {
            checkError.putError("用户异常");
            return;
        }
        Department department = departmentService.findById(filter.getDepartmentId());
        if (ObjectUtils.isEmpty(department)) {
            checkError.putError("所属单位异常");
            return;
        }
        user.setEmail(filter.getEmail());
        user.setNickname(filter.getNickname());
        user.setAvailable(filter.isAvailable());
        user.setDepartment(department);
        userRepository.save(user);
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
}
