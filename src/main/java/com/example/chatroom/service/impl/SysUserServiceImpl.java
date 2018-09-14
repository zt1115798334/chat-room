package com.example.chatroom.service.impl;

import com.example.chatroom.entity.SysUser;
import com.example.chatroom.repo.SysUserRepository;
import com.example.chatroom.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author zhangtong
 * Created by on 2017/11/13
 */
@Transactional
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public Optional<SysUser> findByUserAccount(String userAccount) {
        return sysUserRepository.findByUserAccount(userAccount);
    }

    @Override
    public Optional<SysUser> findByUserAccountAndUserPassword(String userAccount, String userPassword) {
        return sysUserRepository.findByUserAccountAndUserPassword(userAccount, userPassword);
    }
}
