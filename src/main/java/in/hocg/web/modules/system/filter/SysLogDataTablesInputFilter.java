package in.hocg.web.modules.system.filter;

import in.hocg.web.lang.DateKit;
import lombok.Data;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;

import java.util.Date;

/**
 * Created by hocgin on 2017/11/23.
 * email: hocgin@gmail.com
 */
@Data
public class SysLogDataTablesInputFilter extends DataTablesInput {
    private String regexMessage;
    private String from;
    private String tag;
    private String createdAtRange;
    
    public Date getFormatCreatedAtStart() {
        return DateKit.format(createdAtRange.split(" 至 ")[0]);
    }
    
    public Date getFormatCreatedAtEnd() {
        return DateKit.format(createdAtRange.split(" 至 ")[1]);
    }
}
