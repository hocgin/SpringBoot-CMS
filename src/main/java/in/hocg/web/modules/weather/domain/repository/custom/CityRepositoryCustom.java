package in.hocg.web.modules.weather.domain.repository.custom;

import in.hocg.web.modules.weather.domain.City;

import java.util.List;

/**
 * Created by hocgin on 2017/11/25.
 * email: hocgin@gmail.com
 */
public interface CityRepositoryCustom {
    List<City> queryForCity(String q, int size);
}
