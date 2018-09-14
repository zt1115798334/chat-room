package com.example.chatroom.service;

import com.example.chatroom.entity.SysUser;

import java.util.Optional;

/**
 * @author zhangtong
 * Created by on 2017/11/13
 */
public interface SysUserService {

    Optional<SysUser> findByUserAccount(String userAccount);

    Optional<SysUser> findByUserAccountAndUserPassword(String userAccount, String userPassword);
}
