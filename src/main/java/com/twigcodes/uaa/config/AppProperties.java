package com.twigcodes.uaa.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "uaa")
public class AppProperties {
    private String jwtSigningKey = "mySecret";
    private String clientId = "discoveryClient";
    private String clientSecret = "discoverySecret";
    private String serverUrl = "http://localhost:8095";
}
