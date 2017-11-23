package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.SysLog;
import in.hocg.web.modules.system.domain.repository.custom.SysLogRepositoryCustom;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by hocgin on 2017/11/17.
 * email: hocgin@gmail.com
 */
public interface SysLogRepository extends DataTablesRepository<SysLog, String>,
        SysLogRepositoryCustom,
        MongoRepository<SysLog, String> {
}
