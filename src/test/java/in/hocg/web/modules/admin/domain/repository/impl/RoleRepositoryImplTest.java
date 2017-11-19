package in.hocg.web.modules.admin.domain.repository.impl;

import in.hocg.web.modules.admin.domain.repository.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by hocgin on 2017/11/9.
 * email: hocgin@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryImplTest {
    
    @Autowired
    RoleRepository repository;
    
    @Test
    public void findAllByPermissionId() throws Exception {
        repository.removePermissionForAllRole("59f8a29c34f319a76b3c99f9");
    }
    
}