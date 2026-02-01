package com.kefinity.auth.service;

import com.kefinity.common.core.domain.LoginUser;
import com.kefinity.common.core.exception.ServiceException;
import com.kefinity.common.core.utils.SpringUtils;

public interface IAuthStrategy {

    String BASE_NAME = "AuthStrategy";

    static LoginUser login(String username, String password, String grantType) {
        String beanName = grantType + BASE_NAME;
        if (!SpringUtils.containsBean(beanName)) {
            throw new ServiceException("授权类型不正确!");
        }
        IAuthStrategy instance = SpringUtils.getBean(beanName);
        return instance.login(username, password);
    }

    LoginUser login(String username, String password);
}
