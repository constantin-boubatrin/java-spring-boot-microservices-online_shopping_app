package com.example.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

// Using Web Flux Security
@Configuration
// Because the Api Gateway Spring Cloud Gateway project is based on Spring Web Flux project
// but not Spring Web MVC that's why we need to enable the Web Flux Security by @EnableWebFluxSecurity ann
@EnableWebFluxSecurity
public class SecurityConfig {

//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
//        // configuring the webflux security details
//
//        // because we're communicating through the postman client we have to disable the csrf
////        serverHttpSecurity.csrf() // csrf is dedicated
////                .disable()
//        serverHttpSecurity
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/eureka/**").permitAll() // for /eureka/** all have permission without authentication
//                        .anyExchange().authenticated() // for any other calls we have to make sure that those are authenticated
//                )
//                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
////        		.oauth2ResourceServer((oauth2) -> oauth2.jwt(withDefaults()));
//
//        return serverHttpSecurity.build();
//    }


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .csrf().disable()
                .authorizeExchange(exchange ->
                        exchange.pathMatchers("/eureka/**")
                                .permitAll()
                                .anyExchange()
                                .authenticated())
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        return serverHttpSecurity.build();
    }
}
