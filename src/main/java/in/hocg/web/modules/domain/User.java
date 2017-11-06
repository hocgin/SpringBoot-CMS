package in.hocg.web.modules.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.Date;

/**
 * Created by hocgin on 2017/10/24.
 * email: hocgin@gmail.com
 */
@Data
@Document(collection = "User")
public class User extends BaseDomain {
    @Id
    private String id;
    private String username; // 账号 [必须, 唯一, 用于登陆]
    
    private String nickname; // 昵称 [用于显示]
    
    private String email;    // 邮箱 [必须, 用于重置密码]
    
    @JsonIgnore
    private String password; // 密码
    
    @JsonIgnore
    private Date lastPasswordResetDate; // 最后一次重置密码时间
    
    private Boolean available = Boolean.FALSE; // 是否可用, 默认保留, 不分配。
    
    @DBRef
    private Department department; // 所属单位
    
    @DBRef
    private Collection<Role> role; // 角色
    
    public User() {
    }
    
}
