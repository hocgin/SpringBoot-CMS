package in.hocg.web.modules.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by hocgin on 2017/10/24.
 * email: hocgin@gmail.com
 */
//@Entity
@Data
@Document(collection = "Role")
public class Role implements Serializable{
    @Id
    private String id;
    
    private String name; // 名称: 管理员
    
    private String role; // 角色标志: admin
    
    private String description; // 角色描述
    
    private Boolean available = Boolean.FALSE; // 是否可用, 默认保留, 不分配。
    
    @DBRef
    private Department department;
    
    @DBRef
    private Collection<Permission> permissions;
    
    public Role() {
    }
    
    public Role(String name) {
        this.name = name;
    }
    
    public static final String ROLE_USER = "ROLE_USER";
}
