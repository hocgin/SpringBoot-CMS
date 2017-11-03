package in.hocg.web.filter;

import in.hocg.web.modules.domain.User;
import lombok.Data;

/**
 * Created by hocgin on 2017/11/2.
 * email: hocgin@gmail.com
 */
@Data
public class UserInsertFilter {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String departmentId;
    private boolean available;
    
    public User getInsertUser() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setAvailable(available);
        user.setEmail(email);
        return user;
    }
}
