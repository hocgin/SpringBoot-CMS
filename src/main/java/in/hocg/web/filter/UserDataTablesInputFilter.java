package in.hocg.web.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;

/**
 * Created by hocgin on 2017/11/1.
 * email: hocgin@gmail.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDataTablesInputFilter extends DataTablesInput{
    private String department;
    private String role;
    private String regexNicknameOrUsername;
    private String notRole;
}
