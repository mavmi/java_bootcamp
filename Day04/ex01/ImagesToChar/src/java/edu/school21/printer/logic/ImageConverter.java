package edu.school21.printer.logic;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageConverter {
    private char black;
    private char white;
    
    public ImageConverter(char black, char white){
        this.black = black;
        this.white = white;
    }

    public void printImage(){
        try {
            BufferedImage image = ImageIO.read(ImageConverter.class.getResource("/resources/image.bmp"));
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
