package in.hocg.web.modules.service.impl;

import in.hocg.web.filter.DepartmentFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.Department;
import in.hocg.web.modules.domain.repository.DepartmentRepository;
import in.hocg.web.modules.service.DepartmentService;
import in.hocg.web.modules.service.RoleService;
import in.hocg.web.modules.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by hocgin on 2017/10/28.
 * email: hocgin@gmail.com
 */
@Service
public class DepartmentServiceImpl extends BaseService implements DepartmentService {
    private DepartmentRepository departmentRepository;
    private UserService userService;
    private RoleService roleService;
    
    @Autowired
    @Lazy
    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                                 UserService userService,
                                 RoleService roleService) {
        this.departmentRepository = departmentRepository;
        this.userService = userService;
        this.roleService = roleService;
    }
    
    @Override
    public void insert(DepartmentFilter filter, CheckError checkError) {
        Department department = filter.get();
        String path = "";
        String parentId = department.getParent();
        if (!StringUtils.isEmpty(parentId)) {
            Department parentDepartment = departmentRepository.findOne(parentId);
            if (ObjectUtils.isEmpty(parentDepartment)) {
                checkError.putError("所选的上级单位不存在");
                return;
            }
            path = StringUtils.isEmpty(parentDepartment.getPath()) ? "" : parentDepartment.getPath();
            parentDepartment.setHasChildren(true);
            departmentRepository.save(parentDepartment);
        }
        department.setPath(getSubPath(path));
        departmentRepository.save(department);
    }
    
    /**
     * 级联删除
     *
     * @param id
     */
    @Override
    public void delete(String id, CheckError checkError) {
        Department department = departmentRepository.findOne(id);
        if (ObjectUtils.isEmpty(department)) {
            return;
        }
        if (department.getBuiltIn()) {
            checkError.putError("内置分组, 不可删除");
            return;
        }
        
        List<Department> all = departmentRepository.findAllByPathRegex(String.format("%s.*", (StringUtils.isEmpty(department.getPath()) ? "" : department.getPath())));
        String[] ids = all
                .stream()
                .map(Department::getId)
                .toArray(String[]::new);
        
        // 删除此单位 及 子类单位
        departmentRepository.deleteAllByIdIn(ids);
        if (!StringUtils.isEmpty(department.getParent())
                && departmentRepository.countByParent(department.getParent()) < 1) { // 判断是否把父节点设置为根结点
            departmentRepository.updateHasChildren(department.getParent(), false);
        }
        
        // 删除用户 和 此单位及子单位之间的关联(这些单位的用户所属单位设置为null)
        userService.removeDepartmentField(ids);
        
        // 删除角色 和 此单位及子单位之间的关联(直接删除掉这些角色)
        roleService.deleteAllByDepartmentIn(ids);
    }
    
    @Override
    public void update(DepartmentFilter filter, CheckError checkError) {
        String id = filter.getId();
        Department department = departmentRepository.findOne(id);
        if (ObjectUtils.isEmpty(department)) {
            checkError.putError("更新的单位不存在");
            return;
        }
        departmentRepository.save(filter.update(department));
    }
    
    /**
     * 刷新 Department 的 HasChildren 状态
     * @param id
     */
    public void refreshHasChildren(String id) {
        if (!StringUtils.isEmpty(id)) {
            Department department = departmentRepository.findOne(id);
            if (!ObjectUtils.isEmpty(department)) {
                department.setHasChildren(departmentRepository.countByParent(department.getId()) > 0);
                departmentRepository.save(department);
            }
        }
    }
    
    
    @Override
    public List<Department> queryChildren(String parentId) {
        return departmentRepository.findAllByParentIn(parentId);
    }
    
    /**
     * 查询根结点
     *
     * @return
     */
    @Override
    public List<Department> queryRoot() {
        return departmentRepository.findAllByParentIn(null, "");
    }
    
    @Override
    public Department findById(String id) {
        return departmentRepository.findOne(id);
    }
    
    
    /**
     * 获取子路径结构算法
     *
     * @param parentPath
     * @return
     */
    public String getSubPath(String parentPath) {
        List<Department> all = departmentRepository.findAllByPathRegexOrderByPathDesc(parentPath + ".{4}");
        String rsvalue = parentPath + "0001";
        if (all.size() > 0) {
            rsvalue = all.get(0).getPath();
            int newvalue = Integer.parseInt(rsvalue.substring(rsvalue.length() - 4)) + 1;
            rsvalue = rsvalue.substring(0, rsvalue.length() - 4)
                    + new java.text.DecimalFormat("0000")
                    .format(newvalue);
        }
        return rsvalue;
    }
}
