package com.example.memorylane.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://silver-crepe-177b8f.netlify.app")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true); // Required for cookies/session
    }
}
