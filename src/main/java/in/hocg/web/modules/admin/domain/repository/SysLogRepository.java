package in.hocg.web.modules.admin.domain.repository;

import in.hocg.web.modules.admin.domain.SysLog;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */
public interface SysLogRepository extends DataTablesRepository<SysLog, String>,
        MongoRepository<SysLog, String> {
}
