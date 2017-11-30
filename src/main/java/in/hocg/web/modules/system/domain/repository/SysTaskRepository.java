package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.SysTask;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public interface SysTaskRepository
        extends MongoRepository<SysTask, String>,
        DataTablesRepository<SysTask, String> {
    List<SysTask> findAllByIdIn(String... id);
    
    List<SysTask> findAllByAvailable(boolean available);
}
