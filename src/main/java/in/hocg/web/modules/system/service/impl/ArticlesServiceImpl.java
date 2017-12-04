package in.hocg.web.modules.system.service.impl;

import in.hocg.web.modules.base.Base2Service;
import in.hocg.web.modules.system.domain.Articles;
import in.hocg.web.modules.system.domain.repository.ArticlesRepository;
import in.hocg.web.modules.system.service.ArticlesService;
import org.springframework.stereotype.Service;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
@Service
public class ArticlesServiceImpl
        extends Base2Service<Articles, String, ArticlesRepository> implements ArticlesService {
}
