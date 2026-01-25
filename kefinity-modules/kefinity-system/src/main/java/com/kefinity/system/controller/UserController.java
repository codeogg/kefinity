package com.kefinity.system.controller;

import com.kefinity.common.core.domain.LoginUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
public class UserController {

    @GetMapping("info")
    public LoginUser getUserInfo(@RequestParam("username") String username) {

        LoginUser user = new LoginUser();
        user.setUsername(username);
        user.setNickname("用户昵称");

        return user;
    }

}
