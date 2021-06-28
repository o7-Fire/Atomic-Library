package Atom.Utility;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Meth {
    public static double normalizePrecision(long x, long min, long max){
        double average      = (double) (min + max) / 2;
        double range        = (double) (max - min) / 2;
        double normalized_x = (double) (x - average) / range;
        return normalized_x;
    }
    public static float normalize(long x, long min, long max){
        float average      = (float) (min + max) / 2;
        float range        = (float) (max - min) / 2;
        float normalized_x = (float) (x - average) / range;
        return normalized_x;
    }
    public static long max(Long[] arr) {
        List<Long> b = Arrays.asList(arr);
        return Collections.max(b);
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
        return (double) sum / length;
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
}
