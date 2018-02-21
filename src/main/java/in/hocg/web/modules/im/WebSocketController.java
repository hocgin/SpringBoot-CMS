package in.hocg.web.modules.im;

import com.google.gson.Gson;
import in.hocg.web.lang.utils.SecurityKit;
import in.hocg.web.modules.im.packets.MessagePacket;
import in.hocg.web.modules.im.packets.accept.AcceptFeedback;
import in.hocg.web.modules.im.packets.accept.AcceptType;
import in.hocg.web.modules.im.processor.MessageProcessor;
import in.hocg.web.modules.im.processor.UserToUserProcessor;
import in.hocg.web.modules.system.service.kit.NSNotifyService;
import in.hocg.web.modules.system.service.notify.NotifyService;
import in.hocg.web.modules.system.service.notify.UserNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by hocgin on 2017/12/18.
 * email: hocgin@gmail.com
 */
@Controller
public class WebSocketController {
    public static final String QUEUE_MESSAGE = "/queue/messages";
    
    private NSNotifyService nsNotifyService;
    private UserNotifyService userNotifyService;
    private NotifyService notifyService;
    private Gson gson;
    private Map<Byte, MessageProcessor<?>> processors = new HashMap<>();
    
    @Autowired
    public WebSocketController(NSNotifyService nsNotifyService,
                               UserNotifyService userNotifyService,
                               NotifyService notifyService,
                               Gson gson,
    
                               UserToUserProcessor userToUserProcessor) {
        this.nsNotifyService = nsNotifyService;
        this.userNotifyService = userNotifyService;
        this.notifyService = notifyService;
        this.gson = gson;
        
        processors.put(AcceptType.USER_TO_USER, userToUserProcessor);
    }
    
    
    @MessageMapping("/reply") // 服务端接收入口
    public void reply(String value, Principal principal) throws Exception {
        MessagePacket packet = gson.fromJson(value, MessagePacket.class);
        MessageProcessor<?> processor = processors.get(packet.getType());
        if (Objects.nonNull(processor)) {
            processor.process(value);
            return;
        }
        // 处理未知消息
        nsNotifyService.sendMessageToUser(principal.getName(), "无法进行处理");
    }
    
    /**
     * 消息被接收后回馈
     * @param value
     * @param principal
     * @throws Exception
     */
    @MessageMapping("/messages/reply") // 服务端接收入口
    public void feedback(String value, Principal principal) throws Exception {
        AcceptFeedback feedback = gson.fromJson(value, AcceptFeedback.class);
        userNotifyService.ready(feedback.getData().getId());
    }
}
