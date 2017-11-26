package in.hocg.web.modules.system.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;

/**
 * Created by hocgin on 2017/11/1.
 * email: hocgin@gmail.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MemberDataTablesInputFilter extends DataTablesInput{
    private String regexEmail;
    private String regexNickname;
}
