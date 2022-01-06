package Atom.Math;

import Atom.Annotation.MethodFuzzer;
import Atom.Utility.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class Matrix {
    public static boolean[][] randomBoolean() {
        return randomBoolean(Random.getInt(1, 12));
    }
    
    public static byte[][] randomByte() {
        return randomByte(Random.getInt(1, 12));
    }
    
    public static short[][] randomShort() {
        return randomShort(Random.getInt(1, 12));
    }
    
    public static char[][] randomChar() {
        return randomChar(Random.getInt(1, 12));
    }
    
    public static int[][] randomInteger() {
        return randomInteger(Random.getInt(1, 12));
    }
    
    public static long[][] randomLong() {
        return randomLong(Random.getInt(1, 12));
    }
    
    public static float[][] randomFloat() {
        return randomFloat(Random.getInt(1, 12));
    }
    
    public static double[][] randomDouble() {
        return randomDouble(Random.getInt(1, 12));
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static float[][] randomFloat(int size) {
        float[][] matrix = new float[size][];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = Array.randomFloat();
        }
        return matrix;
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static double[][] randomDouble(int size) {
        double[][] matrix = new double[size][];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = Array.randomDouble();
        }
        return matrix;
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static boolean[][] randomBoolean(int size) {
        boolean[][] matrix = new boolean[size][];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = Array.randomBoolean();
        }
        return matrix;
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static byte[][] randomByte(int size) {
        byte[][] matrix = new byte[size][];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = Array.randomByte();
        }
        return matrix;
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static short[][] randomShort(int size) {
        short[][] matrix = new short[size][];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = Array.randomShort();
        }
        return matrix;
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static char[][] randomChar(int size) {
        char[][] matrix = new char[size][];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = Array.randomChar();
        }
        return matrix;
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static int[][] randomInteger(int size) {
        int[][] matrix = new int[size][];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = Array.randomInteger();
        }
        return matrix;
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static long[][] randomLong(int size) {
        long[][] matrix = new long[size][];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = Array.randomLong();
        }
        return matrix;
    }
    
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
    
    //TODO automate this reptitive shit or just copy paste idk
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static Object[] makeTensor(Class<?> type, int x, int y, int z) {
        if (type == char.class || type == Character.class) if (type.isPrimitive()) return new char[x][y][z];
        else return new Character[x][y][z];
        if (type == boolean.class || type == Boolean.class) if (type.isPrimitive()) return new boolean[x][y][z];
        else return new Boolean[x][y][z];
        if (type == int.class || type == Integer.class) if (type.isPrimitive()) return new int[x][y][z];
        else return new Integer[x][y][z];
        if (type == long.class || type == Long.class) if (type.isPrimitive()) return new long[x][y][z];
        else return new Long[x][y][z];
        if (type == double.class || type == Double.class) if (type.isPrimitive()) return new double[x][y][z];
        else return new Double[x][y][z];
        if (type == float.class || type == Float.class) if (type.isPrimitive()) return new float[x][y][z];
        else return new Float[x][y][z];
        if (type == short.class || type == Short.class) if (type.isPrimitive()) return new short[x][y][z];
        else return new Short[x][y][z];
        if (type == byte.class || type == Byte.class) if (type.isPrimitive()) return new byte[x][y][z];
        else return new Byte[x][y][z];
        throw new IllegalArgumentException("Not a primitive: " + type.getClass());
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static Object[] makeMatrix(Class<?> type, int x, int y) {
        if (type == char.class || type == Character.class) if (type.isPrimitive()) return new char[x][y];
        else return new Character[x][y];
        if (type == boolean.class || type == Boolean.class) if (type.isPrimitive()) return new boolean[x][y];
        else return new Boolean[x][y];
        if (type == int.class || type == Integer.class) if (type.isPrimitive()) return new int[x][y];
        else return new Integer[x][y];
        if (type == long.class || type == Long.class) if (type.isPrimitive()) return new long[x][y];
        else return new Long[x][y];
        if (type == double.class || type == Double.class) if (type.isPrimitive()) return new double[x][y];
        else return new Double[x][y];
        if (type == float.class || type == Float.class) if (type.isPrimitive()) return new float[x][y];
        else return new Float[x][y];
        if (type == short.class || type == Short.class) if (type.isPrimitive()) return new short[x][y];
        else return new Short[x][y];
        if (type == byte.class || type == Byte.class) if (type.isPrimitive()) return new byte[x][y];
        else return new Byte[x][y];
        throw new IllegalArgumentException("Not a primitive: " + type.getClass());
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static float[][] randomFloat(int w, int h) {
    
    
        float[][] matrix = new float[w][h];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = Array.randomFloat(h);
        }
        return matrix;


    }

    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static float[][] to2D(float[] f) {
        int x = (int) Math.sqrt(f.length);
        if (x * x != f.length) throw new IllegalArgumentException(f.length + " is not a square, specify size manually");
        return to2D(f, x, x);
    }

    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static float[][] to2D(float[] f, int i, int i1) {
        float[][] matrix = new float[i][i1];
        for (int x = 0; x < i; x++) {
            for (int y = 0; y < i1; y++) {
                matrix[x][y] = f[x + y * i];
            }
        }
        return matrix;
    }
}
