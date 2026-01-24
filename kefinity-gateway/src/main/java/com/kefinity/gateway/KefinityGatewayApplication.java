package com.kefinity.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class KefinityGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(KefinityGatewayApplication.class, args);
        System.out.println("网关启动成功!!!");
    }
}
