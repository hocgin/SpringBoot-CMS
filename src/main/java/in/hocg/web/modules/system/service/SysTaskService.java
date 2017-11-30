package in.hocg.web.modules.system.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.SysTask;
import in.hocg.web.modules.system.filter.SysTaskFilter;
import org.quartz.SchedulerException;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

/**
 * Created by hocgin on 2017/11/30.
 * email: hocgin@gmail.com
 */
public interface SysTaskService {
    DataTablesOutput data(DataTablesInput filter);
    
    SysTask findOne(String id);
    
    void insert(SysTaskFilter filter, CheckError checkError);
    
    void update(SysTaskFilter filter, CheckError checkError);
    
    void delete(String... id);
    
    void available(String id, boolean available, CheckError checkError);
    
    void resume(String id, CheckError checkError) throws SchedulerException;
    
    void restart(String id, CheckError checkError);
    
    void init();
}
