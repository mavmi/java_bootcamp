package edu.school21.printer.logic;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageConverter {
    private char black;
    private char white;
    private String filePath;
    
    public ImageConverter(char black, char white, String filePath){
        this.black = black;
        this.white = white;
        this.filePath = filePath;
    }

    public void printImage(){
        try {
            BufferedImage image = ImageIO.read(new File(filePath));
            for (int y = 0; y < image.getHeight(); y++){
                for (int x = 0; x < image.getWidth(); x++){
                    int color = image.getRGB(x, y);
                    if (color == Color.black.getRGB()){
                        System.out.print(black);
                    } else if (color == Color.white.getRGB()){
                        System.out.print(white);
                    } else {
                        throw new RuntimeException("Unsupported color");
                    }
                }
                System.out.print("\n");
            }
        } catch (NullPointerException | IOException e){
            System.err.println(e.getMessage());
        }
    }
}
