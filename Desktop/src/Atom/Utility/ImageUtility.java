package Atom.Utility;

import Atom.Exception.ShouldNotHappenedException;
import Atom.Math.Matrix;
import Atom.Math.Meth;
import Atom.Noise.SimplexNoise;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtility {
    //run configuration
    //Classpath: Atom.Desktop.test
    //Class Atom.Utility.ImageUtility
    //JVM: 9+
    public static void main(String[] args) {
        int count = 2048;
    
        SimplexNoise noise = new SimplexNoise();
        float sample = noise.noiseNormalized(1.2f, 1.3f);//x y
        for (int x = 0; x < 1; x++) {
            for (int y = -5; y < 5; y++) {
                float[][] data = new float[count][count];
            
            
                for (int i = 0; i < count; i++) {
                    for (int j = 0; j < count; j++) {
                        data[i][j] = noise.noiseNormalized(((float) i / count) * 10 + x, ((float) j / count) * 10 + y);
                    }
                }
            
            
                float[] flat = Matrix.flattenMatrix(data);
                float min = Meth.min(flat), max = Meth.max(flat);
                System.out.println("min: " + min + " max: " + max);
                save2DArrayToImage(new File("test" + x + "_" + y + ".png"), data);
                System.out.println("test" + x + "_" + y + ".png");
            }
        }
    
    
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
    
    
    public static void mirror(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        for (int x = 0; x < width / 2; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                image.setRGB(width - x - 1, y, rgb);
            }
        }
    }
    
    public static void rotate(BufferedImage image, int steps) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);
                newImage.setRGB(y, x, rgb);
            }
        }
        image.setData(newImage.getData());
    }
    
    public static void flipHorizontally(BufferedImage image) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
    }
    
    //data2D[i % width][i / width] = data[i];
    public static BufferedImage save1DArrayToImage(File file, float[] data, int width, int height) {
        float[][] data2D = new float[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                data2D[i][j] = data[i * height + j];//swap j to i
            }
        }
        return save2DArrayToImage(file, data2D);
    }
    
    public static BufferedImage save2DArrayToImage(File file, float[][] data) {
        return save2DArrayToImage(file, data, Color.BLACK, Color.WHITE);
    }
    
    public static BufferedImage save2DArrayToImage(File file, float[][] data, Color low, Color high) {
        float[] ffs = Matrix.flattenMatrix(data);
        float min = Float.MAX_VALUE, max = Float.MIN_VALUE;
        min = Meth.min(ffs);
        max = Meth.max(ffs);
        return save2DArrayToImage(file, data, min, max, low, high);
    }
    
    static JFrame frame;
    static volatile BufferedImage img2D;
    static JPanel panel;
    static volatile String text = "";
    
    public static BufferedImage arrayTo2D(float[][] data, float min, float max, Color low, Color high) {
        int width = data.length;
        int height = data[0].length;
        final BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        float r, g, b;
        float maxR = high.getRed() / 255f, maxG = high.getGreen() / 255f, maxB = high.getBlue() / 255f;
        float minR = low.getRed() / 255f, minG = low.getGreen() / 255f, minB = low.getBlue() / 255f;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                float c = data[i][j];
                
                try {
                    r = Meth.lerp(minR, maxR, (c - min) / (max - min));
                    g = Meth.lerp(minG, maxG, (c - min) / (max - min));
                    b = Meth.lerp(minB, maxB, (c - min) / (max - min));
                    Color color = new Color(r, g, b);
                    g2d.setColor(color);
                }catch(IllegalArgumentException e){
                    System.err.println("Invalid Range: " + c + " at " + i + "," + j);
                    g2d.setColor(c < 0 ? low : high);
                }
                g2d.fillRect(i, j, 1, 1);
            }
        }
        return img;
    }
    
    public static BufferedImage save2DArrayToImage(File file, float[][] data, float min, float max, Color low, Color high) {
        BufferedImage img = arrayTo2D(data, min, max, low, high);
        //flipHorizontally(img);
        //rotate(img, 1);
        //save to file
        try {
            ImageIO.write(img, "png", file);
        }catch(IOException e){
            e.printStackTrace();
        }
        return img;
    }
    
    public static JFrame visualize2DArray(float[][] data, Color low, Color high) {
        float min = Meth.min(data);
        float max = Meth.max(data);
        return visualize2DArray(data, min, max, low, high);
    }
    
    public static JFrame visualize2DArray(float[][] data, float min, float max, Color low, Color high) {
        
        if (data == null) return null;
        if (data.length == 0) return null;
        int width = data.length;
        int height = data[0].length;
        img2D = arrayTo2D(data, min, max, low, high);
        text = "Min: " + min + " Max: " + max;
        if (frame != null){
            if (!frame.isValid()){
                frame = null;
                panel = null;
            }
        }
        if (frame == null){
            frame = new JFrame("Image Test: " + width + "x" + height);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            if (panel == null){
                panel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.clearRect(0, 0, getWidth(), getHeight());
                        
                        // Or _BICUBIC
                        g2d.scale(2, 2);
                        if (img2D != null) g2d.drawImage(img2D, 0, 0, this);
                        if (text != null) g2d.drawString(text, 10, 10);
                        g2d.drawString(Utility.getDate(), 10, 20);
                        //draw grid
                        g2d.setColor(Color.BLACK);
                        for (int i = 0; i < width; i++) {
                            g2d.drawLine(i, 0, i, height);
                            i += width / 10;
                        }
                        for (int i = 0; i < height; i++) {
                            g2d.drawLine(0, i, width, i);
                            i += height / 10;
                        }
                        
                    }
                };
                
                panel.setPreferredSize(new Dimension(width * 2, height * 2));
            }
            frame.getContentPane().add(panel);
            frame.pack();
            frame.setVisible(true);
        }
        
        panel.repaint();
        return frame;
    }
    
    public enum ImageType {
        JPG, PNG, GIF, TIFF, BMP, WEBM
    }
}
