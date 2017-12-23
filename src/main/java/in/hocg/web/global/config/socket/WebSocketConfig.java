package in.hocg.web.global.config.socket;

import in.hocg.web.global.config.socket.custom.WebSocketSessionCapturingHandlerDecorator;
import in.hocg.web.global.config.socket.events.StompConnectEvent;
import in.hocg.web.global.config.socket.events.StompDisconnectEvent;
import in.hocg.web.global.config.socket.interceptors.HttpSessionIdHandshakeInterceptor;
import in.hocg.web.global.config.socket.interceptors.SessionKeepAliveChannelInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * Created by hocgin on 2017/12/18.
 * email: hocgin@gmail.com
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    
    /**
     * 配置入站规则
     * todo： 未配置
     * @param messages
     */
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages.nullDestMatcher().authenticated()
//                .simpDestMatchers("/**").permitAll()
//                .simpSubscribeDestMatchers("/**").permitAll()
//                .simpTypeMatchers(MESSAGE, SUBSCRIBE).denyAll()
//                .anyMessage().denyAll();
        messages.anyMessage().permitAll();
    }
    
    /**
     * 配置 客户端通道绑定
     * @param registration
     */
    @Override
    protected void customizeClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(sessionKeepAliveChannelInterceptor());
    }
    
    /**
     * 注册 STOMP 端点
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 用来接收 Client 连接
        registry.addEndpoint("/socket")
                .withSockJS()
                // 握手
                .setInterceptors(httpSessionIdHandshakeInterceptor());
    }
    
    /**
     * 配置 Message Broker (消息代理入口?)
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 订阅前缀 /topic/message
        registry.enableSimpleBroker(
                "/topic", // 广播前缀
                "/queue" // 非广播前缀
        );
        // 接收消息前缀 /app/reply
        registry.setApplicationDestinationPrefixes("/app");
    }
    
    /**
     * 配置 WebSocket 传输
     * @param registration
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(WebSocketSessionCapturingHandlerDecorator::new);
    }
    
    @Bean
    public StompConnectEvent webSocketConnectHandler() {
        return new StompConnectEvent();
    }
    
    @Bean
    public StompDisconnectEvent webSocketDisconnectHandler() {
        return new StompDisconnectEvent();
    }
    
    @Bean
    public HttpSessionIdHandshakeInterceptor httpSessionIdHandshakeInterceptor() {
        return new HttpSessionIdHandshakeInterceptor();
    }
    
    @Bean
    public SessionKeepAliveChannelInterceptor sessionKeepAliveChannelInterceptor() {
        return new SessionKeepAliveChannelInterceptor();
    }
}