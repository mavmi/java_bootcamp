package edu.school21.chat.app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.school21.chat.models.ChatRoom;
import edu.school21.chat.models.Message;
import edu.school21.chat.models.User;
import edu.school21.chat.repositories.MessagesRepository;
import edu.school21.chat.repositories.MessagesRepositoryJdbcImpl;
import edu.school21.chat.repositories.NotSavedSubEntityException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Program {
    public static void main(String[] args){
        HikariDataSource hikariDataSource = getDataSource();

        User creator = new User(1L, "user", "user", new ArrayList(), new ArrayList());
        User author = creator;
        ChatRoom room = new ChatRoom(2L, "room", creator, new ArrayList());
        Message message = new Message(-1, author, room, "Hello!", Timestamp.valueOf(LocalDateTime.now()));
        MessagesRepository messagesRepository = new MessagesRepositoryJdbcImpl(hikariDataSource);
        try {
            messagesRepository.save(message);
            System.out.println(message.getId());
        } catch (NotSavedSubEntityException e){
            System.err.println(e.getMessage());
        }
    }

    private static boolean initDatabase(HikariDataSource hikariDataSource){
        Connection connection = null;
        try {
            connection = hikariDataSource.getConnection();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        String createTablesQuery = readQueryFromFile("./ex01/Chat/src/main/resources/schema.sql");
        String fillTablesQuery = readQueryFromFile("./ex01/Chat/src/main/resources/data.sql");
        if (createTablesQuery == null || fillTablesQuery == null) System.exit(-1);

        try {
            connection.createStatement().executeUpdate(createTablesQuery);
            connection.createStatement().executeUpdate(fillTablesQuery);
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    private static HikariDataSource getDataSource(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/day05");
        hikariConfig.setUsername("pmaryjo");
        hikariConfig.setPassword("");
        hikariConfig.setValidationTimeout(300_000);

        return new HikariDataSource(hikariConfig);
    }
    private static String readQueryFromFile(String filePath){
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))){
            StringBuilder query = new StringBuilder();
            String line;
            while (true){
                line = reader.readLine();
                if (line == null) break;
                query.append(line);
            }
            return query.toString();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    private static long getId(){
        try {
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLong();
        } catch (Exception e){
            System.err.println(e.getMessage());
            return -1;
        }
    }
}
