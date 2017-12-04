package in.hocg.web.modules.system.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.ShortUrl;
import in.hocg.web.modules.system.filter.ShortUrlFilter;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
public interface ShortUrlService {
    ShortUrl findOneByCode(String code);
    
    ShortUrl findOne(String id);
    
    DataTablesOutput data(DataTablesInput input);
    
    void insert(ShortUrlFilter filter, CheckError checkError);
    
    void update(ShortUrlFilter filter, CheckError checkError);
    
    void delete(String[] id, CheckError checkError);
    
    void available(String id, boolean available);
}
