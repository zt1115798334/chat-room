package com.example.chatroom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/14 13:06
 * description:
 */
//@Configuration
//@EnableWebSocketMessageBroker
public class WebSocketConfig1 implements WebSocketMessageBrokerConfigurer {

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        //允许客户端使用socketJs方式访问，访问点为ws，允许跨域
//        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//
//        //订阅广播 Broker（消息代理）名称
//        registry.enableSimpleBroker("/topic"); // Enables a simple in-memory broker
//        //全局使用的订阅前缀（客户端订阅路径上会体现出来）
//        registry.setApplicationDestinationPrefixes("/app/");
//        //点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
//        registry.setUserDestinationPrefix("/user");
//    }
//
//    @Override
//    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
//        registry.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
//            @Override
//            public WebSocketHandler decorate(final WebSocketHandler handler) {
//                return new WebSocketHandlerDecorator(handler) {
//
//                    @Override
//                    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
//                        // 客户端与服务器端建立连接后，此处记录谁上线了
//                        String username = session.getPrincipal().getName();
//                        System.out.println("online: " + username);
//                        super.afterConnectionEstablished(session);
//                    }
//
//                    @Override
//                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
//                        // 客户端与服务器端断开连接后，此处记录谁下线了
//                        String username = session.getPrincipal().getName();
//                        System.out.println("offline: " + username);
//                        super.afterConnectionClosed(session, closeStatus);
//                    }
//                };
//            }
//        });
//
//    }

}
