package in.hocg.web.modules.system.service;

import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
public interface ArticlesService {
    DataTablesOutput data(DataTablesInput input);
}
