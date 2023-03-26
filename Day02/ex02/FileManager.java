package ex02;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Scanner;

class FileManagerException extends RuntimeException{
    public FileManagerException(String msg){
        super(msg);
    }
}

public class FileManager {
    private File cwd;
    
    public FileManager(String startDir){
        cwd = new File(startDir);
        if (!cwd.exists()) throw new FileManagerException(startDir + " doesn't exist");
        if (!cwd.isDirectory()) throw new FileManagerException(startDir + " is not directory");
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.print("> ");
            makeCall(scanner.nextLine());
        }
    }

    private void makeCall(String inputLine){
        if (inputLine.length() == 0) return;

        String[] args = inputLine.split(" ");
        String cmd = args[0];
        if (cmd.equals("mv")){
            mv(args);
        } else if (cmd.equals("ls")){
            ls(args);
        } else if (cmd.equals("cd")){
            cd(args);
        } else if (cmd.equals("exit")){
            exit(args);
        }
    }

    private void mv(String[] args){
        if (args.length != 3) return;

        try {
            Path whatPath = Paths.get(args[1]);
            Path wherePath = Paths.get(args[2]);
            if (!whatPath.isAbsolute()) whatPath = Paths.get(cwd.getAbsolutePath() + "/" + args[1]);
            if (!wherePath.isAbsolute()) wherePath = Paths.get(cwd.getAbsolutePath() + "/" + args[2]);
            
            File what = whatPath.toAbsolutePath().normalize().toFile();
            File where = wherePath.toAbsolutePath().normalize().toFile();

            if (!what.exists()) {
                System.out.println(what.getAbsolutePath());
                System.err.println(args[1] + " doesn't exist");
                return;
            }

            if (!where.exists()){
                what.renameTo(where);
            } else if (where.isFile()){
                System.err.println("File " + args[2] + " already exists");
                return;
            } else if (where.isDirectory()){
                boolean res = what.renameTo(Paths.get(where.getAbsolutePath() + "/" + what.getName()).toAbsolutePath().normalize().toFile());
                if (!res){
                    System.err.println("Error");
                }
            }
        } catch (InvalidPathException | UnsupportedOperationException e){
            System.err.println(e.getMessage());
        }
    }
    private void ls(String[] args){
        for (File file : cwd.listFiles()){
            System.out.println(file.getName() + " " + formatFileSize(getSize(file)));
        }
    }
    private void cd(String[] args){
        if (args.length != 2) return;
        String filePath = args[1];
        
        try {
            Path newCwdPath = Paths.get(filePath);
            if (!newCwdPath.isAbsolute()){
                newCwdPath = Paths.get(cwd.getAbsolutePath() + "/" + filePath);
            } 

            File newCwd = newCwdPath.toAbsolutePath().normalize().toFile();
            if (!newCwd.exists()) throw new FileManagerException(filePath + " doesn't exist");
            if (!newCwd.isDirectory()) throw new FileManagerException(filePath + " is not a directory");
            cwd = newCwd;
            System.out.println(cwd.getAbsolutePath());
        } catch (InvalidPathException e){
            System.err.println("Invalid path: " + filePath);
            System.err.println(e.getMessage());
        } catch (UnsupportedOperationException e){
            System.err.println("Unsupported path: " + filePath);
            System.err.println(e.getMessage());
        } catch (FileManagerException e){
            System.err.println(e.getMessage());
        }
    }
    private void exit(String[] args){
        System.exit(0);
    }

    private long getSize(File file){
        if (file.isFile()){
            return file.length();
        } else {
            long result = 0;
            for (File innerFile : file.listFiles()){
                result += getSize(innerFile);
            }
            return result;
        }
    }
    private String formatFileSize(long size){
        final String[] formats = new String[]{
            "B",
            "KB",
            "MB",
            "GB"
        };

        int formatIter = 0;
        double sizeD = (double)size;
        while (formatIter < formats.length){
            double newSizeD = sizeD / 1024;
            if (newSizeD < 1) break;
            sizeD = newSizeD;
            formatIter++;
        }

        return new DecimalFormat("0.00").format(sizeD) + " " + formats[formatIter];
    }
}
