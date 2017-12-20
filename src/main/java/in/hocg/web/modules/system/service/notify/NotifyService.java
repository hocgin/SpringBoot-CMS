package in.hocg.web.modules.system.service.notify;

import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.notify.Notify;
import in.hocg.web.modules.system.domain.notify.TargetType;
import in.hocg.web.modules.system.domain.user.User;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;

import java.util.Date;
import java.util.List;

/**
 * Created by hocgin on 2017/12/19.
 * email: hocgin@gmail.com
 */
public interface NotifyService {
    /**
     * 创建公告
     *
     * @param content the content
     * @param sender  the sender
     */
    void createAnnounce(String content, User sender);
    void createAnnounce(String content, String userID, CheckError checkError);
    
    /**
     * 创建一条通知
     *
     * @param target     the target
     * @param targetType the target type
     * @param action     the action
     * @param content    the content
     * @param sender     the sender
     */
    void createRemind(String target, TargetType targetType, String action, String content, User sender);
    
    /**
     * 创建一条私信
     *
     * @param content  the content
     * @param sender   the sender
     * @param receiver the receiver
     */
    void createMessage(String content, User sender, User receiver);
    
    /**
     * 查询某订阅在指定时间之后发布的所有消息
     * @param target
     * @param targetType
     * @param action
     * @param createAt
     * @return
     */
    List<Notify> findAll(String target, String targetType, String action, Date createAt);
    
    /**
     * 查询指定时间之后的所有类型[公告, 信息]
     * @param lastedAt
     * @return
     */
    List<Notify> findAllByType(Notify.Type type, Date lastedAt);
    
    List<Notify> findAllByType(String type);
    
    DataTablesOutput data(DataTablesInput filter);
    
    void deletes(String... IDs);
//    /**
//     * 获取订阅配置
//     *
//     * @param userID
//     */
//    void getSubscriptionConfig(String userID);
//
//    /**
//     * 更新订阅配置
//     *
//     * @param userID
//     */
//    void updateSubscriptionConfig(String userID);
    
}
