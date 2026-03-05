package com.demo_hotel_service.config;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${image_storage.uploads.dir}")
    private String uploadsDir;
    @Value("${image_storage.uploads.url}")
    private String uploadsUrl;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry){
        Path path = Path.of(uploadsDir);
        String strPath = path.toUri().toString();
        resourceHandlerRegistry.addResourceHandler(uploadsUrl +"**")
            .addResourceLocations(path.toUri().toString());
    }
}
