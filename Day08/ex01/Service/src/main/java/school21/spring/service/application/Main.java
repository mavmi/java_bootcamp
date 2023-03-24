package school21.spring.service.application;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import school21.spring.service.models.User;
import school21.spring.service.repositories.UsersRepository;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        if (!initDB()) System.exit(-1);

        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        UsersRepository usersRepository1 = context.getBean("usersRepositoryJdbc", UsersRepository.class);
        UsersRepository usersRepository2 = context.getBean("usersRepositoryJdbcTemplate", UsersRepository.class);

        System.out.println(test(usersRepository1).equals(test(usersRepository2)));
    }

    private static String test(UsersRepository usersRepository){
        StringBuilder result = new StringBuilder();

        result.append(usersRepository.findAll());
        result.append(usersRepository.findById(3L));
        usersRepository.save(new User(6L, "email6"));
        result.append(usersRepository.findById(6L));
        usersRepository.update(new User(6L, "email666"));
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
        hikariConfig.setValidationTimeout(300_000);

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
