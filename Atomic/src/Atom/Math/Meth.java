package Atom.Math;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Meth {


    public static double normalizePrecision(long x, long min, long max){
        double average = (double) (min + max) / 2;
        double range = (double) (max - min) / 2;
        return (double) (x - average) / range;
    }
    
    public static float normalize(long x, long min, long max) {
        float average = (float) (min + max) / 2;
        float range = (float) (max - min) / 2;
        return (float) (x - average) / range;
    }
    
    /**
     * example:
     * x = 0.45
     * min = 0.45
     * max = 0.55
     * return 0
     *
     * @param x   0.45
     * @param min 0.45
     * @param max 0.55
     * @return 0
     */
    public static double normalize(float x, float min, float max) {
        return (x - min) / (max - min);
    }
    
    public static long max(Long[] arr) {
        List<Long> b = Arrays.asList(arr);
        return Collections.max(b);
    }
    
    public static float interpolate(float x0, float x1, float alpha) {
        return x0 * (1 - alpha) + alpha * x1;
    }
    
    public static double interpolate(double x0, double x1, double alpha) {
        return x0 * (1 - alpha) + alpha * x1;
    }
    
    public static long min(Long[] arr) {
        List<Long> b = Arrays.asList(arr);
        return Collections.min(b);
    }
    
    public static float max(float... arr) {
        if (arr.length == 0) return 0;
        float min = arr[0];
        for (float l : arr)
            min = Math.max(l, min);
        return min;
    }
    
    public static double max(double... arr) {
        if (arr.length == 0) return 0;
        double min = arr[0];
        for (double l : arr)
            min = Math.max(l, min);
        return min;
    }
    
    public static short max(short... arr) {
        if (arr.length == 0) return 0;
        int min = arr[0];
        for (int l : arr)
            min = Math.max(l, min);
        return (short) min;
    }
    
    public static byte max(byte... arr) {
        if (arr.length == 0) return 0;
        int min = arr[0];
        for (int l : arr)
            min = Math.max(l, min);
        return (byte) min;
    }
    
    public static int max(int... arr) {
        if (arr.length == 0) return 0;
        int min = arr[0];
        for (int l : arr)
            min = Math.max(l, min);
        return min;
    }
    
    public static long max(long... arr) {
        if (arr.length == 0) return 0;
        long min = arr[0];
        for (long l : arr)
            min = Math.max(l, min);
        return min;
    }
    
    public static float min(float... arr) {
        if (arr.length == 0) return 0;
        float min = arr[0];
        for (float l : arr)
            min = Math.min(l, min);
        return min;
    }
    
    public static double min(double... arr) {
        if (arr.length == 0) return 0;
        double min = arr[0];
        for (double l : arr)
            min = Math.min(l, min);
        return min;
    }
    
    public static short min(short... arr) {
        if (arr.length == 0) return 0;
        int min = arr[0];
        for (short l : arr)
            min = Math.min(l, min);
        return (short) min;
    }
    
    public static byte min(byte... arr) {
        if (arr.length == 0) return 0;
        int min = arr[0];
        for (byte l : arr)
            min = Math.min(l, min);
        return (byte) min;
    }
    
    public static int min(int... arr) {
        if (arr.length == 0) return 0;
        int min = arr[0];
        for (int l : arr)
            min = Math.min(l, min);
        return min;
    }
    
    public static long min(long... arr) {
        if (arr.length == 0) return 0;
        long min = arr[0];
        for (long l : arr)
            min = Math.min(l, min);
        return min;
    }
    
    //primitive
    public static double avg(boolean... arr) {
        long sum = 0;
        int length = 0;
        for (boolean l : arr) {
            sum += l ? 1 : 0;
            length++;
        }
        return (double) sum / length;
    }
    
    public static double avg(double... arr) {
        double sum = 0;
        int length = 0;
        for (double l : arr) {
            sum += l;
            length++;
        }
        return sum / length;
    }
    
    public static double avg(float... arr) {
        double sum = 0;
        int length = 0;
        for (double l : arr) {
            sum += l;
            length++;
        }
        return (double) sum / length;
    }
    
    public static double avg(byte... arr) {
        long sum = 0;
        int length = 0;
        for (long l : arr) {
            sum += l;
            length++;
        }
        return (double) sum / length;
    }
    
    public static double avg(char... arr) {
        long sum = 0;
        int length = 0;
        for (long l : arr) {
            sum += l;
            length++;
        }
        return (double) sum / length;
    }
    
    public static double avg(int... arr) {
        long sum = 0;
        int length = 0;
        for (long l : arr) {
            sum += l;
            length++;
        }
        return (double) sum / length;
    }
    
    public static double avg(short... arr) {
        long sum = 0;
        int length = 0;
        for (long l : arr) {
            sum += l;
            length++;
        }
        return (double) sum / length;
    }
    
    public static double avg(long... arr) {
        long sum = 0;
        int length = 0;
        for (long l : arr) {
            sum += l;
            length++;
        }
        return (double) sum / length;
    }
    
    public static double avg(Iterator<Long> arr) {
        long sum = 0;
        int length = 0;
        
        while (arr.hasNext()) {
            long l = arr.next();
            sum += l;
            length++;
        }
        return (double) sum / length;
    }
    
    public static double avg(Iterable<Long> arr) {
        long sum = 0;
        int length = 0;
        for (long l : arr) {
            sum += l;
            length++;
        }
        return (double) sum / length;
    }
    
    public static int positive(int i) {
        if (i < 0) return -i;
        return i;
    }
    
    public static int negative(int i) {
        if (i > 0) return -i;
        return i;
    }
    
    public static float positive(float i) {
        if (i < 0) return -i;
        return i;
    }
    
    public static float negative(float i) {
        if (i > 0) return -i;
        return i;
    }
    
    public static long positive(long i) {
        if (i < 0) return -i;
        return i;
    }
    
    public static long negative(long i) {
        if (i > 0) return -i;
        return i;
    }
    
    public static double positive(double i) {
        if (i < 0) return -i;
        return i;
    }
    
    public static double negative(double i) {
        if (i > 0) return -i;
        return i;
    }
    
    //TODO add clamp methods
    public static float clamp(float i, float min, float max) {
        if (i < min) return min;
        return Math.min(i, max);
    }
    
    public static int clamp(int i, int min, int max) {
        if (i < min) return min;
        return Math.min(i, max);
    }
    
    public static long clamp(long i, long min, long max) {
        if (i < min) return min;
        return Math.min(i, max);
    }
    
    public static double clamp(double i, double min, double max) {
        if (i < min) return min;
        return Math.min(i, max);
    }
    
    public static float error = 0.00001f;
    
    //floating point equality
    public static boolean equals(float a, float b, float err) {
        return Math.abs(a - b) < err;
    }
    
    public static boolean equals(double a, double b, double err) {
        return Math.abs(a - b) < err;
    }
    
    public static boolean equals(long a, long b, long err) {
        return Math.abs(a - b) < err;
    }
    
    
    public static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }
    
    public static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
    
    public static int lerp(int a, int b, float t) {
        return (int) (a + (b - a) * t);
    }
    
    public static long lerp(long a, long b, float t) {
        return (long) (a + (b - a) * t);
    }
    
    public static short lerp(short a, short b, float t) {
        return (short) (a + (b - a) * t);
    }
    
    public static byte lerp(byte a, byte b, float t) {
        return (byte) (a + (b - a) * t);
    }
    
    public static float max(float[][] data) {
        float max = Float.NEGATIVE_INFINITY;
        for (float[] arr : data) {
            for (float f : arr) {
                max = Math.max(max, f);
            }
        }
        return max;
    }
    
    public static float min(float[][] data) {
        float min = Float.POSITIVE_INFINITY;
        for (float[] arr : data) {
            for (float f : arr) {
                min = Math.min(min, f);
            }
        }
        return min;
    }
}
