package com.kefinity.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.kefinity.auth.properties.CaptchaProperties;
import com.kefinity.auth.service.IAuthStrategy;
import com.kefinity.auth.service.SysLoginService;
import com.kefinity.common.core.domain.LoginBody;
import com.kefinity.common.core.domain.LoginUser;
import com.kefinity.common.core.domain.R;
import com.kefinity.common.core.exception.CaptchaException;
import com.kefinity.common.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final CaptchaProperties captchaProperties;
    private final SysLoginService loginService;

    @PostMapping("/login")
    public R<?> login(@RequestBody LoginBody body) {
        String username = body.getUsername();
        String password = body.getPassword();
        String code = body.getCode();
        String uuid = body.getUuid();


        // 验证码开启
        if (captchaProperties.getEnabled()) {
            // 对验证码进行验证
            validateCaptcha(username, code, uuid);
        }

        LoginUser loginUser = IAuthStrategy.login(username, password, "password");
        return R.ok(loginUser);
    }

    private void validateCaptcha(String username, String code, String uuid) {
        // 从redis里获取存放的验证码
        String redisKey = "global:captcha_codes:" + uuid;
        String captcha = RedisUtils.getCacheObject(redisKey);

        RedisUtils.deleteObject(redisKey);
        if (captcha == null) {
            loginService.recordLoginInformation(username, "Error", "验证码错误");
            throw new CaptchaException("验证码已失效");
        }

        if (!StrUtil.equalsIgnoreCase(code, captcha)) {
            loginService.recordLoginInformation(username, "Error", "验证码错误");
            throw new CaptchaException("验证码错误");
        }
    }
}
