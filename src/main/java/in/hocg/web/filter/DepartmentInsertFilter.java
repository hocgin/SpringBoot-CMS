package in.hocg.web.filter;

import in.hocg.web.modules.domain.Department;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hocgin on 2017/11/8.
 * email: hocgin@gmail.com
 */
@Data
public class DepartmentInsertFilter implements Serializable {
    private String parent;
    @NotEmpty(message = "单位名字不能为空")
    private String name;
    private String phone;
    private String address;
    private String description;
    
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
}
