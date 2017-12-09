package in.hocg.web.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.hocg.web.modules.base.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.DigestUtils;
import org.springframework.util.SerializationUtils;

import java.util.Collection;
import java.util.Date;

/**
 * Created by hocgin on 2017/11/14.
 * email: hocgin@gmail.com
 * 客户
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "Member")
public class Member extends BaseDomain {
    @Id
    private String id;
    private String nickname;
    private String email;    // == username
    
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String signUpIP; // 注册IP
    @JsonIgnore
    private String logInIP;  // 登陆IP
    @JsonIgnore
    public Date logInAt;
    @JsonIgnore
    private String userAgent; // 登陆信息, 系统/浏览器
    @JsonIgnore
    private Date lastPasswordResetAt; // 最后一次修改密码时间
    
    private Token token;  // 邮箱被认证后分配 Token
    
    private String avatar = "/public/images/default_avatar.gif";
    
    private Boolean available = Boolean.FALSE; // 是否可用, 默认不可用。
    private Boolean isVerifyEmail = Boolean.FALSE; // Email是否校验
    private Date verifyEmailAt; // 校验截止日期, 为 null 或 过期 为失效
    
    @DBRef
    private Collection<Role> role; // 角色
    
    
    @Data
    public static class Token {
        // token
        private String token;
        // 使用次数
        private long count = 0;
        // 是否可用, 默认禁止。
        private Boolean available = Boolean.FALSE;
    
        // 目前只有一种级别
        @JsonIgnore
        public static Token gen(String memberId) {
            Token token = new Token();
            token.setAvailable(true);
            byte[] bytes = SerializationUtils.serialize(String.format("%s::%d",
                    memberId,
                    System.currentTimeMillis()));
            token.setToken(DigestUtils.md5DigestAsHex(bytes));
            return token;
        }
    }
    
}
