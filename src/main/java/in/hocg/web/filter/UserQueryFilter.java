package in.hocg.web.filter;

import lombok.Data;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;

/**
 * Created by hocgin on 2017/11/1.
 * email: hocgin@gmail.com
 */
@Data
public class UserQueryFilter extends DataTablesInput{
    private String department;
    private String role;
    private String regexNicknameOrUsername;
    private String notRole;
}
