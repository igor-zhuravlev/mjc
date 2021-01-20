package com.epam.esm.service.config;

import com.epam.esm.repository.config.RepositoryConfig;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Locale;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.epam.esm.service")
@Import(RepositoryConfig.class)
public class ServiceConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        JdbcTransactionManager jdbcTransactionManager = new JdbcTransactionManager(dataSource);
        return jdbcTransactionManager;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(Locale.ENGLISH);
        return messageSource;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
