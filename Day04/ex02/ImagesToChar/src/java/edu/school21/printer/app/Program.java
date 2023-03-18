package edu.school21.printer.app;

import edu.school21.printer.logic.ImageConverter;
import com.beust.jcommander.*;

@Parameters(separators = "=")
public class Program {
    @Parameter(names = "--black", required = true)
    private static String black;
    @Parameter(names = "--white", required = true)
    private static String white;

    public static void main(String[] args){
        JCommander.newBuilder()
            .addObject(new Program())
            .build()
            .parse(args);
        new ImageConverter(black, white).printImage();
    }
}
