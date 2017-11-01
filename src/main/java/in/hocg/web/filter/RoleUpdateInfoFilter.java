package in.hocg.web.filter;

import lombok.Data;

/**
 * Created by hocgin on 2017/11/1.
 * email: hocgin@gmail.com
 */
@Data
public class RoleUpdateInfoFilter {
    private String id;
    private String name;
    private String role;
    private String description;
    private String departmentId;
}
