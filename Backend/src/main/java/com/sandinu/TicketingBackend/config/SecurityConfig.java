package com.sandinu.TicketingBackend.config;

import com.sandinu.TicketingBackend.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final JwtUtil jwtUtil;


    public SecurityConfig(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF for development
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/login", "/api/users/register/vendor", "/api/users/register/customer", "/check-session").permitAll()
                        .requestMatchers("/api/vendors/**").hasRole("VENDOR")
                        .requestMatchers("/api/events/**").hasRole("VENDOR")
                        .requestMatchers("/api/customers/**").hasRole("CUSTOMER")
                        .anyRequest().authenticated() // Require authentication for all other requests
                );

        return http.build();

    }

}
