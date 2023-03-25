package edu.school21.sockets.app;

import edu.school21.sockets.server.Server;
import org.springframework.context.ApplicationContext;
import edu.school21.sockets.config.SocketsApplicationConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.io.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        initDb(applicationContext);

        Server server = (Server) applicationContext.getBean("server");
        server.init(parseArgs(args));

        if (!server.run()){
            System.err.println("Server error");
            System.exit(-1);
        }
    }

    private static int parseArgs(String[] args){
        final String errMsg = "Error\n" +
                "Usage: ServerSocket.jar --port=[PORT]";

        if (args.length != 1) throw new RuntimeException(errMsg);
        int eqPos = args[0].indexOf('=');
        if (eqPos == -1) throw new RuntimeException(errMsg);
        String key = args[0].substring(0, eqPos);
        String value = args[0].substring(eqPos + 1);
        if (!key.equals("--port")) throw new RuntimeException(errMsg);

        try {
            int port = Integer.parseInt(value);
            if (port < 0 || port > 65536) throw new Exception();
            return port;
        } catch (Exception e){
            throw new RuntimeException(errMsg);
        }
    }

    private static void initDb(ApplicationContext context){
        try {
            DataSource dataSource = (DataSource) context.getBean("HikariDataSource");
            dataSource.getConnection().createStatement().executeUpdate(readFile(Main.class.getResourceAsStream("/schema.sql")));
            dataSource.getConnection().createStatement().executeUpdate(readFile(Main.class.getResourceAsStream("/data.sql")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static String readFile(InputStream inputStream){
        try (BufferedInputStream reader = new BufferedInputStream(inputStream)){
            byte[] buffer = new byte[1024];
            StringBuilder query = new StringBuilder();

            while (reader.available() > 0){
                int bytesReadCount = reader.read(buffer);
                String line = new String(buffer, 0, bytesReadCount);
                query.append(line);
            }

            return query.toString();
        } catch (IOException e){
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
