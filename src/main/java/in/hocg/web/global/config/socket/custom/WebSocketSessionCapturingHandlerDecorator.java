package in.hocg.web.global.config.socket.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

public class WebSocketSessionCapturingHandlerDecorator extends WebSocketHandlerDecorator {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketSessionCapturingHandlerDecorator.class);

	public WebSocketSessionCapturingHandlerDecorator(WebSocketHandler delegate) {
		super(delegate);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info("连接后(afterConnectionEstablished)");
		super.afterConnectionEstablished(session);
	}
	
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		logger.info("处理信息(handleMessage)");
		super.handleMessage(session, message);
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		logger.info("处理错误(handleTransportError)");
		super.handleTransportError(session, exception);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		logger.info("连接关闭(afterConnectionClosed)");
		super.afterConnectionClosed(session, closeStatus);
	}
}
