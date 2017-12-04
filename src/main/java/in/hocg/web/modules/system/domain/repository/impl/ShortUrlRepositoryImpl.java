package in.hocg.web.modules.system.domain.repository.impl;

import in.hocg.web.modules.base.BaseMongoCustom;
import in.hocg.web.modules.system.domain.ShortUrl;
import in.hocg.web.modules.system.domain.repository.custom.ShortUrlRepositoryCustom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by hocgin on 2017/12/4.
 * email: hocgin@gmail.com
 */
public class ShortUrlRepositoryImpl
        extends BaseMongoCustom<ShortUrl, String>
        implements ShortUrlRepositoryCustom {
    @Override
    public boolean alreadyExists(String code, String... notInIds) {
        return exists(Query.query(Criteria.where("code").is(code).and("id").nin(notInIds)));
    }
}
