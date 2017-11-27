package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.MailTemplate;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by hocgin on 2017/11/26.
 * email: hocgin@gmail.com
 */
public interface MailTemplateRepository extends DataTablesRepository<MailTemplate, String>,
        MongoRepository<MailTemplate, String> {
    void deleteAllByIdIn(String... id);
    
    MailTemplate findFirstByName(String name);
}
