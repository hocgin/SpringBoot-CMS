package in.hocg.web.global.config.socket.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class CustomHttpSessionListener implements HttpSessionListener {

	private static final Logger logger = LoggerFactory.getLogger(CustomHttpSessionListener.class);

	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		logger.info("Session 创建 Session Creation event called!! : " + httpSessionEvent.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		logger.info("Session 销毁 Session Destroyed event called!! : " + httpSessionEvent.getSession().getId());
	}
}
