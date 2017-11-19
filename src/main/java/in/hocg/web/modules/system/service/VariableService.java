package in.hocg.web.modules.system.service;

import in.hocg.web.modules.system.filter.VariableFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.Variable;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public interface VariableService {
    DataTablesOutput data(DataTablesInput input);
    
    void insert(VariableFilter filter, CheckError checkError);
    
    void update(VariableFilter filter, CheckError checkError);
    void delete(CheckError checkError, String... id);
    
    Variable findById(String id);
    
    String getValue(String key, String def);
}
