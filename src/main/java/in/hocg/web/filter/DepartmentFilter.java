package in.hocg.web.filter;

import in.hocg.web.filter.group.Insert;
import in.hocg.web.filter.group.Update;
import in.hocg.web.modules.domain.Department;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * Created by hocgin on 2017/11/8.
 * email: hocgin@gmail.com
 * Department 增加与更新相关
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentFilter extends BaseFilter {
    
    /**
     * 仅更新拥有
     */
    @NotBlank(message = "ID异常", groups = {Update.class})
    private String id;
    
    
    /**
     * 更新 与 增加 均拥有
     */
    @NotBlank(message = "单位名字为必填", groups = {Insert.class, Update.class})
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5._]+", message = "单位名字只允许由中文、英文、点、下划线组成", groups = {Update.class, Insert.class})
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
    
        department.setUpdatedAt(new Date());
        return department;
    }
}
