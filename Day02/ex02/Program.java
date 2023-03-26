package ex02;

public class Program {
    public static void main(String[] args){
        try {
            String startFolder = (args.length == 0) ? System.getProperty("user.dir") : parseInput(args[0]);
            new FileManager(startFolder).run();
        } catch (Exception e){
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    private static String parseInput(String arg){
        final String errMsg = "Invalid argument\n" +
                                "Usage: Program.java {--current-folder=[FOLDER_PATH]}";

        int eqPos = arg.indexOf('=');
        if (eqPos == -1) throw new RuntimeException(errMsg);
        String flag = arg.substring(0, eqPos);
        String value = arg.substring(eqPos + 1);
        if (!flag.equals("--current-folder")) throw new RuntimeException(errMsg);;
        return value;
    }
}
