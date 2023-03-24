package school21.spring.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import school21.spring.service.repositories.UsersRepositoryJdbcImpl;
import school21.spring.service.repositories.UsersRepositoryJdbcTemplateImpl;
import school21.spring.service.services.UsersServiceImpl;

import javax.sql.DataSource;

@Configuration
public class TestApplicationConfig {
    @Bean({"HikariDataSource", "SpringDataSource"})
    public DataSource getDataSource(){
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .setName("day08")
                .addScript("schemaTest.sql")
                .addScript("data.sql")
                .build();
    }

    @Bean("usersRepositoryJdbc")
    public UsersRepositoryJdbcImpl getUsersRepositoryJdbcImpl(){
        return new UsersRepositoryJdbcImpl();
    }

    @Bean("usersRepositoryJdbcTemplate")
    public UsersRepositoryJdbcTemplateImpl getUsersRepositoryJdbcTemplateImpl(){
        return new UsersRepositoryJdbcTemplateImpl();
    }

    @Bean(name = "usersService")
    public UsersServiceImpl getUsersServiceImpl(){
        return new UsersServiceImpl();
    }
}
