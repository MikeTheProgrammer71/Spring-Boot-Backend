package com.superherobackend.superhero.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Bean
    @Profile("development")
    public DataSource developmentDataSource() {
        return DataSourceBuilder.create()
                .url(dbUrl)
                .build();
    }

    @Bean
    @Profile("test")
    public DataSource testDataSource() {
        return DataSourceBuilder.create()
                .url(dbUrl)
                .build();
    }

    @Bean
    @Profile("production")
    public DataSource productionDataSource() {
        return DataSourceBuilder.create()
                .url(dbUrl)
                .build();
    }
    
}

