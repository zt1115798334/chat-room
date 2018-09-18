package com.example.chatroom.config;

import com.example.chatroom.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/18 11:00
 * description:
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint(value = "/webSocketServer", configurator = WebSocketConfig.class)
@Component
public class WebSocketServer {

    protected final Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    private static int onlineCount = 0;
    private static ConcurrentHashMap<String, WebSocketServer> webSocketSet = new ConcurrentHashMap<>();
    private Session session;
    //todo 这里需要一个变量来接收shiro中登录的人信息
    private SysUser sysUser;
    private final String id = "0";

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        //注入userService
        //设置用户
        this.sysUser = (SysUser) session.getUserProperties().get("user");
        String userId = String.valueOf(sysUser.getId());
        webSocketSet.put(userId, this);
        System.out.println("sysUser.getUserName() = " + sysUser.getUserName());
        addOnlineCount();
        log.info("用户" + userId + "加入！当前在线人数为" + getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("webSocket IO异常");
        }

    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }


    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("来自客户端的消息:" + message);
        //可以自己约定字符串内容，比如 内容|0 表示信息群发，内容|X 表示信息发给id为X的用户
        String sendMessage = message.split("[|]")[0];
        System.out.println("sendMessage = " + sendMessage);
        String sendUserId = message.split("[|]")[1];
        System.out.println("sendUserId = " + sendUserId);
        try {
            if (sendUserId.equals("0"))
                sendToAll(sendMessage);
            else
                sendToUser(sendMessage, sendUserId);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送信息给指定ID用户，如果用户不在线则返回不在线信息给自己
     *
     * @param message
     * @param sendUserId
     * @throws IOException
     */
    private void sendToUser(String message, String sendUserId) throws IOException {
        if (webSocketSet.get(sendUserId) != null) {
            if (!id.equals(sendUserId))
                webSocketSet.get(sendUserId).sendMessage("用户" + id + "发来消息：" + " <br/> " + message);
            else
                webSocketSet.get(sendUserId).sendMessage(message);
        } else {
            //如果用户不在线则返回不在线信息给自己
            sendToUser("当前用户不在线", id);
        }
    }


    /**
     * 发送信息给所有人
     *
     * @param message
     * @throws IOException
     */
    private void sendToAll(String message) throws IOException {
        for (String key : webSocketSet.keySet()) {
            webSocketSet.get(key).sendMessage(message);
        }
    }


    private static synchronized int getOnlineCount() {
        return WebSocketServer.onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}
