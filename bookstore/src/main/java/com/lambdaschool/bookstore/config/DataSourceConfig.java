package com.lambdaschool.bookstore.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Value("${local.run.db:h2}")
    private String db;

    @Value("${spring.datasource.url:}")
    private String datasource;

    @Bean
    public DataSource dataSource()
    {
        if(db.equalsIgnoreCase("POSTGRESQL"))
        {
            // assume this is a heroku deployment
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("org.postgresql.Driver");
            config.setJdbcUrl(datasource);
            return new HikariDataSource(config);
        }
        else
        {
            // default to H2
            String myURLString = "jbdc:h2:mem:testdb";
            String myDriverClass = "org.h2.Driver";
            String myUsername = "sa";
            String myPassword = "";

            return DataSourceBuilder.create()
                    .username(myUsername)
                    .password(myPassword)
                    .driverClassName(myDriverClass)
                    .url(myURLString)
                    .build();
        }
    }
}
