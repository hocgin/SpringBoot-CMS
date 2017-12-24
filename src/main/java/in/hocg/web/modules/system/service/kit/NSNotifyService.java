package in.hocg.web.modules.system.service.kit;

import in.hocg.web.modules.im.packets.transmit.UpdateAnnouncementTransmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by hocgin on 2017/12/20.
 * email: hocgin@gmail.com
 */
@Service
public class NSNotifyService {
    // 更新公告
    public static final String TOPIC_ANNOUNCEMENT = "/topic/update/announcement";
    
    
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    public NSNotifyService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
    
    public void sendAnnouncement(String content) {
        simpMessagingTemplate.convertAndSend(TOPIC_ANNOUNCEMENT, new UpdateAnnouncementTransmit().full(content));
    }
}
