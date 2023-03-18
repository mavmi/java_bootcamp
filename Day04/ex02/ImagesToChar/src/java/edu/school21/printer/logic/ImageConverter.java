package edu.school21.printer.logic;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import com.diogonunes.jcolor.*;

public class ImageConverter {
    private Attribute black;
    private Attribute white;
    
    public ImageConverter(String black, String white){
        this.black = stringToAttribute(black);
        this.white = stringToAttribute(white);
    }

    public void printImage(){
        try {
            BufferedImage image = ImageIO.read(ImageConverter.class.getResource("/resources/image.bmp"));
            for (int y = 0; y < image.getHeight(); y++){
                for (int x = 0; x < image.getWidth(); x++){
                    int color = image.getRGB(x, y);
                    if (color == Color.black.getRGB()){
                        System.out.print(Ansi.colorize(" ", black));
                    } else if (color == Color.white.getRGB()){
                        System.out.print(Ansi.colorize(" ", white));
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

    private Attribute stringToAttribute(String colorName){
        if (colorName.toLowerCase().equals("cyan")){
            return Attribute.CYAN_BACK();
        } else if (colorName.toLowerCase().equals("green")){
            return Attribute.GREEN_BACK();
        } else if (colorName.toLowerCase().equals("magenta")){
            return Attribute.MAGENTA_BACK();
        } else if (colorName.toLowerCase().equals("red")){
            return Attribute.RED_BACK();
        } else if (colorName.toLowerCase().equals("yellow")){
            return Attribute.YELLOW_BACK();
        } else {
            return Attribute.WHITE_BACK();
        }
    }
}
