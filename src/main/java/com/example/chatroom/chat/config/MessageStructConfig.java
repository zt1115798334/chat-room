package com.example.chatroom.chat.config;

import com.example.chatroom.chat.code.MessageTypeCode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/18 14:57
 * description: 消息传递结构配置
 */
public class MessageStructConfig {

    private MessageTypeCode key;

    private Object value;

    private Object temp;



    public Object getTemp() {
        return temp;
    }

    public void setTemp(Object temp) {
        this.temp = temp;
    }

    public MessageTypeCode getKey() {
        return key;
    }

    public void setKey(MessageTypeCode key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
