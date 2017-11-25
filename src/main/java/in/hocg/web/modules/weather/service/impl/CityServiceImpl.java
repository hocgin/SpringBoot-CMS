package in.hocg.web.modules.weather.service.impl;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.weather.domain.City;
import in.hocg.web.modules.weather.domain.repository.CityRepository;
import in.hocg.web.modules.weather.filter.CityDataTablesInputFilter;
import in.hocg.web.modules.weather.filter.CityFilter;
import in.hocg.web.modules.weather.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by hocgin on 2017/11/21.
 * email: hocgin@gmail.com
 */
@Service
public class CityServiceImpl implements CityService {
    
    private CityRepository cityRepository;
    
    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }
    
    @Override
    public DataTablesOutput findAll(CityDataTablesInputFilter filter) {
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(filter.getRegexAddress())) {
            criteria.orOperator(
                    Criteria.where("province").regex(String.format("%s.*", filter.getRegexAddress())),
                    Criteria.where("county").regex(String.format("%s.*", filter.getRegexAddress())),
                    Criteria.where("city").regex(String.format("%s.*", filter.getRegexAddress()))
            );
        }
        DataTablesOutput<City> all = cityRepository.findAll(filter, criteria);
        all.setDraw(0);
        return all;
    }
    
    @Override
    public void deletes(String... id) {
        cityRepository.deleteAllByIdIn(id);
    }
    
    @Override
    public void insert(CityFilter cityFilter, CheckError checkError) {
        cityRepository.insert(cityFilter.get());
    }
    
    @Override
    public void update(CityFilter filter, CheckError checkError) {
        City city = cityRepository.findOne(filter.getId());
        if (ObjectUtils.isEmpty(city)) {
            checkError.putError("更新对象异常");
            return;
        }
        city = filter.update(city);
        cityRepository.save(city);
    }
    
    @Override
    public void inserts(Collection<City> citySet) {
        cityRepository.insert(citySet);
    }
    
    @Override
    public void deleteAll() {
        cityRepository.deleteAll();
    }
    
    @Override
    public List<City> searchForCity(String q, int size) {
        return cityRepository.queryForCity(q, size);
    }
    @Override
    public List<City> searchForCity(String q) {
        return cityRepository.queryForCity(q, -1);
    }
    
    @Override
    public City findById(String id) {
        return cityRepository.findOne(id);
    }
}
