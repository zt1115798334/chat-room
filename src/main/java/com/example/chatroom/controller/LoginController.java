package com.example.chatroom.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.chatroom.controller.base.AbstractController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/14 10:49
 * description:
 */
@RestController
@RequestMapping(value = "api/login")
public class LoginController extends AbstractController {

    /**
     * 网站登录
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping(value = "ajaxLogin")
    public JSONObject ajaxLogin(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam Boolean rememberMe) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return failure("请输入账户,密码");
        }
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
            SecurityUtils.getSubject().login(token);
            return success("登录成功");
        } catch (Exception e) {
            System.out.println("e = " + e);
            return failure(e.getMessage());
        }
    }
}
