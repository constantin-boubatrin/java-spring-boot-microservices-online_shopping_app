package com.example.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // creates a bean of type webclient and will define the bean with the name of webClient
    // whatever we add a Bean ann will create a bean with the name of the method webClient
//    @Bean
//    public WebClient webClient() {
//        return WebClient.builder().build();
//    };

    // instead of creating a bean for webclient we will create a bean for WebClient.Builder
    // this modifications will add the Client Side Load-balancing capabilities to the web client builder
    // whenever we're creating an instance of web client using this web client builder
    // it will automatically create the webclient instance, and it will use it to call the inventory service
    // in this way the order service will call from multiple instances
    // of the inventory service one by one (one after another)
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    };
}
