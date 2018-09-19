package com.example.chatroom.chat.storage;

import com.example.chatroom.entity.SysUser;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/18 15:03
 * description: 个人session缓存类
 */
public class PersonalSysUserStorage {
    private static final ConcurrentHashMap<String, SysUser> storage = new ConcurrentHashMap<String, SysUser>();

    /**
     * 通过一个id获得这个SysUser
     *
     * @param id
     * @return
     */
    public static SysUser getSysUserById(String id) {
        return storage.get(id);
    }

    /**
     * 存储一个SysUser 到指定的id
     *
     * @param id
     * @param sio
     */
    public static void addSysUserById(String id, SysUser sio) {
        storage.put(id, sio);
    }

    /**
     * 删除指定的SysUser
     *
     * @param id
     */
    public static void delSysUserById(String id) {
        storage.remove(id);
    }
}
