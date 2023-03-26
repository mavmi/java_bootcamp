package ex03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Program{
    public static void main(String[] args){
        int threadsCount = 0;
        try {
            threadsCount = parseArgs(args);
        } catch (RuntimeException e){
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        int i = 0;
        ArrayList<String> urlsList = readUrls();
        if (urlsList == null) System.exit(-1);
        Downloader[] downloaders = new Downloader[threadsCount];
        
        while (i < urlsList.size()){
            for (int j = 0; j < threadsCount && i < urlsList.size(); j++){
                if (downloaders[j] == null || downloaders[j].isFinished()){
                    downloaders[j] = new Downloader(j + 1, urlsList.get(i++), "File_" + i + ".jpg", i);
                    downloaders[j].start();
                    break;
                }
            }
        }
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
    private static ArrayList<String> readUrls(){
        ArrayList<String> urlsList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("./files_urls.txt"))){
            String line;
            while ((line = reader.readLine()) != null){
                urlsList.add(line);
            }
            return urlsList;
        } catch (IOException e){
            System.err.println(e.getMessage());
            return null;
        }
    }
}