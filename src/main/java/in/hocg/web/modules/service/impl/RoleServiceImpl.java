package in.hocg.web.modules.service.impl;

import in.hocg.web.filter.RoleQueryFilter;
import in.hocg.web.filter.RoleUpdateInfoFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.Department;
import in.hocg.web.modules.domain.Permission;
import in.hocg.web.modules.domain.Role;
import in.hocg.web.modules.domain.repository.RoleRepository;
import in.hocg.web.modules.service.DepartmentService;
import in.hocg.web.modules.service.PermissionService;
import in.hocg.web.modules.service.RoleService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

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
    
    @Override
    public DataTablesOutput<Role> data(RoleQueryFilter input) {
        Criteria criteria = null;
        if (!StringUtils.isEmpty(input.getDepartment())) {
            criteria = Criteria.where("department.$id").is(new ObjectId(input.getDepartment()));
        }
        DataTablesOutput<Role> all = roleRepository.findAll(input, criteria);
        all.setDraw(0);
        return all;
    }
    
    @Override
    public void insert(Role role, String[] permissionIds, CheckError checkError) {
        if (permissionIds != null
                && permissionIds.length > 0) {
            List<Permission> permissions = permissionService.queryAllByIdOrderByPathAes(permissionIds);
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
    
    @Override
    public void save(RoleUpdateInfoFilter updateInfoFilter,
                     CheckError checkError) {
    
        if (StringUtils.isEmpty(updateInfoFilter.getName())
                || StringUtils.isEmpty(updateInfoFilter.getId())
                || StringUtils.isEmpty(updateInfoFilter.getRole())) {
            checkError.putError("角色标识符/名称不能为空");
            return;
        }
        List<Role> all = roleRepository.findAllByRole(updateInfoFilter.getRole());
        if (all.size() >= 2
                || (all.size() == 1 && !Objects.equals(all.get(0).getId(), updateInfoFilter.getId()))) {
            checkError.putError("角色标识符已经存在");
            return;
        }
        
        Role role = roleRepository.findOne(updateInfoFilter.getId());
        if (ObjectUtils.isEmpty(role)) {
            checkError.putError("角色不存在");
            return;
        }
        Department department = departmentService.findById(updateInfoFilter.getDepartmentId());
        if (ObjectUtils.isEmpty(department)) {
            checkError.putError("所属单位异常");
            return;
        }
        role.setName(updateInfoFilter.getName());
        role.setRole(updateInfoFilter.getRole());
        role.setDescription(updateInfoFilter.getDescription());
        role.setDepartment(department);
        roleRepository.save(role);
    }
    
    @Override
    public void save(String id,
                     String[] permissionIds,
                     CheckError checkError) {
        if (StringUtils.isEmpty(id)) {
            checkError.putError("ID 异常");
            return;
        }
        Role role = roleRepository.findOne(id);
        if (ObjectUtils.isEmpty(role)) {
            checkError.putError("角色不存在");
            return;
        }
        List<Permission> permissions = permissionService.queryAllByIdOrderByPathAes(permissionIds);
        role.setPermissions(permissions);
        roleRepository.save(role);
    }
    
}
