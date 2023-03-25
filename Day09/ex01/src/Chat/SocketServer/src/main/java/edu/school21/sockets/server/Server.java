package edu.school21.sockets.server;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepositoryImpl;
import edu.school21.sockets.services.MessageServiceImpl;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component("server")
public class Server {
    @Autowired
    @Qualifier("usersService")
    private UsersServiceImpl usersService;

    @Autowired
    @Qualifier("usersRepositoryJdbcTemplate")
    private UsersRepositoryImpl usersRepository;

    @Autowired
    @Qualifier("messageServiceImpl")
    private MessageServiceImpl messageService;

    private final long PAUSE = 1000;
    private final String IP = "127.0.0.1";

    private int port;
    private InetAddress host;
    private ServerSocket serverSocket;
    private Map<UUID, ClientConnection> activeConnections;

    public Server(){

    }
    public void init(int port){
        this.port = port;
        this.host = initHost();
        this.activeConnections = new HashMap<>();
        if (host == null) throw new RuntimeException("Cannot init InetAddress");
        this.serverSocket = initServerSocket();
        if (serverSocket == null) throw new RuntimeException("Cannot create ServerSocket");
    }

    public boolean run(){
        System.out.println(" == Server is running == ");
        System.out.println(host.getHostAddress() + ":" + port);

        while(true){
            try {
                Socket clientSocket = serverSocket.accept();
                UUID id = UUID.randomUUID();
                ClientConnection clientConnection = new ClientConnection(id, clientSocket, this);
                activeConnections.put(id, clientConnection);
                clientConnection.start();
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

    // UserService
    synchronized private String signUp(String email, String password){
        return usersService.signUp(email, password);
    }
    synchronized private User signIn(String email, String password){
        return usersService.signIn(email, password);
    }
    synchronized private void exit(String email){
        usersService.exit(email);
    }
    synchronized private void save(Message message){
        messageService.save(message);
    }
    synchronized private void sendAll(User author, String msg){
        for (UUID id : activeConnections.keySet()){
            ClientConnection clientConnection = activeConnections.get(id);
            if (clientConnection.user == author) continue;;
            if (clientConnection.user != null && clientConnection.user.getLog()){
                clientConnection.writeLine(msg);
            }
        }
    }

    private class ClientConnection extends Thread{
        private final long PAUSE = 1000;

        private UUID id;
        private Server server;
        private Socket socket;
        private User user;
        private BufferedInputStream inputStream;
        private BufferedOutputStream outputStream;

        public ClientConnection(UUID id, Socket socket, Server server){
            this.id = id;
            this.socket = socket;
            this.server = server;

            try {
                inputStream = new BufferedInputStream(socket.getInputStream());
                outputStream = new BufferedOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run() {
            if (!writeLine("Hello from Server!")) return;

            while (true) {
                String inputLine = readLine();
                if (inputLine == null) return;

                if (inputLine.equals("signUp")) {
                    if (user != null && user.getLog()){
                        if (!writeLine("You are already logged in")) return;
                    } else {
                        signUp();
                        try {
                            socket.close();
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                        return;
                    }
                } else if (inputLine.equals("signIn")) {
                    if (user == null || !user.getLog()) {
                        if (!signIn()) return;
                    } else {
                        if (!writeLine("You are already logged in")) return;
                    }
                } else if (inputLine.equals("Exit")) {
                    exit();
                    try { socket.close(); }
                    catch (IOException e) {}
                    return;
                } else {
                    if (user != null && user.getLog()){
                        msg(inputLine);
                    } else {
                        if (!writeLine("Invalid command")) return;
                    }
                }
            }
        }

        // Web stuff
        private boolean signUp(){
            writeLine("Enter username:");
            String username = readLine();
            if (username == null) return false;

            writeLine("Enter password:");
            String password = readLine();
            if (password == null) return false;

            String res = server.signUp(username, password);
            if (res == null) {
                if (!writeLine("User with such email/username already exists")) return false;
            }
            else {
                if (!writeLine("Successful!")) return false;
            }

            return true;
        }
        private boolean signIn(){
            if (user != null && user.getLog()) return writeLine("You are already logged in");

            writeLine("Enter username:");
            String username = readLine();
            if (username == null) return false;

            writeLine("Enter password:");
            String password = readLine();
            if (password == null) return false;

            User user = server.signIn(username, password);
            if (user == null) {
                writeLine("No user with such email/username or password is invalid");
                return false;
            }
            this.user = user;
            return writeLine("Start messaging");
        }
        private boolean msg(String msg){
            Message message = new Message(0L, user, msg, Timestamp.valueOf(LocalDateTime.now()));
            server.save(message);
            server.sendAll(user, user.getEmail() + ": " + msg);
            return true;
        }
        private void exit(){
            if (user != null) {
                server.exit(user.getEmail());
            }
        }

        // Utils
        private String readLine(){
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
        private boolean writeLine(String line){
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
}
