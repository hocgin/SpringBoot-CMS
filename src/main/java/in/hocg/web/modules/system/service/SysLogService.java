package in.hocg.web.modules.system.service;

import in.hocg.web.modules.system.domain.SysLog;
import in.hocg.web.modules.system.filter.SysLogDataTablesInputFilter;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

import java.util.List;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */
public interface SysLogService {
    DataTablesOutput data(SysLogDataTablesInputFilter filter);
    
    void empty();
    
    SysLog save(SysLog sysLog);
    
    List getTags();
    
    void aInfo(String tag, String msg);
}
