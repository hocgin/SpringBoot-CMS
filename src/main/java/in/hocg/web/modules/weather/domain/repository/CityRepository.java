package in.hocg.web.modules.weather.domain.repository;

import in.hocg.web.modules.weather.domain.City;
import in.hocg.web.modules.weather.domain.repository.custom.CityRepositoryCustom;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by hocgin on 2017/11/21.
 * email: hocgin@gmail.com
 */
public interface CityRepository extends DataTablesRepository<City, String>,
        CityRepositoryCustom,
        MongoRepository<City, String> {
    void deleteAllByIdIn(String... id);
}
