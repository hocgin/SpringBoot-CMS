package in.hocg.web.modules.system.service;

import in.hocg.web.modules.system.filter.VariableFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.Variable;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 * todo 后期进行 redis 缓存
 */
public interface VariableService {
    DataTablesOutput data(DataTablesInput input);
    
    Variable insert(VariableFilter filter, CheckError checkError);
    
    Variable update(VariableFilter filter, CheckError checkError);
    void delete(CheckError checkError, String... id);
    
    Variable findById(String id);
    
    String getValue(String key, String def);
    
    
    boolean getBool(String key, Boolean def);
}
