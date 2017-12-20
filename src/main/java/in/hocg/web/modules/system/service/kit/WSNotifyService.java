package in.hocg.web.modules.system.service.kit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by hocgin on 2017/12/20.
 * email: hocgin@gmail.com
 */
@Service
public class WSNotifyService {
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    public WSNotifyService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
    
}
