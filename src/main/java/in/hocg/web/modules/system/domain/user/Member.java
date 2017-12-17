package in.hocg.web.modules.system.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.util.DigestUtils;
import org.springframework.util.SerializationUtils;

import java.util.Date;

/**
 * Created by hocgin on 2017/11/14.
 * email: hocgin@gmail.com
 * 客户
 */
@Data
public class Member extends Universal {
    
    private Boolean isVerifyEmail = Boolean.FALSE; // Email是否校验
    private Date verifyEmailAt; // 校验截止日期, 为 null 或 过期 为失效
    private Member.Token token;  // 邮箱被认证后分配 Token
    
    public Member(User user) {
    
    }
    
    
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
    
        public static String genToken(String memberId) {
            byte[] bytes = SerializationUtils.serialize(String.format("%s::%d",
                    memberId,
                    System.currentTimeMillis()));
            return DigestUtils.md5DigestAsHex(bytes);
        }
    }
    
}
