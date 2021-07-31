package Atom.Math;

import java.util.ArrayList;
import java.util.Arrays;

public class Matrix extends Array {
    
    
    public static long[] flattenMatrix(long[][] arr) {
        int count = 0;
        for (long[] i : arr) {
            count += i.length;
        }
        long[] vec = new long[count];
        int i = 0;
        for (long[] ts : arr) {
            for (long e : ts) {
                vec[i++] = e;
            }
        }
        return vec;
    }
    
    public static double[] flattenMatrix(double[][] arr) {
        int count = 0;
        for (double[] i : arr) {
            count += i.length;
        }
        double[] vec = new double[count];
        int i = 0;
        for (double[] ts : arr) {
            for (double e : ts) {
                vec[i++] = e;
            }
        }
        return vec;
    }
    
    public static float[] flattenMatrix(float[][] arr) {
        int count = 0;
        for (float[] i : arr) {
            count += i.length;
        }
        float[] vec = new float[count];
        int i = 0;
        for (float[] ts : arr) {
            for (float e : ts) {
                vec[i++] = e;
            }
        }
        return vec;
    }
    
    public static char[] flattenMatrix(char[][] arr) {
        int count = 0;
        for (char[] i : arr) {
            count += i.length;
        }
        char[] vec = new char[count];
        int i = 0;
        for (char[] ts : arr) {
            for (char e : ts) {
                vec[i++] = e;
            }
        }
        return vec;
    }
    
    public static short[] flattenMatrix(short[][] arr) {
        int count = 0;
        for (short[] i : arr) {
            count += i.length;
        }
        short[] vec = new short[count];
        int i = 0;
        for (short[] ts : arr) {
            for (short e : ts) {
                vec[i++] = e;
            }
        }
        return vec;
    }
    
    public static byte[] flattenMatrix(byte[][] arr) {
        int count = 0;
        for (byte[] i : arr) {
            count += i.length;
        }
        byte[] vec = new byte[count];
        int i = 0;
        for (byte[] ts : arr) {
            for (byte e : ts) {
                vec[i++] = e;
            }
        }
        return vec;
    }
    
    public static int[] flattenMatrix(int[][] arr) {
        int count = 0;
        for (int[] i : arr) {
            count += i.length;
        }
        int[] vec = new int[count];
        int i = 0;
        for (int[] ts : arr) {
            for (int e : ts) {
                vec[i++] = e;
            }
        }
        return vec;
    }
    
    public static <T> T[] flattenMatrix(T[][] arr) {
        ArrayList<T> list = new ArrayList<>();
        for (T[] ts : arr) {
            list.addAll(Arrays.asList(ts));
        }
        if (list.size() == 0) return null;
        return list.toArray(arr[0]);
    }
}
