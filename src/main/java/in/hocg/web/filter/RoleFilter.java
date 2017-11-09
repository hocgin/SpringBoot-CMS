package in.hocg.web.filter;

import in.hocg.web.filter.group.Insert;
import in.hocg.web.filter.group.Update;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created by hocgin on 2017/11/9.
 * email: hocgin@gmail.com
 */
@Data
public class RoleFilter implements Serializable {
    /**
     * 仅更新拥有
     */
    @NotBlank(message = "ID异常", groups = {Update.class})
    private String id;
    
    /**
     * 更新 与 增加 均拥有
     */
    private String description;
    
    /**
     * 仅增加拥有
     */
    @NotBlank(message = "角色名称为必填", groups = {Update.class, Insert.class})
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5._]+", message = "角色名称只允许由中文、英文、点、下划线组成", groups = {Update.class, Insert.class})
    private String name;
    
    @NotBlank(message = "角色标识为必填", groups = {Update.class, Insert.class})
    @Pattern(regexp = "^[A-Z_]+", message = "角色标识允许由大写英文、下划线组成", groups = {Update.class, Insert.class})
    private String role;
    private boolean available = Boolean.FALSE;
    private String departmentId; // 所属单位ID
}
