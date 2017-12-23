package in.hocg.web.global.config.socket.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

/**
 * Created by hocgin on 2017/12/22.
 * email: hocgin@gmail.com
 */
public class StompConnectEvent implements ApplicationListener<SessionConnectEvent> {
    private static final Logger logger = LoggerFactory.getLogger(StompConnectEvent.class);
    
    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        
        logger.debug("STOM 连接 sha.getSessionId(): " + sha.getSessionId() + " sha.toNativeHeaderMap():" + sha.toNativeHeaderMap());
        
        
        //String  company = sha.getNativeHeader("company").get(0);
        //logger.debug("Connect event [sessionId: " + sha.getSessionId() +"; company: "+ company + " ]");
        
        
        // 建立 STOM 连接后
    }
}
