package com.example.memorylane.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://memoryfrontend-o473.onrender.com")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true); // Required for cookies/session
    }
}
