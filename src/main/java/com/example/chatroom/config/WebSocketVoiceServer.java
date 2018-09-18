package com.example.chatroom.config;

import com.alibaba.fastjson.JSON;
import com.example.chatroom.chat.code.MessageTypeCode;
import com.example.chatroom.chat.config.FiveResponseConfig;
import com.example.chatroom.chat.config.MessageStructConfig;
import com.example.chatroom.chat.config.OneReadyResponseConfig;
import com.example.chatroom.chat.service.ChatService;
import com.example.chatroom.chat.storage.FiveHomeStorage;
import com.example.chatroom.chat.storage.MessageSendLockStorage;
import com.example.chatroom.chat.storage.PersonalSessionStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/18 11:00
 * description:
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint(value = "/webSocketVoiceServer", configurator = WebSocketConfig.class)
@Component
public class WebSocketVoiceServer {

    protected final Logger log = LoggerFactory.getLogger(WebSocketVoiceServer.class);

    @OnOpen
    public void onOpen(Session session) {
        String id = session.getId();
        try {
            PersonalSessionStorage.addSessionById(id, session);
            String ret = getRet(MessageTypeCode.PERSONAL_ID, id);
            MessageSendLockStorage.addSendText(id, ret);
        } catch (Throwable e) {
            PersonalSessionStorage.delSessionById(id);
            try {
                session.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

    }

    @OnClose
    public void onClose(Session session) {
        try {
            closeDialogue(session);
            PersonalSessionStorage.delSessionById(session.getId());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        try {
            MessageStructConfig struct = JSON.parseObject(message, MessageStructConfig.class);
            switch (struct.getKey()) {
                case QUERY_ID://搜索个人
                    MessageSendLockStorage.addSendText(session.getId(), getRet(MessageTypeCode.QUERY_ID, ChatService.queryId(session.getId(), struct.getValue().toString())));
                    break;
                case READY_FOR_ONE://一对一准备
                    String remoteId = ChatService.readyForOne(session.getId(), struct.getValue().toString());
                    MessageSendLockStorage.addSendText(remoteId, getRet(MessageTypeCode.READY_FOR_ONE, session.getId()));//给对方发送准备请求
                    break;
                case READY_FOR_ONE_RESPONSE://一对一准备响应
                    OneReadyResponseConfig ret = ChatService.readyForOneResponse(session.getId(), Boolean.parseBoolean(struct.getValue().toString()));
                    MessageSendLockStorage.addSendText(ret.getOfferId(), getRet(MessageTypeCode.READY_FOR_ONE_RESPONSE, JSON.toJSONString(ret)));
                    break;
                case SIGNALLING_OFFER://接收发送到服务端的信令
                    OneReadyResponseConfig offerRet = ChatService.signallingOffer(session.getId());
                    if (offerRet.isStatus()) {//转发信令
                        MessageSendLockStorage.addSendText(offerRet.getAnswerId(), getRet(MessageTypeCode.SIGNALLING_ONE_ANSWER, JSON.toJSONString(struct.getValue())));
                        break;
                    } else {//一对一找不到对话准备
                        //查找是否有指定发送人
                        if (struct.getTemp() != null) {
                            //检查指定发送人是否与当前发送人在同一个房间内
                            String responseUserHomeId = FiveHomeStorage.getHomeIdByUserId(struct.getTemp().toString());
                            String sendHomeId = FiveHomeStorage.getHomeIdByUserId(session.getId());
                            if (responseUserHomeId != null && sendHomeId != null && responseUserHomeId.equals(sendHomeId)) {//正常执行
                                FiveResponseConfig fiveTemp = new FiveResponseConfig();
                                fiveTemp.setSendUserId(session.getId());
                                fiveTemp.setMsg(struct.getValue().toString());
                                MessageSendLockStorage.addSendText(struct.getTemp().toString(), getRet(MessageTypeCode.SIGNALLING_FIVE_ANSWER, JSON.toJSONString(fiveTemp)));
                                break;
                            }
                        }
                        MessageSendLockStorage.addSendText(offerRet.getAnswerId(), getRet(MessageTypeCode.READY_FOR_ONE_RESPONSE, JSON.toJSONString(offerRet)));
                        break;
                    }
                case ONE_CHANNEL_CLOSE://发送了一对一通道关闭
                    closeDialogue(session);
                    break;
                case CREATE_GROUP_FIVE://创建五人群组房间
                    String homeId = ChatService.createHome();
                    ChatService.addHome(session.getId(), homeId);//自己立马加入到这个房间中
                    MessageSendLockStorage.addSendText(session.getId(), getRet(MessageTypeCode.CREATE_GROUP_FIVE, homeId));
                    break;
                case EXIT_GROUP_FIVE://退出五人群组房间
                    closeDialogue(session);
                    break;
                case QUERY_GROUP_FIVE://搜索并加入五人群组房间
                    String queryHomeId = struct.getValue().toString();
                    boolean addHomeRet = ChatService.addHome(session.getId(), queryHomeId);
                    FiveResponseConfig fiveTemp = null;
                    if (addHomeRet) {//有人进入房间,给房间内其他用户发送消息
                        ArrayList<String> userIds = FiveHomeStorage.getHomeUsersNotThis(queryHomeId, session.getId());
                        for (String userId : userIds) {//每次的信令,发给所有人一份
                            MessageSendLockStorage.addSendText(userId, getRet(MessageTypeCode.GROUP_FIVE_ADD_USER, session.getId()));
                        }
                        fiveTemp = new FiveResponseConfig();
                        fiveTemp.setSendUserId(session.getId());
                        fiveTemp.setMsg(struct.getValue().toString());
                        fiveTemp.setHomeId(queryHomeId);
                        ArrayList<String> youUserIds = FiveHomeStorage.getHomeUsersNotThis(queryHomeId, session.getId());
                        fiveTemp.setUserIds(youUserIds.toArray(new String[youUserIds.size()]));
                    }
                    MessageSendLockStorage.addSendText(session.getId(), getRet(MessageTypeCode.QUERY_GROUP_FIVE, fiveTemp != null ? JSON.toJSONString(fiveTemp) : null));//返回房间内其他人的id
                    break;
                default:
                    break;
            }
        } catch (Throwable e) {
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


    /**
     * 删除对话,包括群组 个人
     */
    public void closeDialogue(Session session) {
        //并且给一对一对话 对方 发送关闭对话的通知,如果有在对话
        String closeRemoteId = ChatService.oneChannelClose(session.getId());
        MessageSendLockStorage.addSendText(closeRemoteId, getRet(MessageTypeCode.ONE_CHANNEL_CLOSE, true));
        //先给房内其他人发送退出信息
        String homeId = FiveHomeStorage.getHomeIdByUserId(session.getId());
        if (homeId != null) {
            ArrayList<String> userIds = FiveHomeStorage.getHomeUsersNotThis(homeId, session.getId());
            for (String userId : userIds) {
                MessageSendLockStorage.addSendText(userId, getRet(MessageTypeCode.EXIT_GROUP_FIVE, session.getId()));
            }
        }
        ChatService.exitHome(session.getId());
    }

    /**
     * 发送消息结构
     *
     * @param key
     * @param msg
     * @return
     */
    private String getRet(MessageTypeCode key, Object msg) {
        MessageStructConfig struct = new MessageStructConfig();
        struct.setValue(msg);
        struct.setKey(key);
        return JSON.toJSONString(struct);
    }
}
