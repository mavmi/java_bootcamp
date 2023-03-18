package ex01;

public class Program{
    public static void main(String[] args){
        int msgCount = 0;
        
        try {
            msgCount = parseArgs(args);
        } catch (RuntimeException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }

        ThreadHandler handler = new ThreadHandler();
        Printer egg = new Printer("Egg", msgCount, handler);
        Printer hen = new Printer("Hen", msgCount, handler);

        egg.start();
        hen.start();
    }

    private static int parseArgs(String[] args){
        final String errMsg = "Error\n" +
                                "Usage: Program.java --count=[COUNT_OF_PRINTS]";
        
        if (args.length != 1) throw new RuntimeException(errMsg);

        String arg = args[0];
        int eqPos = arg.indexOf('=');
        if (eqPos == -1) throw new RuntimeException(errMsg);

        String key = arg.substring(0, eqPos);
        String value = arg.substring(eqPos + 1);
        if (!key.equals("--count")) throw new RuntimeException(errMsg);
        try {
            int count = Integer.parseInt(value);
            if (count > 0) return count;
            else throw new NumberFormatException("Count must be positive number");
        } catch (NumberFormatException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}