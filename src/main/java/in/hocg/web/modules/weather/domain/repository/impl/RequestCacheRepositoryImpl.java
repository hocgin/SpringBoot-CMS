package in.hocg.web.modules.weather.domain.repository.impl;

import in.hocg.web.lang.DateKit;
import in.hocg.web.modules.base.BaseMongoCustom;
import in.hocg.web.modules.weather.domain.RequestCache;
import in.hocg.web.modules.weather.domain.repository.custom.RequestCacheRepositoryCustom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by hocgin on 2017/11/23.
 * email: hocgin@gmail.com
 */
public class RequestCacheRepositoryImpl
        extends BaseMongoCustom<RequestCache, String>
        implements RequestCacheRepositoryCustom {
    
    @Override
    public RequestCache findByParamOnToday(String param, String type) {
        Query query = Query.query(
                Criteria.where("param").is(param)
                        .and("type").is(type)
                        .and("createdAt").gte(DateKit.getTodayStart()).lt(DateKit.getTodayEnd())
        );
        return findOne(query);
    }
}
