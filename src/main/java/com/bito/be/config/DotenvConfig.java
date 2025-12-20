package com.bito.be.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {

    @PostConstruct
    public void loadEnv() {
        // Load .env file
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        // Set environment variables as system properties
        dotenv.entries().forEach(entry ->
            System.setProperty(entry.getKey(), entry.getValue())
        );
    }
}