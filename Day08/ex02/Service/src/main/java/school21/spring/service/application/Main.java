package school21.spring.service.application;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import school21.spring.service.config.ApplicationConfig;
import school21.spring.service.models.User;
import school21.spring.service.repositories.UsersRepository;
import school21.spring.service.repositories.UsersRepositoryJdbcImpl;
import school21.spring.service.repositories.UsersRepositoryJdbcTemplateImpl;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        if (!initDB()) System.exit(-1);
        String res1 = test((UsersRepositoryJdbcImpl)applicationContext.getBean("usersRepositoryJdbc"));

        if (!initDB()) System.exit(-1);
        String res2 = test((UsersRepositoryJdbcTemplateImpl)applicationContext.getBean("usersRepositoryJdbcTemplate"));

        System.out.println(res1.equals(res2));
    }

    private static String test(UsersRepository usersRepository){
        StringBuilder result = new StringBuilder();

        result.append(usersRepository.findAll());
        result.append(usersRepository.findById(3L));
        usersRepository.save(new User(123L, "email6", "password6"));
        result.append(usersRepository.findById(6L));
        usersRepository.update(new User(6L, "email666", "password666"));
        result.append(usersRepository.findById(6L));
        usersRepository.delete(6L);
        result.append(usersRepository.findById(6L));
        result.append(usersRepository.findByEmail("email3"));

        return result.toString();
    }

    private static boolean initDB(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/day08");
        hikariConfig.setUsername("pmaryjo");
        hikariConfig.setPassword("");

        try {
            DataSource dataSource = new HikariDataSource(hikariConfig);
            dataSource.getConnection().createStatement().executeUpdate(readFile(Main.class.getResource("/schema.sql").getPath()));
            dataSource.getConnection().createStatement().executeUpdate(readFile(Main.class.getResource("/data.sql").getPath()));
            return true;
        } catch (SQLException e) {
            System.err.println("Cannot init database");
            System.err.println(e.getMessage());
            return false;
        }
    }
    private static String readFile(String fileName){
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            StringBuilder builder = new StringBuilder();
            while (true){
                String line = reader.readLine();
                if (line == null) break;
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e){
            System.err.println(e.getMessage());
            return null;
        }
    }
}
