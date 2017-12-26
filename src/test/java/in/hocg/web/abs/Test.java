package in.hocg.web.abs;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by hocgin on 2017/11/26.
 * email: hocgin@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {
    @Autowired
    IService iService;
    
    @org.junit.Test
    public void testMail2() {
        iService.test();
    }
    
}
