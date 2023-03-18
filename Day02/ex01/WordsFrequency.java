package ex01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordsFrequency {
    private String file1;
    private String file2;
    private Set<String> dict;
    private List<String> contentFile1;
    private List<String> contentFile2;
    private int[] vector1;
    private int[] vector2;

    public WordsFrequency(String file1, String file2){
        this.file1 = file1;
        this.file2 = file2;
        dict = new HashSet<>();
        contentFile1 = new ArrayList<>();
        contentFile2 = new ArrayList<>();
    }

    public void run(){
        appendContent(file1, contentFile1);
        appendContent(file2, contentFile2);
        saveDict();
        vector1 = analyzeFrequency(contentFile1);
        vector2 = analyzeFrequency(contentFile2);
        
        System.out.print("Similarity = ");
        System.out.printf("%.2f\n", calculateSimilarity());
    }

    private void appendContent(String filePath, List<String> content){
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            
            String line;
            while (true){
                line = reader.readLine();
                if (line == null) break;
                for (String word : line.split(" ")){
                    content.add(word);
                    dict.add(word);
                }
            }

        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    private void saveDict(){
        try (BufferedWriter outputStream = new BufferedWriter(new FileWriter("dictionary.txt"))){
            for (String word : dict){
                outputStream.write(word);
                outputStream.write(" ");
            }
            outputStream.write("\n");
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    private int[] analyzeFrequency(List<String> content){
        int[] vector = new int[dict.size()];

        int i = 0;
        for (String dictWord : dict){
            for (String contentWord : content){
                if (dictWord.equals(contentWord)){
                    vector[i]++;
                }
            }
            i++;
        }

        return vector;
    }
    private double calculateSimilarity(){
        double exp = 1e-6;
        double numerator = 0;
        double denominator = 0;

        for (int i = 0; i < vector1.length; i++){
            numerator += vector1[i] * vector2[i];
        }

        double tmp1 = 0, tmp2 = 0;
        for (int i = 0; i < vector1.length; i++){
            tmp1 += vector1[i] * vector1[i];
        }
        for (int i = 0; i < vector2.length; i++){
            tmp2 += vector2[i] * vector2[i];
        }
        denominator = Math.sqrt(tmp1) * Math.sqrt(tmp2);

        if (Math.abs(denominator) < exp || Math.abs(numerator) < exp){
            return 0;
        } else {
            return numerator / denominator;
        }
    }
}
