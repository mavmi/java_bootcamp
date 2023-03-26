package edu.school21.sockets.server;

import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepositoryImpl;
import edu.school21.sockets.services.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

@Component("server")
public class Server {
    @Autowired
    @Qualifier("usersService")
    private UsersServiceImpl usersService;

    @Autowired
    @Qualifier("usersRepositoryJdbcTemplate")
    private UsersRepositoryImpl usersRepository;

    private final long PAUSE = 1000;
    private final String IP = "127.0.0.1";

    private int port;
    private InetAddress host;
    private ServerSocket serverSocket;

    public Server(){

    }
    public void init(int port){
        this.port = port;
        this.host = initHost();
        if (host == null) throwOnError("Cannot init InetAddress");
        this.serverSocket = initServerSocket();
        if (serverSocket == null) throwOnError("Cannot create ServerSocket");
    }

    // Main thread
    public boolean run(){
        System.out.println(" == Server is running == ");
        System.out.println(host.getHostAddress() + ":" + port);

        while(true){
            try {
                Socket clientSocket = serverSocket.accept();
                BufferedOutputStream outputStream = new BufferedOutputStream(clientSocket.getOutputStream());
                BufferedInputStream inputStream = new BufferedInputStream(clientSocket.getInputStream());

                if (!writeLine(outputStream, "Hello from Server!")) return false;
                while (true){
                    String inputLine = readLine(inputStream);
                    if (inputLine == null) return false;
                    if (!inputLine.equals("signUp")) {
                        writeLine(outputStream, "Invalid command. Try again");
                        continue;
                    }

                    writeLine(outputStream, "Enter username:");
                    String username = readLine(inputStream);
                    if (username == null) return false;

                    writeLine(outputStream, "Enter password:");
                    String password = readLine(inputStream);
                    if (password == null) return false;

                    writeLine(outputStream, "Successful!");
                    usersService.signUp(username, password);
                    return true;
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
                return false;
            }
        }
    }

    // Server initialization
    private InetAddress initHost(){
        try {
            return InetAddress.getByName(IP);
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    private ServerSocket initServerSocket(){
        try {
            ServerSocket tmpServerSocket = new ServerSocket(port, 1, host);
            tmpServerSocket.setSoTimeout(0);
            return tmpServerSocket;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    // Utils
    private String readLine(BufferedInputStream inputStream){
        try {
            while (inputStream.available() <= 0) if (!pause()) return null;

            byte[] buffer = new byte[1024];
            StringBuilder builder = new StringBuilder();
            while (inputStream.available() > 0){
                int bytesReadCount = inputStream.read(buffer);
                if (bytesReadCount <= 0) break;
                builder.append(new String(buffer, 0, bytesReadCount));
            }

            return builder.toString();

        } catch (IOException e){
            System.err.println(e.getMessage());
            return null;
        }
    }
    private boolean writeLine(BufferedOutputStream outputStream, String line){
        try{
            outputStream.write(line.getBytes());
            outputStream.flush();
            return true;
        } catch (IOException e){
            System.err.println(e.getMessage());
            return false;
        }
    }
    private void throwOnError(String msg){
        throw new RuntimeException(msg);
    }
    private boolean pause(){
        try {
            Thread.sleep(PAUSE);
            return true;
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
