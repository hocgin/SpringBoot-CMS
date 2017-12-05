package in.hocg.web.modules.system.service.impl;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.Department;
import in.hocg.web.modules.system.domain.Role;
import in.hocg.web.modules.system.domain.SysMenu;
import in.hocg.web.modules.system.domain.repository.RoleRepository;
import in.hocg.web.modules.system.filter.RoleDataTablesInputFilter;
import in.hocg.web.modules.system.filter.RoleFilter;
import in.hocg.web.modules.system.service.DepartmentService;
import in.hocg.web.modules.system.service.RoleService;
import in.hocg.web.modules.system.service.SysMenuService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;
    private SysMenuService permissionService;
    private DepartmentService departmentService;
    
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository,
                           SysMenuService permissionService,
                           DepartmentService departmentService) {
        this.roleRepository = roleRepository;
        this.departmentService = departmentService;
        this.permissionService = permissionService;
    }
    
    @Override
    public DataTablesOutput<Role> data(RoleDataTablesInputFilter input) {
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(input.getDepartment())) {
            criteria.andOperator(Criteria.where("department.$id").is(new ObjectId(input.getDepartment())));
        }
        DataTablesOutput<Role> all = roleRepository.findAll(input, criteria);
        all.setDraw(0);
        return all;
    }
    
    @Override
    public void insert(RoleFilter filter, CheckError checkError) {
        Role role = filter.get();
        
        // 检测单位是否存在
        Department department = departmentService.findById(filter.getDepartmentId());
        if (ObjectUtils.isEmpty(department)) {
            checkError.putError("所选择单位不存在");
            return;
        }
        role.setDepartment(department);
    
        // 检测角色标识是否已经存在
        if (!CollectionUtils.isEmpty(roleRepository.findAllByRole(role.getRole()))) {
            checkError.putError("该角色标识已存在");
            return;
        }
        
        // 过滤非法权限
        if (!ObjectUtils.isEmpty(filter.getPermissionIds())
                && filter.getPermissionIds().length > 0) {
            List<SysMenu> permissions = permissionService.queryAllByIdOrderByPathAsc(filter.getPermissionIds());
            role.setPermissions(permissions);
        }
        
        roleRepository.insert(role);
    }
    
    @Override
    public void delete(CheckError checkError, String... id) {
        for (Role role : roleRepository.findAllByIdIn(id)) {
            if (role.getBuiltIn()) {
                checkError.putError("删除失败, 含有内置对象");
                return;
            }
        }
        roleRepository.deleteAllByIdIn(id);
    }
    
    @Override
    public void updateAvailable(String id, boolean b) {
        Role role = roleRepository.findOne(id);
        if (!ObjectUtils.isEmpty(role)) {
            role.setAvailable(b);
            role.updatedAt();
            roleRepository.save(role);
        }
    }
    
    @Override
    public Role find(String id) {
        return roleRepository.findOne(id);
    }
    
    @Override
    public Role findByRole(String role) {
        return roleRepository.findTopByRole(role);
    }
    
    @Override
    public void updateDescription(RoleFilter filter,
                                  CheckError checkError) {
        Role role = roleRepository.findOne(filter.getId());
        if (ObjectUtils.isEmpty(role)) {
            checkError.putError("角色异常");
            return;
        }
        roleRepository.save(filter.update1(role));
    }
    
    @Override
    public void updatePermission(RoleFilter filter,
                                 CheckError checkError) {
        Role role = roleRepository.findOne(filter.getId());
        if (ObjectUtils.isEmpty(role)) {
            checkError.putError("角色异常");
            return;
        }
        List<SysMenu> permissions = permissionService.queryAllByIdOrderByPathAsc(filter.getPermissionIds());
        role.setPermissions(permissions);
        role.updatedAt();
        roleRepository.save(role);
    }
    
    @Override
    public void deleteAllByDepartmentIn(String... departmentId) {
        roleRepository.deleteAllByDepartmentIn(departmentId);
    }
    
    @Override
    public void removePermissionForAllRole(String... id) {
        roleRepository.removePermissionForAllRole(id);
    }
    
    @Override
    public List<Role> findByDepartmentAndChildren(String departmentId) {
        
        List<Department> departments = departmentService.findByDepartmentAndChildren(departmentId);
        if (departments.isEmpty()) {
            return Collections.emptyList();
        }
        String[] departmentIds = departments.stream()
                .map(Department::getId)
                .toArray(String[]::new);
        return roleRepository.findAllByDepartmentIn(departmentIds);
    }
    
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
    
}
