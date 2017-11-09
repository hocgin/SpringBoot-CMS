package in.hocg.web.filter;

import in.hocg.web.filter.group.Insert;
import in.hocg.web.filter.group.Update;
import in.hocg.web.modules.domain.Permission;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by hocgin on 2017/11/8.
 * email: hocgin@gmail.com
 */
@Data
public class PermissionFilter implements Serializable {
    /**
     * 仅更新拥有
     */
    @NotBlank(message = "ID异常", groups = {Update.class})
    private String id;
    
    /**
     * 更新 与 增加 均拥有
     */
    @NotBlank(message = "权限名称为必填", groups = {Update.class, Insert.class})
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5._]+", message = "权限名称只允许由中文、英文、点、下划线组成", groups = {Update.class, Insert.class})
    private String name; // 名称: 用户添加
    @NotNull(message = "权限类型为必填", groups = {Update.class, Insert.class})
    @Pattern(regexp = "^[0-1]+", message = "权限类型错误", groups = {Update.class, Insert.class})
    private Integer type; // 资源类型 0 菜单 1 按钮
    private String url;  // 资源路径.
    @NotBlank(message = "权限标识为必填", groups = {Update.class, Insert.class})
    @Pattern(regexp = "^[a-zA-Z._]+", message = "权限标识只允许由英文、点、下划线组成", groups = {Update.class, Insert.class})
    private String permission; // 权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view
    private String target;
    private String icon;  // 图标 class
    private boolean available = Boolean.FALSE;
    
    /**
     * 仅增加拥有
     */
    private String parent;   // 父ID
    
    public Permission get() {
        Permission permissionObject = new Permission();
        permissionObject.setAvailable(available);
        permissionObject.setParent(parent);
        permissionObject.setPermission(permission);
        permissionObject.setTarget(target);
        permissionObject.setIcon(icon);
        permissionObject.setName(name);
        permissionObject.setUrl(url);
        permissionObject.setType(type);
        
        permissionObject.setCreatedAt(new Date());
        return permissionObject;
    }
    
    public Permission update(Permission permissionObject) {
        permissionObject.setAvailable(available);
        permissionObject.setPermission(permission);
        permissionObject.setTarget(target);
        permissionObject.setIcon(icon);
        permissionObject.setName(name);
        permissionObject.setUrl(url);
        permissionObject.setType(type);
        
        permissionObject.setUpdatedAt(new Date());
        return permissionObject;
    }
}
