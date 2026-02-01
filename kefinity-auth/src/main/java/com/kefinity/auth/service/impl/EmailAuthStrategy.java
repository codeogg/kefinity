package com.kefinity.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import com.kefinity.auth.feign.RemoteUserService;
import com.kefinity.auth.service.IAuthStrategy;
import com.kefinity.auth.service.SysLoginService;
import com.kefinity.common.core.domain.LoginUser;
import com.kefinity.common.redis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 邮件认证策略
 *
 * @author Michelle.Chung
 */
@Slf4j
@Service("email" + IAuthStrategy.BASE_NAME)
public class EmailAuthStrategy implements IAuthStrategy {

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private RemoteUserService remoteUserService;

    @Override
    public LoginUser login(String username, String password) {
        return null;
    }

    /**
     * 校验邮箱验证码
     */
    private boolean validateEmailCode(String tenantId, String email, String emailCode) {
//        String code = RedisUtils.getCacheObject(GlobalConstants.CAPTCHA_CODE_KEY + email);
//        if (StringUtils.isBlank(code)) {
//            loginService.recordLogininfor(tenantId, email, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
//            throw new CaptchaExpireException();
//        }
//        return code.equals(emailCode);
        return true;
    }

}
