package com.example.housingmanagementsystem.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/api/auth/**").permitAll() //login,register
                        .anyRequest().authenticated() //everything else needs a token
                        )
                .sessionManagement(session ->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //JWT,no sessions
                        );
        //TODO: Add JWTFilter here

        return httpSecurity.build();
    }

    // Expose AuthenticationManager as a bean (needed for login)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
