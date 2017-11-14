package in.hocg.web.modules.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.Date;

/**
 * Created by hocgin on 2017/11/14.
 * email: hocgin@gmail.com
 * 客户
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "Custom")
public class Custom extends BaseDomain {
    private String email;    // == username
    
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String signUpIP; // 注册IP
    @JsonIgnore
    private String loginIP;  // 登陆IP
    @JsonIgnore
    private String userAgent; // 登陆信息, 系统/浏览器
    @JsonIgnore
    private Date lastPasswordResetAt; // 最后一次修改密码时间
    
    private Boolean available = Boolean.FALSE; // 是否可用, 默认保留, 不可用。
    
    @DBRef
    private Collection<Role> role; // 角色
}
