package in.hocg.web.modules.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * Created by hocgin on 2017/10/24.
 * email: hocgin@gmail.com
 */
@Data
@Document(collection = "User")
public class User implements Serializable {
    @Id
    private String id;
    private String username; // 账号
    
    @JsonIgnore
    private String password; // 密码
    
    @JsonIgnore
    private Date lastPasswordResetDate; // 最后一次重置密码时间
    
    @JsonIgnore
    private byte state; // 用户状态
    
    @DBRef
    private Collection<Role> role;
    
    public User() {
    }
    
}
