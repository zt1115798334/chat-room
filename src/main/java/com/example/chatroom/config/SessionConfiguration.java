package com.example.chatroom.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/14 17:51
 * description:
 */
@Configuration
public class SessionConfiguration extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        String name = HttpSession.class.getName();
        System.out.println("httpSession.getName() = " + name);
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
}
