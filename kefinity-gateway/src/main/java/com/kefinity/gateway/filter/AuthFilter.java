package com.kefinity.gateway.filter;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.dev33.satoken.util.SaTokenConsts;
import cn.hutool.core.util.StrUtil;
import com.kefinity.common.core.constant.HttpStatus;
import com.kefinity.gateway.config.properties.IgnoreWhiteProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

@Configuration
public class AuthFilter {


    @Bean
    public SaReactorFilter getSaReactorFilter(IgnoreWhiteProperties ignoreWhiteProperties) {
        return new SaReactorFilter()
                .addInclude("/**")
                .addExclude("/favicon.ico")
                .setAuth(obj -> {
                    SaRouter.match("/**")
                            .notMatch(ignoreWhiteProperties.getWhites())
                            .check(r -> {
                                ServerHttpRequest request = SaReactorSyncHolder.getExchange().getRequest();
                                StpUtil.checkLogin();

                                String clienId = "clienId";
                                // 请求头里获取客户端ID
                                String headerCid = request.getHeaders().getFirst(clienId);
                                // 参数里获取客户端ID
                                String paramCid = request.getQueryParams().getFirst(clienId);
                                // 获取客户端ID
                                String clientId = StpUtil.getExtra(clienId).toString();
                                // 校验客户端ID, 请求头或者参数里只要有一个和登录的客户端ID不一致,就抛出异常
                                if (!StrUtil.equalsAny(clientId, headerCid, paramCid)) {
                                    throw NotLoginException.newInstance(StpUtil.getLoginType(),
                                            "-100", "客户端ID与Token不匹配", StpUtil.getTokenValue()
                                    );
                                }
                            });

                }).setError(e -> {
                    ServerHttpResponse response = SaReactorSyncHolder.getExchange().getResponse();
                    // 设置HTTP响应头，将Content-Type指定为application/json格式
                    response.getHeaders().set(SaTokenConsts.CONTENT_TYPE_KEY, SaTokenConsts.CONTENT_TYPE_APPLICATION_JSON);
                    if (e instanceof NotLoginException) {
                        return SaResult.error(e.getMessage()).setCode(HttpStatus.UNAUTHORIZED);
                    }
                    return SaResult.error("认证失败，无法访问系统资源").setCode(HttpStatus.UNAUTHORIZED);
                });
    }


}
