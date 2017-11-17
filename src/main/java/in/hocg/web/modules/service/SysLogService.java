package in.hocg.web.modules.service;

import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */
public interface SysLogService {
    DataTablesOutput data(DataTablesInput filter);
    
    void empty();
}
