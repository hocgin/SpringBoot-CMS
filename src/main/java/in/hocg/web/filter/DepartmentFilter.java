package in.hocg.web.filter;

import in.hocg.web.filter.group.Insert;
import in.hocg.web.filter.group.Update;
import in.hocg.web.modules.domain.Department;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hocgin on 2017/11/8.
 * email: hocgin@gmail.com
 */
@Data
public class DepartmentFilter implements Serializable {
    
    /**
     * 仅更新拥有
     */
    @NotBlank(message = "ID异常", groups = {
            Update.class
    })
    private String id;
    
    
    /**
     * 更新 与 增加 均拥有
     */
    @NotBlank(message = "单位名字不能为空", groups = {
            Insert.class,
            Update.class
    })
    private String name;
    private String phone;
    private String address;
    private String description;
    
    /**
     * 仅增加拥有
     */
    private String parent;
    
    public Department get() {
        Department department = new Department();
        department.setAddress(address);
        department.setDescription(description);
        department.setParent(parent);
        department.setPhone(phone);
        department.setName(name);
        
        department.setCreatedAt(new Date());
        return department;
    }
    
    public Department update(Department department) {
        department.setAddress(address);
        department.setDescription(description);
        department.setPhone(phone);
        department.setName(name);
        return department;
    }
}
