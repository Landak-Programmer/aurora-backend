package com.aurora.backend.security.api;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Configuration
public class CorsConfig {

    @Value("${app.security.cors.allowed_origins:localhost:9293}")
    private String allowedOrigins;

    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", createConfig());
        return source;
    }

    private CorsConfiguration createConfig() {
        String[] allowOrigins = getAllowedOrigins();

        // Allow any origin by default
        if (allowOrigins.length == 1 && EMPTY.equals(allowOrigins[0])) {
            allowOrigins = new String[] { "*" };
        }

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.copyOf(allowOrigins));
        configuration.setAllowedMethods(ImmutableList.of("*"));
        configuration.setAllowedHeaders(ImmutableList.of("*"));
        return configuration;
    }

    public String[] getAllowedOrigins() {
        assert allowedOrigins != null;
        return allowedOrigins.split(",");
    }

}
