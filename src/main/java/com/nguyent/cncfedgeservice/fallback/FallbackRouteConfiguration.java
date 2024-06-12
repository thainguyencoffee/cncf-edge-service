package com.nguyent.cncfedgeservice.fallback;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class FallbackRouteConfiguration {

    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .GET("/api-fallback", request ->
                        ServerResponse.ok().body(Mono.just("api fallback"), String.class)
                ).build();
    }

}
