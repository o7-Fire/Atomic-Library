package Atom.Math;

import Atom.Annotation.FuzzerSettings;
import Atom.Utility.Random;

import java.util.Arrays;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class Array {
    public static String visualize(Object[] arr) {
        return visualize(arr, System.getProperty("line.separator"));
    }
    
    
    public static void random(Object arr) {
        if (arr.getClass().isArray()){
            random((Object[]) arr);
        }
    }
    
    //TODO add anti dejaVu
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
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
    public static void random(boolean[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Random.getBool();
        }
    }
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
    public static void random(char[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (char) Random.getInt(Character.MIN_VALUE, Character.MAX_VALUE);
        }
    }
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
    public static void random(short[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (short) Random.getShort();
        }
    }
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
    public static void random(byte[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (byte) Random.getInt(Byte.MIN_VALUE, Byte.MAX_VALUE);
        }
    }
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
    public static void random(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Random.getInt();
        }
    }
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
    public static void random(double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Random.getDouble();
        }
    }
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
    public static void random(long[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Random.getLong();
        }
    }
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
    public static void random(float[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Random.getFloat();
        }
    }
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
    public static float[] randomFloat(int size) {
        float[] arr = new float[size];
        random(arr);
        return arr;
    }
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
    public static double[] randomDouble(int size) {
        double[] arr = new double[size];
        random(arr);
        return arr;
    }
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
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
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
    public static int[] randomInteger(int size) {
        int[] arr = new int[size];
        random(arr);
        return arr;
    }
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
    public static boolean[] randomBoolean(int size) {
        return new boolean[size];
    }
    
    public static boolean[] randomBoolean() {
        return randomBoolean(Random.getInt(100));
    }
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
    public static short[] randomShort(int size) {
        short[] shorts = new short[size];
        random(shorts);
        return shorts;
    }
    
    public static short[] randomShort() {
        return randomShort(Random.getInt(100));
    }
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
    public static byte[] randomByte(int size) {
        byte[] bytes = new byte[size];
        random(bytes);
        return bytes;
    }
    
    public static byte[] randomByte() {
        return randomByte(Random.getInt(100));
    }
    
    public static char[] randomChar() {
        return randomChar(Random.getInt(100));
    }
    
    @FuzzerSettings(maxInteger = 10, minInteger = 1)
    private static char[] randomChar(int size) {
        char[] chars = new char[size];
        random(chars);
        return chars;
    }
    
    public static Long[] boxArray(long[] arr) {
        Long[] longs = new Long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            longs[i] = arr[i];
        }
        return longs;
    }
    
    public static Short[] boxArray(short[] arr) {
        Short[] shorts = new Short[arr.length];
        for (int i = 0; i < arr.length; i++) {
            shorts[i] = arr[i];
        }
        return shorts;
    }
    
    public static Byte[] boxArray(byte[] arr) {
        Byte[] bytes = new Byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            bytes[i] = arr[i];
        }
        return bytes;
    }
    
    public static Boolean[] boxArray(boolean[] arr) {
        Boolean[] booleans = new Boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            booleans[i] = arr[i];
        }
        return booleans;
    }
    
    public static Float[] boxArray(float[] arr) {
        Float[] floats = new Float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            floats[i] = arr[i];
        }
        return floats;
    }
    
    public static Double[] boxArray(double[] arr) {
        Double[] doubles = new Double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            doubles[i] = arr[i];
        }
        return doubles;
    }
    
    public static Character[] boxArray(char[] arr) {
        Character[] characters = new Character[arr.length];
        for (int i = 0; i < arr.length; i++) {
            characters[i] = arr[i];
        }
        return characters;
    }
    
    public static Integer[] boxArray(int[] arr) {
        Integer[] integers = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            integers[i] = arr[i];
        }
        return integers;
    }
    
    public static int[] unboxArray(Integer[] arr) {
        int[] integers = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            integers[i] = arr[i];
        }
        return integers;
    }
    
    public static char[] unboxArray(Character[] arr) {
        char[] chars = new char[arr.length];
        for (int i = 0; i < arr.length; i++) {
            chars[i] = arr[i];
        }
        return chars;
    }
    
    public static short[] unboxArray(Short[] arr) {
        short[] shorts = new short[arr.length];
        for (int i = 0; i < arr.length; i++) {
            shorts[i] = arr[i];
        }
        return shorts;
    }
    
    public static byte[] unboxArray(Byte[] arr) {
        byte[] bytes = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            bytes[i] = arr[i];
        }
        return bytes;
    }
    
    public static long[] unboxArray(Long[] arr) {
        long[] longs = new long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            longs[i] = arr[i];
        }
        return longs;
    }
    
    public static float[] unboxArray(Float[] arr) {
        float[] floats = new float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            floats[i] = arr[i];
        }
        return floats;
    }
    
    public static double[] unboxArray(Double[] arr) {
        double[] doubles = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            doubles[i] = arr[i];
        }
        return doubles;
    }
    
    public static boolean[] unboxArray(Boolean[] arr) {
        boolean[] booleans = new boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            booleans[i] = arr[i];
        }
        return booleans;
    }
    
    public static <T> void random(Supplier<T> supplier, T[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = supplier.get();
        }
    }
    
    public static <T> T[] getArray(Supplier<T> supplier, Class<T> clazz, int size) {
        T[] t = create1D(clazz, size);
        random(supplier, t);
        return t;
    }
    
    @FuzzerSettings(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static <T> T[] create1D(Class<T> clazz, int size) {
        return (T[]) java.lang.reflect.Array.newInstance(clazz, size);
    }
    
    @FuzzerSettings(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static <T> T[][] create2D(Class<T> clazz, int a, int b) {
        return (T[][]) java.lang.reflect.Array.newInstance(clazz, a, b);
    }
    
    @FuzzerSettings(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static <T> T[][][] create3D(Class<T> clazz, int a, int b, int c) {
        return (T[][][]) java.lang.reflect.Array.newInstance(clazz, a, b, c);
    }
    
    @FuzzerSettings(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static <T> T[][][][] create4D(Class<T> clazz, int a, int b, int c, int d) {
        return (T[][][][]) java.lang.reflect.Array.newInstance(clazz, a, b, c, d);
    }
    
    @FuzzerSettings(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static <T> T[][][][][] create5D(Class<T> clazz, int a, int b, int c, int d, int e) {
        return (T[][][][][]) java.lang.reflect.Array.newInstance(clazz, a, b, c, d, e);
    }
    
    public static <T> T[] random(Supplier<T> supplier, Class<T> tClass) {
        return getArray(supplier, tClass, Random.getInt(1, 100));
    }
    
    public static int[] getRandomDimension() {
        return getRandomDimension(Random.getInt(1, 10));
    }
    
    @FuzzerSettings(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static int[] getRandomDimension(int rank) {
        int[] dimension = new int[rank];
        for (int i = 0; i < dimension.length; i++) {
            dimension[i] = Random.getInt(1, 20);
        }
        return dimension;
    }
}
