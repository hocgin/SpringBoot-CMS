package in.hocg.web.modules.system.service.impl;

import in.hocg.web.lang.CheckError;
import in.hocg.web.lang.utils.SecurityKit;
import in.hocg.web.modules.base.Base2Service;
import in.hocg.web.modules.base.body.Page;
import in.hocg.web.modules.im.filter.ChatLogQueryFilter;
import in.hocg.web.modules.system.domain.notify.Notify;
import in.hocg.web.modules.system.domain.notify.UserNotify;
import in.hocg.web.modules.system.domain.repository.UserNotifyRepository;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.filter.UserNotifyQueryFilter;
import in.hocg.web.modules.system.service.UserService;
import in.hocg.web.modules.system.service.notify.NotifyService;
import in.hocg.web.modules.system.service.notify.SubscriptionService;
import in.hocg.web.modules.system.service.notify.UserNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hocgin on 2017/12/19.
 * email: hocgin@gmail.com
 */
@Service
public class UserNotifyServiceImpl extends Base2Service<UserNotify, String, UserNotifyRepository> implements UserNotifyService {
    private SubscriptionService subscriptionService;
    
    private NotifyService notifyService;
    
    private UserService userService;
    
    @Autowired
    public UserNotifyServiceImpl(@Lazy SubscriptionService subscriptionService,
                                 UserService userService,
                                 NotifyService notifyService) {
        this.subscriptionService = subscriptionService;
        this.notifyService = notifyService;
        this.userService = userService;
    }
    
    /**
     * 同步 公告
     *
     * @param user the user
     */
    @Override
    public void pullAnnounce(User user) {
        String[] notifyIDs = notifyService.findAllByType(Notify.Type.Announce.name()).stream()
                .map(Notify::getId)
                .toArray(String[]::new);
        UserNotify userNotify = repository.findTopByUser_IdAndNotify_IdInOrderByCreatedAtDesc(user.getId(),
                notifyIDs);
        Date lastAt;
        if (Objects.nonNull(userNotify)) {
            lastAt = userNotify.getCreatedAt();
        } else {
            lastAt = new Date();
            lastAt.setTime(0);
        }
        List<Notify> notifies = notifyService.findAllByType(Notify.Type.Announce, lastAt);
        if (!notifies.isEmpty()) {
            List<Notify> singletonList = Collections.singletonList(notifies.get(0));
            createUserNotify(user, singletonList);
        }
        
    }
    
    /**
     * 同步订阅信息
     *
     * @param user the user
     */
    @Override
    public void pullRemind(User user) {
        List<Notify> notifies = subscriptionService.findAll(user).stream()
//                .filter(subscription -> {
//                    // - 用户配置进行筛选
//                })
                .map(subscription -> {
                    String[] notifyIDs = notifyService.findAll(subscription.getTarget(), subscription.getTargetType(), subscription.getAction(), subscription.getCreatedAt()).stream()
                            .map(Notify::getId)
                            .toArray(String[]::new);
                    if (notifyIDs.length <= 0) {
                        return Collections.<Notify>emptyList();
                    }
                    // 最后订阅通知时间
                    UserNotify userNotify = repository.findTopByUser_IdAndNotify_IdInOrderByCreatedAtDesc(user.getId(), notifyIDs);
                    Date lastDate = subscription.getCreatedAt();
                    if (Objects.nonNull(userNotify)) {
                        lastDate = userNotify.getCreatedAt();
                    }
                    // 查询出订阅的消息
                    return notifyService.findAll(subscription.getTarget(),
                            subscription.getTargetType(),
                            subscription.getAction(),
                            lastDate);
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
        createUserNotify(user, notifies);
        
    }
    
    @Override
    public List<UserNotify> createUserNotify(User user, List<Notify> notifies) {
        List<UserNotify> userNotifies = notifies.stream()
                .map(notify -> new UserNotify(user, notify))
                .collect(Collectors.toList());
        return repository.insert(userNotifies);
    }
    
    @Override
    public List<UserNotify> getUserNotify(String userID) {
        User user = userService.findOne(userID);
        if (Objects.isNull(user)) {
            return Collections.emptyList();
        }
        return getUserNotify(user);
    }
    
    @Override
    public Page<UserNotify> pager(UserNotifyQueryFilter filter, String userID, CheckError checkError) {
        User user = userService.findOne(userID);
        if (Objects.isNull(user)) {
            checkError.putError("异常");
            return null;
        }
        
        // 判断是否有更新
        pullAnnounce(user);
        pullRemind(user);
        
        return repository.pageByUserAndNotifyTypeOrderByCreatedAtDesc(filter.getPage(), filter.getSize(), userID, filter.getType());
    }
    
    @Override
    public List<User> getMostRecentContact(String userID) {
        return repository.getMostRecentContact(userID)
                .stream()
                .limit(5)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<UserNotify> getChatLog(ChatLogQueryFilter filter) {
        String[] IDs = new String[]{SecurityKit.iUser().getId(), filter.getId()};
        return repository.pageByUserIDsAndNotifySenderIDsAndNotifyTypeOrderByCreatedAtDesc(filter.getPage(),
                filter.getSize(),
                IDs,
                IDs,
                Notify.Type.Message.name()
        );
    }
    
    @Override
    public List<UserNotify> findAllUnreadyUserNotifyOrderByCreatedAtDesc(String userID, Notify.Type type) {
        return repository.findAllUnreadyUserNotifyOrderByCreatedAtDesc(userID, type);
    }
    
    
    /**
     * 未读
     *
     * @param user
     * @return
     */
    @Override
    public List<UserNotify> getUserNotify(User user) {
        return repository.findAllByUser_IdIsAndReadIsOrderByCreatedAtDesc(user.getId(), false);
    }
    
    
    @Override
    public void ready(String... userNotifyIDs) {
        List<UserNotify> userNotifyList = repository.findAllByIdIn(userNotifyIDs);
        userNotifyList.forEach(userNotify -> {
            userNotify.setRead(true);
        });
        repository.save(userNotifyList);
    }
}
