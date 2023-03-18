package ex00;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SignatureReader {
    private int maxSigLen;

    public SignatureReader(){
        maxSigLen = -1;

        List<String> inputLines = readSignaturesFile("./signatures.txt");
        if (inputLines == null) {
            throw new RuntimeException("Invalid signatures file");
        }

        Map<String, List<Byte>> extToSig = parseSignaturesLines(inputLines);
        if (extToSig == null){
            throw new RuntimeException("Invalid signature");
        }

        run(extToSig);
    }

    private void run(Map<String, List<Byte>> extToSig){
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.print("> ");
            String inputLine = scanner.nextLine();
            if (inputLine.equals("42")) System.exit(0);
            try (InputStream inputStream = new FileInputStream(inputLine)){
                String resultExt = null;
                List<Byte> fileBytes = new ArrayList<>();
                while (inputStream.available() > 0){
                    fileBytes.add((byte)inputStream.read());

                    for (Map.Entry<String, List<Byte>> entry : extToSig.entrySet()){
                        String curExt = entry.getKey();
                        List<Byte> curSig = entry.getValue();
                        
                        if (fileBytes.size() < curSig.size()) continue;
                        for (int i = 0; i < curSig.size(); i++){
                            if (fileBytes.get(i) != curSig.get(i)) break;
                            if (i + 1 == curSig.size()) resultExt = curExt;
                        }
                        if (resultExt != null) break;
                    }

                    if (resultExt != null) break;
                    if (fileBytes.size() >= maxSigLen) break;
                }
                writeToFile(resultExt);

            } catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private void writeToFile(String ext){
        if (ext == null) {
            System.out.println("UNDEFINED");
            return;
        }

        try (OutputStream outputStream = new FileOutputStream("result.txt", true)){
            outputStream.write(ext.getBytes());
            outputStream.write("\n".getBytes());
            System.out.println("PROCESSED");
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
    
    private List<String> readSignaturesFile(String filePath){
        try (InputStream inputStream = new FileInputStream(filePath)){
            StringBuilder inputChars = new StringBuilder();

            while (inputStream.available() > 0){
                inputChars.append((char)inputStream.read());
            }

            StringBuilder tmp = new StringBuilder();
            List<String> lines = new ArrayList<>();
            for (int i = 0; i < inputChars.length(); i++){
                char c = inputChars.charAt(i);
                if (c == '\n'){
                    lines.add(tmp.toString());
                    tmp = new StringBuilder();
                } else {
                    tmp.append(c);
                }
            }

            return lines;
        } catch (Exception e){
            System.err.println(e.getMessage());
            return null;
        }
    }
    private Map<String, List<Byte>> parseSignaturesLines(List<String> lines){
        Map<String, List<Byte>> extToSig = new HashMap<>();

        for (String line : lines){
            int commaPos = line.indexOf(',');
            if (commaPos == -1) return null;

            String extName = line.substring(0, commaPos);
            List<Byte> sigList = new ArrayList<>();
            String sigStr = line.substring(commaPos + 1);
            if (sigStr.length() % 3 != 0) return null;
            for (int i = 0; i < sigStr.length(); i += 3){
                if (sigStr.charAt(i) != ' ') return null;
                int byte1 = hexToByte(sigStr.charAt(i + 1));
                int byte2 = hexToByte(sigStr.charAt(i + 2));
                if (byte1 == -1 || byte2 == -1) return null;
                sigList.add((byte)((byte1 << 4) + byte2));
            }

            extToSig.put(extName, sigList);
            if (sigList.size() > maxSigLen) maxSigLen = sigList.size();
        }

        return extToSig;
    }

    private static int hexToByte(char hex){
        if (hex == '0'){
            return 0;
        } else if (hex == '1') {
            return 1;
        } else if (hex == '2') {
            return 2;
        } else if (hex == '3') {
            return 3;
        } else if (hex == '4') {
            return 4;
        } else if (hex == '5') {
            return 5;
        } else if (hex == '6') {
            return 6;
        } else if (hex == '7') {
            return 7;
        } else if (hex == '8') {
            return 8;
        } else if (hex == '9') {
            return 9;
        } else if (hex == 'A') {
            return 10;
        } else if (hex == 'B') {
            return 11;
        } else if (hex == 'C') {
            return 12;
        } else if (hex == 'D') {
            return 13;
        } else if (hex == 'E') {
            return 14;
        } else if (hex == 'F') {
            return 15;
        } else {
            return -1;
        }
    }

}
