package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.MailVerify;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by hocgin on 2017/12/17.
 * email: hocgin@gmail.com
 */
public interface MailVerifyRepository extends MongoRepository<MailVerify, String> {
}
