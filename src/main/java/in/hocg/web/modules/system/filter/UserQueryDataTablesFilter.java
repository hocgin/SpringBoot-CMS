package in.hocg.web.modules.system.filter;

import lombok.Data;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;

/**
 * Created by hocgin on 2017/12/21.
 * email: hocgin@gmail.com
 */
@Data
public class UserQueryDataTablesFilter extends DataTablesInput {
    
    private String nicknameOrUsernameOrIDorEmail;
}
