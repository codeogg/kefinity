package com.kefinity.auth.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.kefinity.auth.feign.RemoteUserService;
import com.kefinity.auth.properties.UserPasswordProperties;
import com.kefinity.common.core.domain.LoginUser;
import com.kefinity.common.core.exception.UserException;
import com.kefinity.common.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class SysLoginService {

    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private UserPasswordProperties userPasswordProperties;


    public LoginUser checkLogin(String username, String password) {

        LoginUser user = remoteUserService.getUserInfo(username);

        String errorKey = "login_error_count:" + username;

        Integer maxRetryCount = userPasswordProperties.getMaxRetryCount();
        Integer lockTime = userPasswordProperties.getLockTime();

        int errorNumber = ObjectUtil.defaultIfNull(RedisUtils.getCacheObject(errorKey), 0);

        if (errorNumber >= maxRetryCount) {
            recordLoginInformation(username, "Error", "密码输入错误" + maxRetryCount + "次，账户锁定" + lockTime + "分钟");
            throw new UserException("密码输入错误" + maxRetryCount + "次，账户锁定" + lockTime + "分钟");
        }

        if (!StrUtil.equals(password, user.getPassword())) {
            errorNumber++;
            RedisUtils.setCacheObject(errorKey, errorNumber, Duration.ofMinutes(lockTime));

            if (errorNumber >= maxRetryCount) {
                recordLoginInformation(username, "Error", "密码输入错误" + maxRetryCount + "次，账户锁定" + lockTime + "分钟");
                throw new UserException("密码输入错误" + maxRetryCount + "次，账户锁定" + lockTime + "分钟");
            } else {
                // 未达到规定错误次数
                recordLoginInformation(username, "Error", "密码输入错误" + errorNumber + "次");
                throw new UserException("密码输入错误" + maxRetryCount + "次");
            }
        }

        RedisUtils.deleteObject(errorKey);

        return user;
    }

    public void recordLoginInformation(String username, String status, String message) {
    }
}
