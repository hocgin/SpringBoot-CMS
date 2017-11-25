package in.hocg.web.modules.weather.utils;

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
    
    public ResponseEntity<String> getCurrentWeather(String param) {
        String url = OpenWeatherMap.weather(getAppId(), param);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return null;
        }
        return responseEntity;
    }
    public ResponseEntity<String> getForecastWeather(String param) {
        String url = OpenWeatherMap.forecast(getAppId(), param);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return null;
        }
        return responseEntity;
    }
    
    
    
    
    private String getAppId() {
        return "5cc4a35dddbcda5e26e06a47868d7291";
    }
}
