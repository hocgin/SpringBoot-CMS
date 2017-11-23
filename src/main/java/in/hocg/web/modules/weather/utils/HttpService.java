package in.hocg.web.modules.weather.utils;

import in.hocg.web.modules.weather.body.Forecast;
import in.hocg.web.modules.weather.body.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by hocgin on 2017/11/23.
 * email: hocgin@gmail.com
 */
@Component
public class HttpService {
    private RestTemplate restTemplate;
    @Autowired
    public HttpService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public ResponseEntity<Weather> getCurrentWeather(String unit, String lang, String lon, String lat) {
        String url = OpenWeatherMap.weather(getAppId(), unit, lang, lat, lon);
        ResponseEntity<Weather> responseEntity = restTemplate.getForEntity(url, Weather.class);
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return null;
        }
        return responseEntity;
    }
    public ResponseEntity<Forecast> getForecastWeather(String unit, String lang, String lon, String lat) {
        String url = OpenWeatherMap.forecast(getAppId(), unit, lang, lat, lon);
        ResponseEntity<Forecast> responseEntity = restTemplate.getForEntity(url, Forecast.class);
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return null;
        }
        return responseEntity;
    }
    
    
    
    
    private String getAppId() {
        return "5cc4a35dddbcda5e26e06a47868d7291";
    }
}
