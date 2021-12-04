package Atom.Utility;

import Atom.Exception.ShouldNotHappenedException;
import Atom.Math.Array;
import Atom.Math.Matrix;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtility {
    //run configuration
    //Classpath: Atom.Desktop.test
    //Class Atom.Utility.ImageUtility
    //JVM: 9+
    public static void main(String[] args) {
        float[][] data2d = Matrix.randomFloat(256, 256);
        float[] data1d = Array.randomFloat(256);
        OpenSimplexNoise noise = new OpenSimplexNoise();
        float[][] simplex = new float[256][256];
        for (int i = 0; i < simplex.length; i++) {
            for (int j = 0; j < simplex[i].length; j++) {
                simplex[i][j] = (float) noise.eval(i, j);
            }
        }
        visualize2DArray(simplex);
        visualize2DArray(data2d);
        visualize1DArray(data1d);
        
    }
    
    public static byte[] encodeImage(BufferedImage image) {
        return encodeImage(image, "png");
    }
    
    public static byte[] encodeImage(BufferedImage image, String format) {
        format = format.toLowerCase();// ?
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, format, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        }catch(IOException e){
            throw new ShouldNotHappenedException(e);
        }
    }
    
    public static JFrame visualize1DArray(float[] data) {
        if (data == null) return null;
        if (data.length == 0) return null;
        int width = data.length;
        final BufferedImage img = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        int lastX = 0, lastY = 0;
        g.setColor(Color.WHITE);
        for (int i = 0; i < width; i++) {
            float c = data[i];
            int y = (int) ((c + 1) * (width / 2));
            g.drawLine(lastX, lastY, i, y);
            lastX = i;
            lastY = y;
        }
        JFrame frame = new JFrame("Image Test: " + width);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.clearRect(0, 0, getWidth(), getHeight());
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                // Or _BICUBIC
                g2d.scale(2, 2);
                g2d.drawImage(img, 0, 0, this);
            }
        };
        panel.setPreferredSize(new Dimension(width, width));
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
        return frame;
    }
    
    public static JFrame visualize2DArray(float[][] data) {
        if (data == null) return null;
        if (data.length == 0) return null;
        int width = data.length;
        int height = data[0].length;
        final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) img.getGraphics();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                float c = data[i][j];
                
                try {
                    g.setColor(new Color(c, c, c));
                }catch(IllegalArgumentException e){
                    System.err.println("Invalid Range: " + c + " at " + i + "," + j);
                    g.setColor(c < 0 ? Color.BLACK : Color.WHITE);
                }
                g.fillRect(i, j, 1, 1);
            }
        }
        
        JFrame frame = new JFrame("Image Test: " + width + "x" + height);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.clearRect(0, 0, getWidth(), getHeight());
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                // Or _BICUBIC
                g2d.scale(2, 2);
                g2d.drawImage(img, 0, 0, this);
            }
        };
        panel.setPreferredSize(new Dimension(width * 2, height * 2));
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
        return frame;
    }
    
    public enum ImageType {
        JPG, PNG, GIF, TIFF, BMP, WEBM;
    }
}
