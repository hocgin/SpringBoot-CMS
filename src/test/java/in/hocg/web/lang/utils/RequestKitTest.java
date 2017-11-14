package in.hocg.web.lang.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    @Test
    public void getClientIP() throws Exception {
        System.out.println(RequestKit.getClientIP(request));
    }
    
}