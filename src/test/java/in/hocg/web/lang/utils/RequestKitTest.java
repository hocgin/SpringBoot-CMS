package in.hocg.web.lang.utils;

import in.hocg.web.modules.weather.body.Weather;
import in.hocg.web.modules.weather.domain.RequestCache;
import in.hocg.web.modules.weather.domain.repository.RequestCacheRepository;
import org.junit.Test;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hocgin on 2017/11/14.
 * email: hocgin@gmail.com
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class RequestKitTest {
    @Autowired
    HttpServletRequest request;
    @Autowired
    RequestCacheRepository requestCacheRepository;
    
    @Test
    public void getClientIP() throws Exception {
        System.out.println(RequestKit.getClientIP(request));
    }
    
    @Test
    public void testRequest() {
        requestCacheRepository.insert(new RequestCache());
    }
}