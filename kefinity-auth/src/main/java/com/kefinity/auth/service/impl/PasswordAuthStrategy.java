package com.kefinity.auth.service.impl;


import com.kefinity.auth.service.IAuthStrategy;
import com.kefinity.auth.service.SysLoginService;
import com.kefinity.common.core.domain.LoginUser;
import com.kefinity.common.satoken.utils.LoginHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("password" + IAuthStrategy.BASE_NAME)
@RequiredArgsConstructor
public class PasswordAuthStrategy implements IAuthStrategy{

    private final SysLoginService loginService;

    @Override
    public LoginUser login(String username, String password) {
        // 根据登录名查询用户
        LoginUser loginUser = loginService.checkLogin(username, password);
        LoginHelper.login(loginUser,null);
        return loginUser;
    }
}
