package com.example.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 降临
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private static final String UPLOADED_FOLDER = System.getProperty("user.dir");

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///" + UPLOADED_FOLDER + "/");
    }
}