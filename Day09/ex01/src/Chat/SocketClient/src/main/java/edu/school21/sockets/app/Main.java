package edu.school21.sockets.app;

public class Main {
    public static void main(String[] args){
        try {
            Client client = new Client(parseArgs(args));
        } catch (RuntimeException e){
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    private static int parseArgs(String[] args){
        final String errMsg = "Error\n" +
                "Usage: ServerSocket.jar --server-port=[PORT]";

        if (args.length != 1) throw new RuntimeException(errMsg);
        int eqPos = args[0].indexOf('=');
        if (eqPos == -1) throw new RuntimeException(errMsg);
        String key = args[0].substring(0, eqPos);
        String value = args[0].substring(eqPos + 1);
        if (!key.equals("--server-port")) throw new RuntimeException(errMsg);

        try {
            int port = Integer.parseInt(value);
            if (port < 0 || port > 65536) throw new Exception();
            return port;
        } catch (Exception e){
            throw new RuntimeException(errMsg);
        }
    }
}
