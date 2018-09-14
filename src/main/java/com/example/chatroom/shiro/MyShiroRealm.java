package com.example.chatroom.shiro;

import com.example.chatroom.entity.SysUser;
import com.example.chatroom.service.SysUserService;
import com.example.chatroom.utils.MyDES;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/14 10:30
 * description: shiro身份校验核心类
 */
public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private SysUserService sysUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());
        Optional<SysUser> userOptional = sysUserService.findByUserAccount(username);
        if (!userOptional.isPresent()) {
            throw new AccountException("账户不存在");
        }

        SysUser sysUser = userOptional.get();
        String paw = password + username;
        String pawDES = MyDES.encryptBasedDes(paw);
        Optional<SysUser> sysUserOptional = sysUserService.findByUserAccountAndUserPassword(username, pawDES);
        if (!sysUserOptional.isPresent()) {
            throw new AccountException("帐号或密码不正确！");
        }

        //处理session
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
        for (Session session : sessions) {
            //清除该用户以前登录时保存的session
            Object attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            System.out.println("attribute = " + attribute);
            if (username.equals(String.valueOf(attribute))) {
                sessionManager.getSessionDAO().delete(session);
            }
        }

        return new SimpleAuthenticationInfo(sysUser, password, getName());
    }
}
