package school21.spring.service.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("school21.spring.service")
@PropertySource("classpath:db.properties")
public class ApplicationConfig {
    @Value("${db.url}")
    private String DB_URL;
    @Value("${db.user}")
    private String DB_USER;
    @Value("${db.password}")
    private String DB_PASSWD;
    @Value("${db.driver.name}")
    private String DB_DRIVER_NAME;

    @Bean(name = "HikariDataSource")
    @Scope("singleton")
    public DataSource initHikariDataSource(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(DB_URL);
        hikariConfig.setUsername(DB_USER);
        hikariConfig.setPassword(DB_PASSWD);
        hikariConfig.setDriverClassName(DB_DRIVER_NAME);
        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "SpringDataSource")
    @Scope("singleton")
    public DataSource initSpringDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USER);
        dataSource.setPassword(DB_PASSWD);
        dataSource.setDriverClassName(DB_DRIVER_NAME);
        return dataSource;
    }
}
