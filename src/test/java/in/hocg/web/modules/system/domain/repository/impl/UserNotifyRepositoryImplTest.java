package in.hocg.web.modules.system.domain.repository.impl;

import in.hocg.web.modules.system.domain.repository.UserNotifyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by hocgin on 2017/12/21.
 * email: hocgin@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserNotifyRepositoryImplTest {
    @Autowired
    private UserNotifyRepository repository;
    
    @Test
    public void getTestSender() {
        List contact = repository.getMostRecentContact("5a3a02983d79b0e134df43c3");
        System.out.println(contact);
    }
}