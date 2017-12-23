package in.hocg.web.modules.system.domain.user;

import in.hocg.web.modules.security.details.IUser;
import in.hocg.web.modules.system.domain.Department;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by hocgin on 2017/10/24.
 * email: hocgin@gmail.com
 * 用户[管理员 + 会员]
 *
 * - 邮箱 和 username 全站唯一
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "User")
public class User extends Universal {
    
    /**
     * 管理员独有属性
     */
    @DBRef
    private Department department; // 所属单位
    private String username;
    
    
    /**
     * 会员独有属性
     */
    private Boolean isVerifyEmail = Boolean.FALSE; // Email是否校验
    private Date verifyEmailAt; // 校验截止日期, 为 null 或 过期 为失效
    private Member.Token token;  // 邮箱被认证后分配 Token
    
    public enum Type {
        Manager(0), // 管理员
        Member(1);  // 会员
        private int code;
        
        Type(int code) {
            this.code = code;
        }
        
        public int getCode() {
            return code;
        }
        
        public void setCode(int code) {
            this.code = code;
        }
    }
    
    public IUser asIUser(){
        return new IUser(this);
    }
    
//    public Member asMember() {
//        return new Member(this);
//    }
//    public Manager asManager() {
//        return new Manager(this);
//    }
    
}
