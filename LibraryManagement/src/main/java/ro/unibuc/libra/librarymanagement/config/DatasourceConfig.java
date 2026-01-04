package ro.unibuc.libra.librarymanagement.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatasourceConfig {

    @Bean(name = "commonDataSourceHikariConfig")
    @ConfigurationProperties(prefix = "spring.datasource")
    public HikariConfig commonHikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public SpringLiquibase liquibase(HikariDataSource dataSource,
                                     LiquibaseProperties liquibaseProperties) {
        var liquibase = new SpringLiquibase();
        liquibase.setChangeLog(liquibaseProperties.getChangeLog());
        liquibase.setDataSource(dataSource);
        liquibase.setShouldRun(liquibaseProperties.isEnabled());

        return liquibase;
    }
}
