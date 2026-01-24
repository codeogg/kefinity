package com.kefinity.gateway.filter.factory;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

@Component
public class StripPrefixGatewayFilterFactory
        extends AbstractGatewayFilterFactory<StripPrefixGatewayFilterFactory.Config> {

    public static final String PARTS_KEY = "parts";

    public StripPrefixGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String path = request.getURI().getRawPath();
            String[] originalParts = StringUtils.tokenizeToStringArray(path, "/");

            StringBuilder newPath = new StringBuilder("/");
            for (int i = 0; i < originalParts.length; i++) {
                if (i >= config.getParts()) {
                    // only append slash if this is the second part or greater
                    if (newPath.length() > 1) {
                        newPath.append('/');
                    }
                    newPath.append(originalParts[i]);
                }
            }
            if (newPath.length() > 1 && path.endsWith("/")) {
                newPath.append('/');
            }

            String prefix = "/" + originalParts[config.getParts() - 1];

            ServerHttpRequest newRequest = request.mutate()
                    .header("X-Forwarded-Prefix", prefix)
                    .path(newPath.toString())
                    .build();

            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());

            return chain.filter(exchange.mutate().request(newRequest).build());
        };
    }


    public static class Config {

        private int parts = 1;

        public int getParts() {
            return this.parts;
        }

        public void setParts(int parts) {
            this.parts = parts;
        }
    }
}
