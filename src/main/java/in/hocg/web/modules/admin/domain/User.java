package in.hocg.web.modules.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.hocg.web.modules.base.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.Date;

/**
 * Created by hocgin on 2017/10/24.
 * email: hocgin@gmail.com
 * 后台用户
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "User")
public class User extends BaseDomain {
    @Id
    private String id;
    private String username; // 账号 [必须, 唯一, 用于登陆]
    
    private String nickname; // 昵称 [用于显示]
    
    private String email;    // 邮箱 [必须, 用于重置密码]
    
    @JsonIgnore
    private String signUpIP; // 注册IP
    @JsonIgnore
    private String logInIP;  // 登陆IP
    @JsonIgnore
    private String userAgent; // 登陆信息, 系统/浏览器
    @JsonIgnore
    public Date logInAt;
    
    @JsonIgnore
    private String password; // 密码
    
    @JsonIgnore
    private Date lastPasswordResetAt; // 最后一次重置密码时间
    
    private Boolean available = Boolean.FALSE; // 是否可用, 默认保留, 不分配。
    private Boolean builtIn = Boolean.FALSE; // 是否为内置, 默认False
    
    @DBRef
    private Department department; // 所属单位
    
    @DBRef
    private Collection<Role> role; // 角色
    
    public User() {
    }
    
}
