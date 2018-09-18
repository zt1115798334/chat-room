package com.example.chatroom.config;

import com.example.chatroom.entity.SysUser;
import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/18 10:55
 * description:
 */
@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator {
    /**
     * 修改握手,就是在握手协议建立之前修改其中携带的内容
     * @param sec
     * @param request
     * @param response
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        SysUser sysUser = (SysUser)SecurityUtils.getSubject().getPrincipal();
        System.out.println("SpringWebSocketConfig.modifyHandshake");
        sec.getUserProperties().put("user", sysUser);
        //sec.getUserProperties().put("name", "wb");
        super.modifyHandshake(sec, request, response);
    }
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        //这个对象说一下，貌似只有服务器是tomcat的时候才需要配置,具体我没有研究
        return new ServerEndpointExporter();
    }

}
