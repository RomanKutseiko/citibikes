package com.kutseiko.bicycle.config;

import org.springframework.boot.SpringBootConfiguration;

@SpringBootConfiguration
public class AppConfig {

    /*@Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUser;

    @Value("${spring.datasource.password}")
    private String databasePassword;*/

    /*@Bean
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }*/

    /*
    @Bean
    public Connection connection() {
        try {
            return DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }*/

}
