package in.hocg.web.modules.system.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.hocg.web.modules.base.BaseDomain;
import in.hocg.web.modules.system.domain.Role;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

/**
 * Created by hocgin on 2017/12/16.
 * email: hocgin@gmail.com
 * 用户公有属性
 */
@Data
public abstract class Universal extends BaseDomain {
    String id;
    String nickname;  // 用户名
    String email;     // 邮箱
    @JsonIgnore
    String userAgent; // 登陆信息, 系统/浏览器
    @JsonIgnore
    String password;  // 密码
    @JsonIgnore
    String signUpIP;  // 注册IP
    @JsonIgnore
    String logInIP;   // 登陆IP
    @JsonIgnore
    Date logInAt;     // 最近登陆时间
    @JsonIgnore
    Date lastPasswordResetAt; // 最后一次修改密码时间
    String avatar; // 头像地址
    Integer type; // 用户类型 [管理员 / 会员]
    
    Boolean available = Boolean.FALSE; // 是否可用, 默认保留, 不分配。
    Boolean builtIn = Boolean.FALSE; // 是否为内置, 默认False
    
    @DBRef
    Collection<Role> role; // 角色
    
    public String getAvatar() {
        return Optional.ofNullable(avatar).orElse("/public/images/default_avatar.gif");
    }
}
