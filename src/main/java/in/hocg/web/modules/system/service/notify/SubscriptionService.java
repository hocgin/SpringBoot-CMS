package in.hocg.web.modules.system.service.notify;

import in.hocg.web.modules.system.domain.notify.Subscription;
import in.hocg.web.modules.system.domain.notify.TargetType;
import in.hocg.web.modules.system.domain.user.User;

import java.util.List;

/**
 * Created by hocgin on 2017/12/19.
 * email: hocgin@gmail.com
 */
public interface SubscriptionService {
    
    /**
     * 订阅
     */
    void subscribe(User user, String target, TargetType targetType, String reason);
    
    /**
     * 取消订阅
     *
     * @param user
     * @param target
     * @param targetType
     */
    void unsubscribe(User user, String target, TargetType targetType);
    
    /**
     * 获取用户的订阅列表
     * @param user
     * @return
     */
    List<Subscription> findAll(User user);
}
