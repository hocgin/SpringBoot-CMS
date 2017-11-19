package in.hocg.web.modules.system.service.impl;

import in.hocg.web.modules.system.domain.SysLog;
import in.hocg.web.modules.system.domain.repository.SysLogRepository;
import in.hocg.web.modules.system.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */
@Service
public class SysLogServiceImpl implements SysLogService {
    private SysLogRepository sysLogRepository;
    
    @Autowired
    public SysLogServiceImpl(SysLogRepository sysLogRepository) {
        this.sysLogRepository = sysLogRepository;
    }
    
    public DataTablesOutput data(DataTablesInput filter) {
        return sysLogRepository.findAll(filter);
    }
    
    public SysLog save(SysLog sysLog) {
        return sysLogRepository.save(sysLog);
    }
    
    @Override
    public void empty() {
        sysLogRepository.deleteAll();
    }
}
