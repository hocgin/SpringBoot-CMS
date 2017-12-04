package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.ShortUrl;
import in.hocg.web.modules.system.domain.repository.custom.ShortUrlRepositoryCustom;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
public interface ShortUrlRepository
        extends MongoRepository<ShortUrl, String>,
        DataTablesRepository<ShortUrl, String>,
        ShortUrlRepositoryCustom {
    
    ShortUrl findTopByCodeAndAvailable(String code, boolean available);
    
    ShortUrl findTopByCode(String code);
    
    void deleteAllByIdIn(String... id);
    
    ShortUrl findTopByOriginalUrl(String originalUrl);
}
