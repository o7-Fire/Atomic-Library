package Atom.Utility;

import Atom.Exception.ShouldNotHappenedException;
import Atom.Math.Matrix;
import Atom.Math.Meth;
import Atom.Noise.SimplexNoise;

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
        int count = 2048;
        float[][] data = new float[count][count];
        SimplexNoise noise = new SimplexNoise();
        for (int i = 0; i < count; i++) {
            for (int j = 0; j < count; j++) {
                data[i][j] = noise.noiseNormalized((float) i / 256, (float) j / 256);
            }
        }
        float[] flat = Matrix.flattenMatrix(data);
        float min = Meth.min(flat), max = Meth.max(flat);
        System.out.println("min: " + min + " max: " + max);
        visualize2DArray(data);
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
        //save to file
        try {
            ImageIO.write(img, "png", new java.io.File("test.png"));
        }catch(IOException e){
            e.printStackTrace();
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
