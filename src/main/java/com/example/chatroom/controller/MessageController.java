package com.example.chatroom.controller;

import com.example.chatroom.entity.SysUser;
import com.example.chatroom.model.ChatMessage;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/14 13:08
 * description:
 */
@Controller
public class MessageController {

    //注入SimpMessagingTemplate 用于点对点消息发送
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sendPublicMessage")
    //这里是客户端发送消息对应的路径，等于configureMessageBroker中配置的setApplicationDestinationPrefixes + 这路径即 /app/sendPublicMessage
    @SendTo("/topic/public") //也可以使用 messagingTemplate.convertAndSend(); 推送
    public ChatMessage sendPublicMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }


    @MessageMapping("/sendPrivateMessage")
    //这里是客户端发送消息对应的路径，等于configureMessageBroker中配置的setApplicationDestinationPrefixes + 这路径即 /app/sendPrivateMessage
    public void sendPrivateMessage(@Payload ChatMessage msg) {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        System.out.println("user.getUserName() = " + user.getUserName());
        msg.setSender("admin");
        //将消息推送到指定路径上
        messagingTemplate.convertAndSendToUser(msg.getReceiver(), "topic/chat", msg);
    }
}
