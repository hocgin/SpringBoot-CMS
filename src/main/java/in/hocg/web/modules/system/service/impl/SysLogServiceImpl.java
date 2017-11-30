package in.hocg.web.modules.system.service.impl;

import com.google.gson.Gson;
import in.hocg.web.lang.utils.SecurityKit;
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
import java.util.Map;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */
@Service
public class SysLogServiceImpl implements SysLogService {
    private SysLogRepository sysLogRepository;
    private Gson gson;
    
    @Autowired
    public SysLogServiceImpl(SysLogRepository sysLogRepository,Gson gson) {
        this.sysLogRepository = sysLogRepository;
        this.gson = gson;
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
    
    /**
     * 记录日志
     * @param start 执行开始时间
     * @param type 日志类型
     * @param from 记录来源
     * @param tag 记录类型[用户登陆..]
     * @param ip  请求IP
     * @param msg 消息
     * @param params 携带参数
     * @param result 执行结果
     */
    public void log(long start, SysLog.Type type, SysLog.From from,
                    String tag, String ip, String msg, Map<String, Object> params, Object result) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[2];
        String src = String.format("%s#%s", stackTraceElement.getClassName(), stackTraceElement.getMethodName());
        SysLog sysLog = SysLog.NEW(type.name(),
                tag,
                src,
                ip,
                msg,
                gson.toJson(params),
                gson.toJson(result),
                SecurityKit.username(),
                from.name());
        sysLog.setUsageTime(System.currentTimeMillis() - start);
        sysLogRepository.save(sysLog);
    }
    
    @Override
    public void empty() {
        sysLogRepository.deleteAll();
    }
}
