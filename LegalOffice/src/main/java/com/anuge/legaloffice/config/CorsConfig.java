package com.anuge.legaloffice.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // Allow Angular application hosted on Vercel
        configuration.setAllowedOrigins(List.of(
            "https://legaloffice.vercel.app"
        ));

        // Allow HTTP methods used by the Angular application
        configuration.setAllowedMethods(List.of(
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "OPTIONS"
        ));

        // Allow headers used by Angular/JWT
        configuration.setAllowedHeaders(List.of(
            "Authorization",
            "Content-Type"
        ));

        // Allow credentials such as Authorization headers/cookies
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
