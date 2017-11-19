package in.hocg.web.modules.system.filter;

import in.hocg.web.modules.base.filter.BaseFilter;
import in.hocg.web.modules.base.filter.group.Insert;
import in.hocg.web.modules.base.filter.group.Update1;
import in.hocg.web.modules.base.filter.group.Update2;
import in.hocg.web.modules.system.domain.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * Created by hocgin on 2017/11/9.
 * email: hocgin@gmail.com
 * Role 增加与更新相关
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleFilter  extends BaseFilter {
    /**
     * 仅更新拥有
     */
    @NotBlank(message = "ID异常", groups = {Update1.class, Update2.class})
    private String id;
    
    /**
     * 更新 与 增加 均拥有
     */
    private String description; // 说明
    private String[] permissionIds; // 拥有的权限ID
    
    /**
     * 仅增加拥有
     */
    @NotBlank(message = "角色名称为必填", groups = {Insert.class})
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5._]+", message = "角色名称只允许由中文、英文、点、下划线组成", groups = {Insert.class})
    private String name;
    
    @NotBlank(message = "角色标识为必填", groups = {Insert.class})
    @Pattern(regexp = "^[A-Z_]+", message = "角色标识允许由大写英文、下划线组成", groups = {Insert.class})
    private String role;
    private boolean available = Boolean.FALSE;
    @NotBlank(message = "所属单位异常", groups = {Insert.class})
    private String departmentId; // 所属单位ID
    
    public Role get() {
        Role r = new Role();
        r.setName(name);
        r.setRole(role);
        r.setAvailable(available);
        r.setDescription(description);
        
        r.setCreatedAt(new Date());
        return r;
    }
    
    public Role update1(Role r) {
        r.setDescription(description);
        
        r.setUpdatedAt(new Date());
        return r;
    }
    
}
