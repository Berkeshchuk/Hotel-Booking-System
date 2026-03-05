package com.api.gateway.config.cloud;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class GatewayMultipartConfig {

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver() {
            @Override
            public boolean isMultipart(HttpServletRequest request) {
                String uri = request.getRequestURI();
                
                // 1. ВИМИКАЄМО парсинг для мікросервісу кімнат
                if (uri != null && uri.startsWith("/api/rooms")) {
                    return false; 
                }
                
                // 2. УВІМКНЕНО для всіх інших
                return super.isMultipart(request);
            }
        };
    }
}