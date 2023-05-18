package net.quazar.apigateway.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.quazar.apigateway.exception.ApiGatewayErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    ApiGatewayErrorDecoder errorDecoder() {
        return new ApiGatewayErrorDecoder();
    }

    @Bean
    JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

}
