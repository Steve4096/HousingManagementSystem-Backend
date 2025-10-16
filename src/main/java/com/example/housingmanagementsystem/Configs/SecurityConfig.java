package com.example.housingmanagementsystem.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity //Enables @PreAuthorize annotations
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,JWTAuthenticationFilter jwtAuthenticationFilter) throws Exception{
        httpSecurity
                .csrf(csrf->csrf.disable())

                //Determines which methods are open vs protected
                .authorizeHttpRequests(auth->auth

                        //Open URLs/Public endpoints(no authentication required)
                       // .requestMatchers("/api/auth/**").permitAll() //login,refresh

                        //Methods/routes only accessible by admin
                                //.requestMatchers("/api/**").hasRole("ADMIN")

                        //Methods accessible by admin and landlord
                                //.requestMatchers("/api/**").hasAnyRole("ADMIN,LANDLORD")

                        //Methods accessible by admins,landlords and users
                               // .requestMatchers("/api/").hasAnyRole("ADMIN,LANDLORD,USER")

                        //.anyRequest().authenticated() //everything else needs a token

                        //Temporarily disable spring security for all endpoints for easier testing
                                .anyRequest().permitAll()
                        )

                //Tells spring security not to create or use any HTTP sessions
                .sessionManagement(session ->session

                        //STATELESS means each request is authenticated independently
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //JWT,no sessions
                        );

                //JWT filter
                //.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return httpSecurity.build();
    }

    // Expose AuthenticationManager as a bean (needed for login)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
