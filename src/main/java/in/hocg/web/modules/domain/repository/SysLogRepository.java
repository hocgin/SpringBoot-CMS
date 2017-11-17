package in.hocg.web.modules.domain.repository;

import in.hocg.web.modules.domain.SysLog;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */
public interface SysLogRepository extends DataTablesRepository<SysLog, String>,
        MongoRepository<SysLog, String> {
}
