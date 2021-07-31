package Atom.Math;

import Atom.Utility.Random;

import java.util.Arrays;

public class Array {
    public static String visualize(Object[] arr) {
        return visualize(arr, System.getProperty("line.separator"));
    }
    
    //add anti dejaVu
    public static void random(Object[] arr) {
        for (Object element : arr) {
            Class<?> eClass = element.getClass();
            if (eClass == byte[].class) random((byte[]) element);
            else if (eClass == short[].class) random((short[]) element);
            else if (eClass == int[].class) random((int[]) element);
            else if (eClass == long[].class) random((long[]) element);
            else if (eClass == char[].class) random((char[]) element);
            else if (eClass == float[].class) random((float[]) element);
            else if (eClass == double[].class) random((double[]) element);
            else if (eClass == boolean[].class) random((boolean[]) element);
            else random((Object[]) element);
        }
    }
    
    
    public static String visualize(Object[] arr, String lineSeparator) {
        String deep = " " + Arrays.deepToString(arr).substring(1);
        deep = deep.replace("],", "]" + lineSeparator);
        deep = deep.substring(0, deep.length() - 1);
        deep = deep.replaceAll("\\[\\[", "[");
        deep = deep.replaceAll("]]", "]" + lineSeparator);
        return deep;
    }
    
    public static void random(boolean[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Random.getBool();
        }
    }
    
    public static void random(char[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (char) Random.getInt(Character.MIN_VALUE, Character.MAX_VALUE);
        }
    }
    
    public static void random(short[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (short) Random.getInt(Short.MIN_VALUE, Short.MAX_VALUE);
        }
    }
    
    public static void random(byte[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (byte) Random.getInt(Byte.MIN_VALUE, Byte.MAX_VALUE);
        }
    }
    
    public static void random(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Random.getInt();
        }
    }
    
    public static void random(double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Random.getDouble();
        }
    }
    
    public static void random(long[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Random.getLong();
        }
    }
    
    public static void random(float[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Random.getFloat();
        }
    }
    
    public static float[] randomFloat(int size) {
        float[] arr = new float[size];
        random(arr);
        return arr;
    }
    
    public static double[] randomDouble(int size) {
        double[] arr = new double[size];
        random(arr);
        return arr;
    }
    
    public static long[] randomLong(int size) {
        long[] arr = new long[size];
        random(arr);
        return arr;
    }
    
    public static long[] randomLong() {
        return randomLong(Random.getInt(1, 10));
    }
    
    public static double[] randomDouble() {
        return randomDouble(Random.getInt(1, 10));
    }
    
    public static float[] randomFloat() {
        return randomFloat(Random.getInt(1, 10));
    }
    
    public static int[] randomInteger() {
        return randomInteger(Random.getInt(1, 10));
    }
    
    public static int[] randomInteger(int size) {
        int[] arr = new int[size];
        random(arr);
        return arr;
    }
}
