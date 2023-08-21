package com.example.bharath.corps;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v2/excel-to-postgres") // Update with your desired endpoint
                .allowedOrigins("https://localhost:3000") // Update with your front-end URL
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify allowed HTTP methods
                .allowedHeaders("Content-Type") // Specify allowed request headers
                .allowCredentials(true); // Allow sending cookies from the front-end
        registry.addMapping("api/v2/getData") // Update with your desired endpoint
                .allowedOrigins("https://localhost:3000") // Update with your front-end URL
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Specify allowed HTTP methods
                .allowedHeaders("Content-Type") // Specify allowed request headers
                .allowCredentials(true); // Allow sending cookies from the front-end
    }
}
