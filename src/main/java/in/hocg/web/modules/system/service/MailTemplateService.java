package in.hocg.web.modules.system.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.MailTemplate;
import in.hocg.web.modules.system.filter.MailTemplateDataTablesInputFilter;
import in.hocg.web.modules.system.filter.MailTemplateFilter;
import in.hocg.web.modules.system.filter.SendGroupMailFilter;
import in.hocg.web.modules.system.filter.SendMailFilter;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

/**
 * Created by hocgin on 2017/11/26.
 * email: hocgin@gmail.com
 */
public interface MailTemplateService {
    
    DataTablesOutput<MailTemplate> data(MailTemplateDataTablesInputFilter filter);
    
    void delete(CheckError checkError, String... id);
    
    void insert(MailTemplateFilter filter, CheckError checkError);
    
    MailTemplate find(String id);
    
    void update(MailTemplateFilter filter, CheckError checkError);
    
    void sendGroup(String id, SendGroupMailFilter filter, CheckError checkError);
    void send(String id, SendMailFilter filter, CheckError checkError);
}
