package in.hocg.web.modules.service.impl;

import in.hocg.web.modules.domain.Department;
import in.hocg.web.modules.domain.repository.DepartmentRepository;
import in.hocg.web.modules.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by hocgin on 2017/10/28.
 * email: hocgin@gmail.com
 */
@Service
public class DepartmentServiceImpl extends BaseService implements DepartmentService {
    private DepartmentRepository departmentRepository;
    
    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
    
    @Override
    public void insert(Department department) {
        String path = "";
        String parentId = department.getParent();
        if (!StringUtils.isEmpty(parentId)) {
            Department parentDepartment = departmentRepository.findOne(parentId);
            path = parentDepartment.getPath();
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
    public void delete(String id) {
        Department department = departmentRepository.findOne(id);
        // 删除此单位 及 子类单位
        List<Department> all = departmentRepository.findAllByPathRegex(String.format("%s.*", (StringUtils.isEmpty(department.getPath()) ? "" : department.getPath())));
        if (all.size() > 1) {
            return;
        }
        String[] ids = all
                .stream()
                .map(Department::getId)
                .toArray(String[]::new);
        
        departmentRepository.deleteAllByIdIn(ids);
        if (!StringUtils.isEmpty(department.getParent())
                && departmentRepository.countByParent(department.getParent()) < 1) { // 判断是否把父节点设置为根结点
            departmentRepository.updateHasChildren(department.getParent(), false);
        }
        // 删除此用户 和 单位及子单位之间的关联 todo 上面的先查出id
        
        // 删除角色 和 单位及子单位之间的关联 todo 上面的先查出id
        
    }
    
    @Override
    public void update(Department department) {
        departmentRepository.save(department);
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
