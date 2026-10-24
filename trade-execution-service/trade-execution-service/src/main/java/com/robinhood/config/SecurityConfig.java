
package com.robinhood.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            // Disable CSRF for stateless REST APIs
            .csrf(AbstractHttpConfigurer::disable)

            // Keep existing CORS configuration
            .cors(cors -> {})

            // Stateless REST API
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // Allow Actuator health checks
                .requestMatchers(
                    HttpMethod.GET,
                    "/actuator/health",
                    "/actuator/health/**"
                ).permitAll()

                // Keep existing API access for local testing
                .requestMatchers("/api/**").permitAll()

                // Protect remaining endpoints
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
