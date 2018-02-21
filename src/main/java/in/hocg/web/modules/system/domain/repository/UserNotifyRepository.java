package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.notify.UserNotify;
import in.hocg.web.modules.system.domain.repository.custom.UserNotifyRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by hocgin on 2017/12/19.
 * email: hocgin@gmail.com
 */
public interface UserNotifyRepository extends MongoRepository<UserNotify, String>,
        UserNotifyRepositoryCustom {
    /**
     * 查询某用户最近接收的一条通知
     * @param userID
     * @param notifyIDs
     * @return
     */
    UserNotify findTopByUser_IdAndNotify_IdInOrderByCreatedAtDesc(String userID, String... notifyIDs);
    
    /**
     * 查询某用户所有未读的通知
     * @param userID
     * @param read
     * @return
     */
    List<UserNotify> findAllByUser_IdIsAndReadIsOrderByCreatedAtDesc(String userID, boolean read);
    
    List<UserNotify> findAllByUser_IdIsAndNotify_IdIn(String userID, String... notifyIDs);
    
    List<UserNotify> findAllByIdIn(String... userNotifyIDs);
    
}
