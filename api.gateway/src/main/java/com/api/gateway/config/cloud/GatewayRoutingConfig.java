package com.api.gateway.config.cloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.common.MvcUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;

import java.net.URI;

import com.api.gateway.config.security.JwtUtil;
import com.common.security.AuthPrincipal;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class GatewayRoutingConfig {
    private final JwtUtil jwtUtil;
    @Value("${booking_service_url}")
    private String bookingServiceUrl;
    @Value("${user_service_url}")
    private String userServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> bookingRoute() {
        return buildServiceRoute("/api/bookings/**", bookingServiceUrl);
    }

    @Bean
    public RouterFunction<ServerResponse> roomRoute() {
        return buildServiceRoute("/api/rooms/**", bookingServiceUrl);
    }
    @Bean
    public RouterFunction<ServerResponse> imageRoute() {
        return buildServiceRoute("/api/images/**", bookingServiceUrl);
    }

    @Bean
    public RouterFunction<ServerResponse> uploadsRoute() {
        return buildPublicServiceRoute("/uploads/**", bookingServiceUrl);
    }
    @Bean
    public RouterFunction<ServerResponse> signUpRoute() {
        return buildPublicServiceRoute("/sign_up**", userServiceUrl);
    }
    
    private RouterFunction<ServerResponse> buildServiceRoute(String pattern, String targetUrl) {
        return route()
            .route(path(pattern), http())
            .filter((req, next) -> {
                // Відразу створюємо білдер і вказуємо, куди проксіювати запит
                ServerRequest.Builder builder = ServerRequest.from(req)
                        .attribute(MvcUtils.GATEWAY_REQUEST_URL_ATTR, URI.create(targetUrl));

                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                
                if (auth != null && auth.getPrincipal() instanceof AuthPrincipal p) {
                    String jwt = jwtUtil.generateUserToken(p.getId(), p.getLogin(), p.getRole());
                    builder.header("Authorization", "Bearer " + jwt);
                }

                return next.handle(builder.build());
            })
        .build();
    }

    private RouterFunction<ServerResponse> buildPublicServiceRoute(String pattern, String targetUrl) {
        return route()
            .route(path(pattern), http())
            .filter((req, next) -> {
                // Відразу створюємо білдер і вказуємо, куди проксіювати запит
                ServerRequest.Builder builder = ServerRequest.from(req)
                        .attribute(MvcUtils.GATEWAY_REQUEST_URL_ATTR, URI.create(targetUrl));
                return next.handle(builder.build());
            })
        .build();
    }
}
