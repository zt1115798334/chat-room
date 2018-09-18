package com.example.chatroom.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/14 11:09
 * description:
 */
@Controller
public class PageController {

    @GetMapping(value = {"login"})
    public String login() {
        return "login";
    }

    @GetMapping(value = {"/", "index"})
    public String index() {
        return "index";
    }

    @GetMapping(value = {"voice"})
    public String voice() {
        return "voice";
    }
}
