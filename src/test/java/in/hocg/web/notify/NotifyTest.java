package in.hocg.web.notify;

import in.hocg.web.modules.system.domain.notify.Notify;
import in.hocg.web.modules.system.domain.notify.TargetType;
import in.hocg.web.modules.system.domain.notify.UserNotify;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.service.UserService;
import in.hocg.web.modules.system.service.notify.NotifyService;
import in.hocg.web.modules.system.service.notify.SubscriptionService;
import in.hocg.web.modules.system.service.notify.UserNotifyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by hocgin on 2017/12/19.
 * email: hocgin@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NotifyTest {
    @Autowired
    SubscriptionService subscriptionService;
    @Autowired
    NotifyService notifyService;
    @Autowired
    UserNotifyService userNotifyService;
    @Autowired
    UserService userService;
    
    
    @Test
    public void subscription() {
        User user = userService.findOne("5a375d12ff286867726ec886");
        /**
         * User(5a375d12ff286867726ec886) 订阅文章(Post, 111) 的行为([comment])
         */
        subscriptionService.subscribe(user, "111", TargetType.Post, "like_post");
    }
    
    @Test
    public void notifyRemind() {
        User user = userService.findOne("5a375d12ff286867726ec886");
        User sender = userService.findOne("5a375d12ff286867726ec887");
        notifyService.createRemind("111", TargetType.Post, "comment",
                String.format("文章[111]被%s评论了", sender.getNickname()),
                sender);
        userNotifyService.pullRemind(user);
    }
    
    @Test
    public void unsubscribe() {
        User user = userService.findOne("5a375d12ff286867726ec886");
        subscriptionService.unsubscribe(user, "111", TargetType.Post);
    }
    
    @Test
    public void read() {
        User user = userService.findOne("5a375d12ff286867726ec886");
        String[] userNotifyIDs = userNotifyService.getUserNotify(user).stream()
                .map(UserNotify::getNotify)
                .map(Notify::getId)
                .toArray(String[]::new);
        userNotifyService.read(user.getId(), userNotifyIDs);
    }
    
    @Test
    public void notifyAnnounce() {
        User user = userService.findOne("5a375d12ff286867726ec886");
        User sender = userService.findOne("5a375d12ff286867726ec887");
        notifyService.createAnnounce("XX这是一条公告", sender);
        userNotifyService.pullAnnounce(user);
    }
    
    @Test
    public void notifyMessage() {
        User user = userService.findOne("5a375d12ff286867726ec886");
        User sender = userService.findOne("5a375d12ff286867726ec887");
        notifyService.createMessage("这是一条私信", sender, user);
    }
}
