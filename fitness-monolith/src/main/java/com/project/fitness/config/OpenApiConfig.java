package com.project.fitness.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fitness Tracking API")
                        .version("1.0")
                        .description("API documentation for Fitness Tracking Application")
                        .license(new License()
                                .name("Git-hub")
                                .url("https://github.com/sumantkr1306/Fitness-TrackerApplication"))
                );
    }
}