package ex01;

public class Program {
    public static void main(String[] args){
        if (args.length != 2){
            System.err.println(
                "Error\n" +
                "Usage: Program.java [file1] [file2]"
            );
            System.exit(-1);
        }
        
        try {
            new WordsFrequency(args[0], args[1]).run();
        } catch (Exception e){
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }
}
