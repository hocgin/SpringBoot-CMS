package in.hocg.web.modules.weather.domain.repository;

import in.hocg.web.modules.weather.domain.RequestCache;
import in.hocg.web.modules.weather.domain.repository.custom.RequestCacheRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by hocgin on 2017/11/22.
 * email: hocgin@gmail.com
 */
public interface RequestCacheRepository extends
        MongoRepository<RequestCache, String>,
        RequestCacheRepositoryCustom {
    RequestCache findTopByParamAndType(String ip, String type);
    
    List<RequestCache> findAllByTypeInAndCreatedAtBetween(String[] inType, Date start, Date end);
    
    List<RequestCache> findAllByTypeIn(String[] inType);
}
