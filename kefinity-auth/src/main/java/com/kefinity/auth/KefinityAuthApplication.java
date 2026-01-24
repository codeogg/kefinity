package com.kefinity.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class KefinityAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(KefinityAuthApplication.class, args);
        System.out.println("认证服务启动成功!!!");
    }
}
