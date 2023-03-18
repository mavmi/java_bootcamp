package edu.school21.printer.app;

import java.io.File;

import edu.school21.printer.logic.ImageConverter;

public class Program {
    public static void main(String[] args){
        try {
            String[] parsedArgs = parseArgs(args);
            new ImageConverter(parsedArgs[0].charAt(0), parsedArgs[1].charAt(0), parsedArgs[2]).printImage();
        } catch (RuntimeException e){
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    private static String[] parseArgs(String[] args){
        final String errMsg = "Error\n" +
                                "Usage: Program.java --black=[BLACK_SYMBOL] --white=[WHITE_SYMBOL] --image=[BMP_FILE_PATH]";
        
        if (args.length != 3) throw new RuntimeException(errMsg);

        String[] arr = new String[]{
            parseArg(args[0], "--black"),
            parseArg(args[1], "--white"),
            parseArg(args[2], "--image")
        };
        if (arr[0].length() != 1 || arr[1].length() != 1) throw new RuntimeException(errMsg);
        if (!(new File(arr[2]).exists())) throw new RuntimeException("File doesn't exist");

        return arr;
    }
    private static String parseArg(String arg, String awaitingKey){
        int eqPos = arg.indexOf('=');
        if (eqPos == -1) throw new RuntimeException();

        String key = arg.substring(0, eqPos);
        String value = arg.substring(eqPos + 1);
        if (!key.equals(awaitingKey)) throw new RuntimeException();
        return value;
    }
}
