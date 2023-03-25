package edu.school21.sockets.app;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final long PAUSE = 1000;
    private final int TIMEOUT = 10_000;
    private final String IP = "127.0.0.1";

    private int port;
    private Socket socket;
    private BufferedOutputStream outputStream;
    private BufferedInputStream inputStream;
    private Scanner scanner;

    public Client(int port){
        this.port = port;

        scanner = new Scanner(System.in);
        socket = connect();
        if (socket == null) throwOnError("Cannot connect to server");

        try {
            outputStream = new BufferedOutputStream(socket.getOutputStream());
            inputStream = new BufferedInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throwOnError("Cannon init IOStream");
        }

        if (!run()) throwOnError("Smth bad happened, brother");
    }

    // Main thread
    public boolean run(){
        String userRequest;
        String inputLine;

        inputLine = readLine();
        if (inputLine == null) return false;
        System.out.println(inputLine);
        if (!inputLine.equals("Hello from Server!")) return false;

        while (true) {
            userRequest = scanner.nextLine();
            if (!writeLine(userRequest)) return false;

            inputLine = readLine();
            if (inputLine == null) return false;
            System.out.println(inputLine);
            if (inputLine.equals("Enter username:")) break;
        }

        userRequest = scanner.nextLine();
        if (!writeLine(userRequest)) return false;

        inputLine = readLine();
        if (inputLine == null) return false;
        System.out.println(inputLine);

        userRequest = scanner.nextLine();
        if (!writeLine(userRequest)) return false;

        inputLine = readLine();
        if (inputLine == null) return false;
        System.out.println(inputLine);

        try { socket.close();}
        catch (IOException e) {}

        return true;
    }

    // Init connection
    private Socket connect(){
        try {
            Socket tmpSocket = new Socket();
            tmpSocket.connect(new InetSocketAddress(IP, port), TIMEOUT);
            return tmpSocket;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
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
        } catch (IOException e) {
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
