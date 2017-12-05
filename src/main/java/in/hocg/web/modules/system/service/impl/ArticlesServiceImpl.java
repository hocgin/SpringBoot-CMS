package in.hocg.web.modules.system.service.impl;

import in.hocg.web.modules.base.Base2Service;
import in.hocg.web.modules.system.domain.Articles;
import in.hocg.web.modules.system.domain.repository.ArticlesRepository;
import in.hocg.web.modules.system.service.ArticlesService;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Service
public class ArticlesServiceImpl
        extends Base2Service<Articles, String, ArticlesRepository> implements ArticlesService {
    
    @Override
    public DataTablesOutput data(DataTablesInput input) {
        return repository.findAll(input);
    }
}
