package edu.school21.sockets.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@ComponentScan("edu.school21.sockets")
@PropertySource("classpath:db.properties")
public class SocketsApplicationConfig {
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
        hikariConfig.setConnectionTimeout(999_000_000);
        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "passwordEncoder")
    @Scope("singleton")
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
