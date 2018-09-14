package com.example.chatroom.repo;

import com.example.chatroom.entity.SysUser;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author zhangtong
 * Created by on 2017/11/13
 */
public interface SysUserRepository extends CrudRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {

    Optional<SysUser> findByUserAccount(String userAccount);

    Optional<SysUser> findByUserAccountAndUserPassword(String userAccount, String userPassword);
}
