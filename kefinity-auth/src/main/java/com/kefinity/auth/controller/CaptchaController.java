package com.kefinity.auth.controller;

import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.kefinity.auth.config.WaveAndCircleCaptcha;
import com.kefinity.auth.domain.vo.CaptchaVo;
import com.kefinity.auth.properties.CaptchaProperties;
import com.kefinity.common.core.domain.R;
import com.kefinity.common.core.utils.SpringUtils;
import com.kefinity.common.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaProperties captchaProperties;

    @GetMapping("/code")
    public R<?> getCode() {
        CaptchaVo captchaVo = new CaptchaVo();
        Boolean enabled = captchaProperties.getEnabled();
        captchaVo.setCaptchaEnabled(enabled);
        if (!enabled) {
            return R.ok(captchaVo);
        }
        return R.ok(SpringUtils.getAopProxy(this).getCodeImpl());
    }

    public CaptchaVo getCodeImpl() {

        String uuid = IdUtil.simpleUUID();

        String key = "global:captcha_codes:"+uuid;

        RandomGenerator codeGenerator = new RandomGenerator(captchaProperties.getLength());

        WaveAndCircleCaptcha captcha = new WaveAndCircleCaptcha(160, 60);

        captcha.setFont(new Font("Arial", Font.BOLD, 45));

        captcha.setGenerator(codeGenerator);

        captcha.createCode();

        // 随机的验证码
        String code = captcha.getCode();

        // 将验证码保存到redis中, 2分钟后失效
        RedisUtils.setCacheObject(key, code, Duration.ofMillis(2));

        CaptchaVo captchaVo = new CaptchaVo();

        captchaVo.setUuid(uuid);

        captchaVo.setImg(captcha.getImageBase64());

        return captchaVo;


    }

}
