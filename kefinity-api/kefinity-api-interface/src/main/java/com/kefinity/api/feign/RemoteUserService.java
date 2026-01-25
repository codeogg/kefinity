package com.kefinity.api.feign;

import com.kefinity.common.core.domain.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "system-service")
public interface RemoteUserService {

    @GetMapping("/user/info")
    LoginUser getUserInfo(@RequestParam("username") String username);
}
