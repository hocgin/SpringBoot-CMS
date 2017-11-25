package in.hocg.web.modules.weather.service;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.weather.domain.City;
import in.hocg.web.modules.weather.filter.CityDataTablesInputFilter;
import in.hocg.web.modules.weather.filter.CityFilter;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

import java.util.Collection;
import java.util.List;

/**
 * Created by hocgin on 2017/11/21.
 * email: hocgin@gmail.com
 */
public interface CityService {
    
    DataTablesOutput findAll(CityDataTablesInputFilter filter);
    
    void deletes(String... id);
    
    void insert(CityFilter cityFilter, CheckError checkError);
    
    void update(CityFilter filter, CheckError checkError);
    
    void inserts(Collection<City> citySet);
    
    void deleteAll();
    
    List<City> searchForCity(String q, int size);
    List<City> searchForCity(String q);
    
    City findById(String id);
}
