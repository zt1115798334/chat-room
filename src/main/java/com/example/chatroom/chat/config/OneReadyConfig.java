package com.example.chatroom.chat.config;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/18 14:57
 * description: 一对一准备配置
 */
public class OneReadyConfig {
    private String remoteId;// 远端的ID

    private boolean status;//  远端是否已经准备好

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
