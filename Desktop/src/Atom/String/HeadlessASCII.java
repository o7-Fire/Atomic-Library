package Atom.String;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HeadlessASCII {
    public static String generateFromImage(BufferedImage bufferedImage, String a, String b){
        int height = bufferedImage.getHeight(), width = bufferedImage.getWidth();
        
        StringBuilder stringBuilder = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                stringBuilder.append(bufferedImage.getRaster().getSampleDouble(x,y, 0) > 0.5f ? a : b);
            }
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
    public static String generateFromWord(String text, int width, int height, String a, String b){
        return generateFromWord(text,width,height,24,12,a,b);
    }
    public static String generateFromWord(String text, int width, int height, int x, int y, String a, String b){
        BufferedImage bufferedImage = new BufferedImage(
                width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.drawString(text, x, y);
        return generateFromImage(bufferedImage,a,b);
    }
}
