package in.hocg.web.modules.admin.service;

import in.hocg.web.modules.admin.domain.SysLog;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */
public interface SysLogService {
    DataTablesOutput data(DataTablesInput filter);
    
    void empty();
    
    SysLog save(SysLog sysLog);
}
