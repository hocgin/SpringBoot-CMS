package in.hocg.web.modules.system.domain.repository;

import in.hocg.web.modules.system.domain.notify.Notify;
import org.springframework.data.mongodb.datatables.repository.DataTablesRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by hocgin on 2017/12/19.
 * email: hocgin@gmail.com
 */
public interface NotifyRepository extends DataTablesRepository<Notify, String>, MongoRepository<Notify, String> {
    
    /**
     * 查询某种指定订阅的所有通知
     * @param target
     * @param targetType
     * @param action
     * @param createdAt
     * @return
     */
    List<Notify> findAllByTargetIsAndTargetTypeIsAndActionIsAndCreatedAtAfter(String target, String targetType, String action, Date createdAt);
    
    /**
     *
     * @param type
     * @param createdAfter
     * @return
     */
    List<Notify> findAllByTypeIsAndCreatedAtAfterOrderByCreatedAtDesc(String type, Date createdAfter);
    
    
//    String target,
//    String targetType,
//    String action
    
    /**
     * 查询某种通知类型[私信, 订阅, 公告..]的所有通知
     * @param type
     * @return
     */
    List<Notify> findAllByTypeIs(String type);
    
    void deleteAllByIdIn(String... notifyIDs);
}
