package com.example.housingmanagementsystem.Configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JWTProperties {

    private String secretKey;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
}
