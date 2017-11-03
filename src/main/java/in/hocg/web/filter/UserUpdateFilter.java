package in.hocg.web.filter;

import lombok.Data;

/**
 * Created by hocgin on 2017/11/2.
 * email: hocgin@gmail.com
 */
@Data
public class UserUpdateFilter {
    private String id;
    private String password;
    private String nickname;
    private String email;
    private String departmentId;
    private boolean available;
}
