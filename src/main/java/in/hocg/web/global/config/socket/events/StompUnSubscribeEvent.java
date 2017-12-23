package in.hocg.web.global.config.socket.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

/**
 * Created by hocgin on 2017/12/22.
 * email: hocgin@gmail.com
 */
public class StompUnSubscribeEvent implements ApplicationListener<SessionUnsubscribeEvent> {
    private static final Logger logger = LoggerFactory.getLogger(StompUnSubscribeEvent.class);
    
    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent sessionUnSubscribeEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionUnSubscribeEvent.getMessage());
        logger.info(headerAccessor.toString());
    }
}
