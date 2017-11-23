package in.hocg.web.modules.system.service.impl;

import in.hocg.web.modules.system.domain.SysLog;
import in.hocg.web.modules.system.domain.repository.SysLogRepository;
import in.hocg.web.modules.system.filter.SysLogDataTablesInputFilter;
import in.hocg.web.modules.system.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

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
    
    @Override
    public DataTablesOutput data(SysLogDataTablesInputFilter filter) {
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(filter.getFrom())) {
            criteria.and("from").is(filter.getFrom());
        }
        if (!StringUtils.isEmpty(filter.getRegexMessage())) {
            criteria.and("msg").regex(String.format("%s.*", filter.getRegexMessage()));
        }
        if (!StringUtils.isEmpty(filter.getTag())) {
            criteria.and("tag").is(filter.getTag());
        }
        if (!StringUtils.isEmpty(filter.getCreatedAtRange())) {
            criteria.and("createdAt").lte(filter.getFormatCreatedAtEnd())
                    .gte(filter.getFormatCreatedAtStart());
        }
        DataTablesOutput<SysLog> all = sysLogRepository.findAll(filter, criteria);
        all.setDraw(0);
        return all;
    }
    
    @Override
    public List getTags() {
        return sysLogRepository.getTags();
    }
    
    public SysLog save(SysLog sysLog) {
        return sysLogRepository.save(sysLog);
    }
    
    @Override
    public void empty() {
        sysLogRepository.deleteAll();
    }
}
