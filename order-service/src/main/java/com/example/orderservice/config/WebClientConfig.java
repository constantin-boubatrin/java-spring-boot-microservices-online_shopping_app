package com.example.orderservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // creates a bean of type webclient and will define the bean with the name of webClient
    // whatever we add a Bean ann will create a bean with the name of the method webClient
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    };
}
