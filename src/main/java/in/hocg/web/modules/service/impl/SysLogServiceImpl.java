package in.hocg.web.modules.service.impl;

import in.hocg.web.modules.domain.repository.SysLogRepository;
import in.hocg.web.modules.service.SysLogService;
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
    
    @Override
    public void empty() {
        sysLogRepository.deleteAll();
    }
}
