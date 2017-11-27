package in.hocg.web.modules.system.domain;

import in.hocg.web.modules.base.BaseDomain;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

/**
 * Created by hocgin on 2017/10/24.
 * email: hocgin@gmail.com
 */
@Data
@Document(collection = "Role")
public class Role extends BaseDomain {
    
    @Id
    private String id;
    
    private String name; // 名称: 管理员
    
    private String role; // 角色标志: admin
    
    private String description; // 角色描述
    
    private Boolean available = Boolean.FALSE; // 是否启用, 默认禁止。
    
    private Boolean builtIn = Boolean.FALSE; // 是否为内置, 默认False
    
    @DBRef
    private Department department;
    
    @DBRef
    private Collection<SysMenu> permissions;
    
    public Role() {
    }
    
    // 管理员
    @Transient
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
}
