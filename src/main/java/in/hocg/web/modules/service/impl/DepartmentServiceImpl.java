package in.hocg.web.modules.service.impl;

import in.hocg.web.modules.domain.Department;
import in.hocg.web.modules.domain.repository.DepartmentRepository;
import in.hocg.web.modules.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
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
        String parentId = department.getParent();
        if (parentId != null) {
            Department parent = departmentRepository.findOne(parentId);
            setHasChildren(parent, true);
        }
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
        departmentRepository.findAllByParentIn(department.getId())
                .stream()
                .map(Department::getId)
                .forEach(this::delete);
        
        // 删除此单位 及 子类单位
        departmentRepository.deleteAllByIdIn(id);
        if (!StringUtils.isEmpty(department.getParent())) { // 判断是否把父节点设置为根结点
            List<Department> all = departmentRepository.findAllByParentIn(department.getParent());
            if (all == null
                    || all.size() == 0) {
                Department parentDepartment = departmentRepository.findOne(department.getParent());
                setHasChildren(parentDepartment, false);
            }
        }
        // 删除此单位权限 及 子类单位权限
        
        // 删除用户与 此单位和子类单位 之间的关联
        
    }
    
//    /**
//     * @param id
//     */
//    public void delete(String id) {
//        Department department = departmentRepository.findOne(id);
//        List<Department> children = departmentRepository.findAllByParent(department.getId());
//        String[] array = children.stream()
//                .map(Department::getId)
//                .toArray(String[]::new);
//        departmentRepository.deleteAllByIdIn(array);
//    }
    @Override
    public void update(Department department) {
        departmentRepository.save(department);
    }
    
    public void setHasChildren(Department department, boolean hasChildren) {
        department.setHasChildren(hasChildren);
        departmentRepository.save(department);
    }
    
    @Override
    public DataTablesOutput<Department> data(DataTablesInput input) {
        return departmentRepository.findAll(input);
    }
    
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
}
