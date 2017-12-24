package in.hocg.web.modules.im.processor;

import com.google.gson.Gson;
import in.hocg.web.modules.im.packets.accept.im.UserToUserAccept;
import in.hocg.web.modules.im.packets.accept.im.common.Mine;
import in.hocg.web.modules.im.packets.accept.im.common.To;
import in.hocg.web.modules.im.packets.transmit.im.UserToUserTransmit;
import in.hocg.web.modules.system.domain.user.User;
import in.hocg.web.modules.system.service.UserService;
import in.hocg.web.modules.system.service.kit.NSNotifyService;
import in.hocg.web.modules.system.service.notify.NotifyService;
import in.hocg.web.modules.system.service.notify.UserNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by hocgin on 2017/12/23.
 * email: hocgin@gmail.com
 */
@Component
public class UserToUserProcessor extends MessageProcessor<UserToUserAccept> {
    @Autowired
    private Gson gson;
    @Autowired
    private NSNotifyService nsNotifyService;
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
        this.nsNotifyService.sendMessageToUser(toUser.getUsername(), new UserToUserTransmit().full(sender, content));
    }
}
