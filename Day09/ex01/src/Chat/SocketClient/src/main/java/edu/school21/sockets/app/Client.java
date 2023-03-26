package edu.school21.sockets.app;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private MsgGetter msgGetter;
    private final long PAUSE = 1000;
    private final int TIMEOUT = 10_000;
    private final String IP = "127.0.0.1";

    private boolean loggedIn;
    private int port;
    private Socket socket;
    private BufferedOutputStream outputStream;
    private BufferedInputStream inputStream;
    private Scanner scanner;

    public Client(int port){
        this.port = port;
        this.msgGetter = new MsgGetter(this);

        loggedIn = false;
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
        String inputLine = readLine();
        if (inputLine == null) return false;
        System.out.println(inputLine);

        while (true){
            inputLine = scanner.nextLine();

            if (inputLine.equals("signUp")){
                return signUp();
            } else if (inputLine.equals("signIn")){
                if (!signIn()) return false;
                if (loggedIn) msgGetter.start();
            } else if (inputLine.equals("Exit")){
                exit();
                return true;
            } else {
                if (loggedIn){
                    if (!writeLine(inputLine)) return false;
                } else {
                    System.out.println("Invalid command");
                }
            }
        }
    }

    private boolean signUp(){
        String inputLine;

        if (!writeLine("signUp")) return false;

        inputLine = readLine();
        System.out.println(inputLine);
        if (inputLine == null || !inputLine.equals("Enter username:")) return false;
        if (!writeLine(scanner.nextLine())) return false;

        inputLine = readLine();
        System.out.println(inputLine);
        if (inputLine == null || !inputLine.equals("Enter password:")) return false;
        if (!writeLine(scanner.nextLine())) return false;

        inputLine = readLine();
        System.out.println(inputLine);
        return true;
    }
    private boolean signIn(){
        String inputLine;

        if (!writeLine("signIn")) return false;

        inputLine = readLine();
        System.out.println(inputLine);
        if (inputLine == null || !inputLine.equals("Enter username:")) return false;
        if (!writeLine(scanner.nextLine())) return false;

        inputLine = readLine();
        System.out.println(inputLine);
        if (inputLine == null || !inputLine.equals("Enter password:")) return false;
        if (!writeLine(scanner.nextLine())) return false;

        inputLine = readLine();
        System.out.println(inputLine);

        if (inputLine == null) return false;
        if (inputLine.equals("Start messaging")) loggedIn = true;
        return loggedIn;
    }
    private void exit(){
        msgGetter.exit = true;
        writeLine("Exit");
        try { socket.close(); }
        catch (IOException e) {}
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
    synchronized private String readLine(){
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

    private class MsgGetter extends Thread{
        private boolean exit;
        private Client client;

        public MsgGetter(Client client){
            this.exit = false;
            this.client = client;
        }

        @Override
        public void run() {
            while (!exit){
                String inputLine = client.readLine();
                if (inputLine != null) System.out.println(inputLine);
                client.pause();
            }
        }
    }
}
