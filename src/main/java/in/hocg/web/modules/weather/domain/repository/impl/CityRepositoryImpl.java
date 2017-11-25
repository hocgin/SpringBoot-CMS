package in.hocg.web.modules.weather.domain.repository.impl;

import in.hocg.web.modules.base.BaseMongoCustom;
import in.hocg.web.modules.weather.domain.City;
import in.hocg.web.modules.weather.domain.repository.custom.CityRepositoryCustom;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * Created by hocgin on 2017/11/25.
 * email: hocgin@gmail.com
 */
public class CityRepositoryImpl
        extends BaseMongoCustom<City, String>
        implements CityRepositoryCustom {
    @Override
    public List<City> queryForCity(String q, int size) {
        q = String.format(".*%s.*", q);
        Query query = Query.query(
                new Criteria().orOperator(
                        Criteria.where("city").regex(q),
                        Criteria.where("province").regex(q),
                        Criteria.where("county").regex(q)
                )
        );
        if (size != -1) {
            query.limit(size);
        }
        return find(query);
    }
}
