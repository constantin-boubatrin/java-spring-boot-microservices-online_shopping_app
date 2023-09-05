package com.example.discoveryserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${eureka.username}")
    private String username;
    @Value("${eureka.password}")
    private String password;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser(username).password(password)
                .authorities("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
}

// Using Spring MVC Security we can make use of web security configurer adapter class
// Here we have the basic security configuration with basic auth enabled
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    // previously defined in the application.properties file
//    @Value("${eureka.username}")
//    private String username;
//
//    @Value("${eureka.password}")
//    private String password;
//
//    @Bean
//    public static PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//    // Configure Authentication
//    //using the httpBasic() element to define Basic Authentication inside the SecurityFilterChain bean.
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf().disable()
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().authenticated())
//                .httpBasic(Customizer.withDefaults());
//        return httpSecurity.build();
//    }
//
//    // Configure HTTP Security
//    // In-Memory Authentication
//    //we have created one user and stored it in the InMemoryUserDetailsManager class object
//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user = User.builder()
//                .username(username)
//                .password(passwordEncoder().encode(password))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
//}
