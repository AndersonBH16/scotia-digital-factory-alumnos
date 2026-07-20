package com.scotiabank.digitalfactory.alumnos.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security")
public record SecurityProperties(String apiKey) {
}
