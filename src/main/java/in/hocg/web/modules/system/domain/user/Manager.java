package in.hocg.web.modules.system.domain.user;

import in.hocg.web.modules.system.domain.Department;
import lombok.Data;

/**
 * Created by hocgin on 2017/12/16.
 * email: hocgin@gmail.com
 */
@Data
public class Manager extends Universal {
    private Department department; // 所属单位
    private String username;
    
    public Manager(User user) {
        this.department = user.getDepartment();
        this.username = user.getUsername();
        
        
    }
}
