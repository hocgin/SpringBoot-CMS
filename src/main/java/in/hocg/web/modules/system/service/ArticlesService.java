package in.hocg.web.modules.system.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.Articles;
import in.hocg.web.modules.system.filter.ArticlesFilter;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
public interface ArticlesService {
    DataTablesOutput data(DataTablesInput input);
    
    void insert(ArticlesFilter filter, CheckError checkError);
    
    Articles findOne(String id);
    
    void update(ArticlesFilter filter, CheckError checkError);
    
    void available(String id, boolean available);
    
    void deletes(String[] id, CheckError checkError);
    
    void deletesByChannel(String... channelsId);
}
