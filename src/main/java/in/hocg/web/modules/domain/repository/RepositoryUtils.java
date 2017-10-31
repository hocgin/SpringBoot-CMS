package in.hocg.web.modules.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Created by hocgin on 2017/10/31.
 * email: hocgin@gmail.com
 */
public class RepositoryUtils {
    @Autowired
    private MongoTemplate mongoTemplate;
    
}
