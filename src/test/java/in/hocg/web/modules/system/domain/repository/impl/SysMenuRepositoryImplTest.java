package in.hocg.web.modules.system.domain.repository.impl;

import in.hocg.web.modules.system.domain.repository.SysMenuRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by hocgin on 2017/11/18.
 * email: hocgin@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysMenuRepositoryImplTest {
    @Autowired
    SysMenuRepository repository;
    @Test
    public void findAllOrderByPathDescAndLocationAsc() throws Exception {
    }
    
}