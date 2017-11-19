package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.IFile;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by hocgin on 2017/11/19.
 * email: hocgin@gmail.com
 */
public interface IFileRepository extends DataTablesRepository<IFile, String>,
        MongoRepository<IFile, String> {
    IFile findFirstByMd5(String md5);
    
    void deleteAllByIdIn(String... id);
    
    void removeAllByKeepNameNotIn(String... keepName);
    
    IFile findByKeepName(String keepName);
}
