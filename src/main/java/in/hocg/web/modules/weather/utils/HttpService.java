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
    
    /**
     * 使用Ip 获取定位
     * @param ip
     * @return
     */
    public ResponseEntity<String> getLocation(String ip) {
        String url = String.format("https://api.map.baidu.com/location/ip?ip=%s&ak=C159d6c7abc8fa73baf7604aa862d0b1&coor=bd09ll", ip);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return null;
        }
        return responseEntity;
    }
    
    
    private String getAppId() {
        return "572557aadb9c8243cbce1d8cfa0d43eb";
    }
}
