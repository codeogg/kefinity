package com.kefinity.auth.controller;

import com.kefinity.common.core.domain.LoginBody;
import com.kefinity.common.core.domain.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    public R<?> login(@RequestBody LoginBody body){

        return R.ok("登录成功");
    }
}
