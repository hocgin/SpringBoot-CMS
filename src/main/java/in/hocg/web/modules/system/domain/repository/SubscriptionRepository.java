package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.notify.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by hocgin on 2017/12/19.
 * email: hocgin@gmail.com
 */
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    void deleteAllByUser_IdIsAndTargetIsAndTargetTypeIs(String userID, String target, String targetType);
    
    List<Subscription> findAllByUser_Id(String userID);
}
