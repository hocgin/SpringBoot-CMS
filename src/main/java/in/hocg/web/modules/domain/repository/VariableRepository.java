package in.hocg.web.modules.domain.repository;

import in.hocg.web.modules.domain.Variable;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public interface VariableRepository
        extends MongoRepository<Variable, String>,
        DataTablesRepository<Variable, String> {
    int countAllByKey(String key);
    
    Variable findVariableByIdAndKey(String id, String key);
    
    void deleteAllByIdIn(String... id);
    
    List<Variable> findAllByIdIn(String... id);
}
