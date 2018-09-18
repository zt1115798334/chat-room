package com.example.chatroom.chat.config;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/18 14:53
 * description: 五人群组配置
 */
public class FiveHomeConfig {

    private String homeId;//房号

    private final int maxSize = 5;//最多允许多少人加入

    private final ArrayList<String> userIds = new ArrayList<String>(maxSize);//已加入的人id

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public ArrayList<String> getUserIds() {
        return userIds;
    }

    public int getMaxSize() {
        return maxSize;
    }
}
