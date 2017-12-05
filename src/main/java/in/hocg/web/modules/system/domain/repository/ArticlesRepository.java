package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.Articles;
import in.hocg.web.modules.system.domain.repository.custom.ArticlesRepositoryCustom;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
public interface ArticlesRepository extends MongoRepository<Articles, String>,
        DataTablesRepository<Articles, String>,
        ArticlesRepositoryCustom {
    void deleteAllByIdIn(String[] id);
}
