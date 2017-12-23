package in.hocg.web.modules.im.processor;

import com.google.gson.Gson;
import in.hocg.web.modules.im.packets.transmit.UserToUserTransmit;
import in.hocg.web.modules.im.packets.accept.common.Mine;
import in.hocg.web.modules.im.packets.accept.common.To;
import in.hocg.web.modules.im.packets.accept.UserToUserAccept;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.service.UserService;
import in.hocg.web.modules.system.service.notify.NotifyService;
import in.hocg.web.modules.system.service.notify.UserNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import static in.hocg.web.modules.im.WebSocketController.QUEUE_MESSAGE;

/**
 * Created by hocgin on 2017/12/23.
 * email: hocgin@gmail.com
 */
@Component
public class UserToUserProcessor extends MessageProcessor<UserToUserAccept> {
    @Autowired
    private Gson gson;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private UserNotifyService userNotifyService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private UserService userService;
    
    @Override
    public void process(String value) throws Exception {
        UserToUserAccept packet = gson.fromJson(value, UserToUserAccept.class);
        Mine mine = packet.getData().getMine(); // 发送者
        To to = packet.getData().getTo(); // 接受者
        String content = mine.getContent();
        User sender = userService.findOne(mine.getId());
        User toUser = userService.findOne(to.getId());
        notifyService.createMessage(content, sender, toUser);
        this.simpMessagingTemplate.convertAndSendToUser(toUser.getUsername(), QUEUE_MESSAGE, new UserToUserTransmit().full(sender, content));
    }
}
