package co.com.pragma.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "routes.paths")
public record UserPath(String users) {}
