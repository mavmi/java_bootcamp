package ex03;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Downloader extends Thread {
    private String fileUrl;
    private String fileName;
    private int fileNum;
    private boolean isFinished;
    private int id;

    public Downloader(int id, String fileUrl, String fileName, int fileNum){
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.fileNum = fileNum;
        this.id = id;
        isFinished = false;
    }   
    
    public void run(){
        printStartInfo();
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(fileUrl).openStream())){
            FileOutputStream outputStream = new FileOutputStream(fileName);

            byte[] buffer = new byte[4096];
            int bytesCount;
            while ((bytesCount = inputStream.read(buffer, 0, 4096)) > 0){
                outputStream.write(buffer, 0, bytesCount);
            }

            outputStream.close();
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
        printStopInfo();
        isFinished = true;
    }
    
    public boolean isFinished(){
        return isFinished;
    }

    synchronized private void printStartInfo(){
        System.out.println("Thread-" + id + " start download file number " + fileNum);
    }

    synchronized private void printStopInfo(){
        System.out.println("Thread-" + id + " finish download file number " + fileNum);
    }
}
