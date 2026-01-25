package com.kefinity.common.satoken.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.core.util.ObjectUtil;
import com.kefinity.common.core.domain.LoginUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {

    public static void login(LoginUser loginUser, SaLoginParameter model) {
        model = ObjectUtil.defaultIfNull(model, new SaLoginParameter());
        StpUtil.login(loginUser.getUserId(),
                model.setExtra("userId", loginUser.getUserId())
                        .setExtra("userName", loginUser.getUsername())
        );
        StpUtil.getTokenSession().set("loginUser", loginUser);
    }
}
