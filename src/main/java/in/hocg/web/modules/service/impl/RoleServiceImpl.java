package in.hocg.web.modules.service.impl;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.Department;
import in.hocg.web.modules.domain.Permission;
import in.hocg.web.modules.domain.Role;
import in.hocg.web.modules.domain.repository.RoleRepository;
import in.hocg.web.modules.service.DepartmentService;
import in.hocg.web.modules.service.PermissionService;
import in.hocg.web.modules.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by hocgin on 2017/10/29.
 * email: hocgin@gmail.com
 */
@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;
    private PermissionService permissionService;
    private DepartmentService departmentService;
    
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository,
                           PermissionService permissionService,
                           DepartmentService departmentService) {
        this.roleRepository = roleRepository;
        this.departmentService = departmentService;
        this.permissionService = permissionService;
    }
    
    public DataTablesOutput<Role> data(DataTablesInput input) {
        return roleRepository.findAll(input);
    }
    
    @Override
    public void insert(Role role, String[] permissionIds, CheckError checkError) {
        if (permissionIds != null && permissionIds.length > 0) {
            List<Permission> permissions = permissionService.queryById(permissionIds);
            role.setPermissions(permissions);
        }
        if (StringUtils.isEmpty(role.getDepartment().getId())) {
            checkError.putError("单位不能为空");
            return;
        }
        Department department = departmentService.findById(role.getDepartment().getId());
        if (department == null) {
            checkError.putError("所选择单位不存在");
            return;
        }
        
        
        
        
        
        role.setDepartment(department);
        roleRepository.insert(role);
    }
    
    @Override
    public void delete(String... id) {
        roleRepository.deleteAllByIdIn(id);
    }
    
    @Override
    public void updateAvailable(String id, boolean b) {
        Role role = roleRepository.findOne(id);
        if (role != null) {
            role.setAvailable(b);
            roleRepository.save(role);
        }
    }
    
    @Override
    public Role find(String id) {
        return roleRepository.findOne(id);
    }
    
}
