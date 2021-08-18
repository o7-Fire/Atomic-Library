package Atom.Utility;

import Atom.Annotation.ParamClamp;
import Atom.Math.Array;
import Atom.String.WordGenerator;
import Atom.Struct.FunctionalPoolObject;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

//Random get more random when used really often
public class Random extends java.util.Random {
    public static ThreadLocal<java.util.Random> atomicRandom = ThreadLocal.withInitial(java.util.Random::new);
    public static final Map<Class<?>, Supplier<?>> randomSupplier = new HashMap<>();
    public static synchronized void seed(long seed) {
        atomicRandom.get().setSeed(seed);
    }
    
    public static void getNextBytes(byte[] bytes) {
        atomicRandom.get().nextBytes(bytes);
    }
    
    public static int getNextInt() {
        return atomicRandom.get().nextInt();
    }
    
    public static int getNextInt(int bound) {
        return atomicRandom.get().nextInt(bound);
    }
    
    public static long getNextLong() {
        return atomicRandom.get().nextLong();
    }
    
    public static boolean getNextBoolean() {
        return atomicRandom.get().nextBoolean();
    }
    
    public static float getNextFloat() {
        return atomicRandom.get().nextFloat();
    }
    
    public static double getNextDouble() {
        return atomicRandom.get().nextDouble();
    }
    
    public static synchronized double getNextGaussian() {
        return atomicRandom.get().nextGaussian();
    }
    
    @ParamClamp(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static IntStream getInts(long streamSize) {
        return atomicRandom.get().ints(streamSize);
    }
    
    public static IntStream getInts() {
        return atomicRandom.get().ints();
    }
    
    @ParamClamp(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static IntStream getInts(long streamSize, int randomNumberOrigin, int randomNumberBound) {
        return atomicRandom.get().ints(streamSize, randomNumberOrigin, randomNumberBound);
    }
    
    @ParamClamp(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static IntStream getInts(int randomNumberOrigin, int randomNumberBound) {
        return atomicRandom.get().ints(randomNumberOrigin, randomNumberBound);
    }
    
    @ParamClamp(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static LongStream getLongs(long streamSize) {
        return atomicRandom.get().longs(streamSize);
    }
    
    public static LongStream getLongs() {
        return atomicRandom.get().longs();
    }
    
    @ParamClamp(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static LongStream getLongs(long streamSize, long randomNumberOrigin, long randomNumberBound) {
        return atomicRandom.get().longs(streamSize, randomNumberOrigin, randomNumberBound);
    }
    
    @ParamClamp(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static LongStream getLongs(long randomNumberOrigin, long randomNumberBound) {
        return atomicRandom.get().longs(randomNumberOrigin, randomNumberBound);
    }
    
    @ParamClamp(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static DoubleStream getDoubles(long streamSize) {
        return atomicRandom.get().doubles(streamSize);
    }
    
    public static DoubleStream getDoubles() {
        return atomicRandom.get().doubles();
    }
    
    @ParamClamp(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static DoubleStream getDoubles(long streamSize, double randomNumberOrigin, double randomNumberBound) {
        return atomicRandom.get().doubles(streamSize, randomNumberOrigin, randomNumberBound);
    }
    
    @ParamClamp(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static DoubleStream getDoubles(double randomNumberOrigin, double randomNumberBound) {
        return atomicRandom.get().doubles(randomNumberOrigin, randomNumberBound);
    }
    
    public static String getString() {
        return getString(8);
    }
    
    @ParamClamp(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static String getString(int length) {
        return getString(length, 125);
    }
    
    @ParamClamp(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static String getString(int length, int max) {
        return getString(length, 33, max);
    }
    
    public static final HashSet<Class<?>> primitiveClazz = new HashSet<>();
    
    //what the fuck im doing
    static {
        primitiveClazz.addAll(Arrays.asList(int.class, boolean.class, short.class, long.class, float.class, double.class, byte.class, char.class));
        randomSupplier.put(boolean.class, Random::getBool);
        randomSupplier.put(byte.class, Random::getByte);
        randomSupplier.put(short.class, Random::getShort);
        randomSupplier.put(char.class, Random::getChar);
        randomSupplier.put(int.class, Random::getInt);
        randomSupplier.put(long.class, Random::getLong);
        randomSupplier.put(float.class, Random::getFloat);
        randomSupplier.put(double.class, Random::getDouble);
        randomSupplier.put(Boolean.class, Random::getBool);
        randomSupplier.put(Byte.class, Random::getByte);
        randomSupplier.put(Short.class, Random::getShort);
        randomSupplier.put(Character.class, Random::getChar);
        randomSupplier.put(Integer.class, Random::getInt);
        randomSupplier.put(Long.class, Random::getLong);
        randomSupplier.put(Float.class, Random::getFloat);
        randomSupplier.put(Double.class, Random::getDouble);
        //
        randomSupplier.put(boolean[].class, Array::randomBoolean);
        randomSupplier.put(byte[].class, Array::randomByte);
        randomSupplier.put(short[].class, Array::randomShort);
        randomSupplier.put(char[].class, Array::randomChar);
        randomSupplier.put(int[].class, Array::randomInteger);
        randomSupplier.put(long[].class, Array::randomLong);
        randomSupplier.put(float[].class, Array::randomFloat);
        randomSupplier.put(double[].class, Array::randomDouble);
        randomSupplier.put(Boolean[].class, () -> Array.boxArray(Array.randomBoolean()));
        randomSupplier.put(Byte[].class, () -> Array.boxArray(Array.randomByte()));
        randomSupplier.put(Short[].class, () -> Array.boxArray(Array.randomShort()));
        randomSupplier.put(Character[].class, () -> Array.boxArray(Array.randomChar()));
        randomSupplier.put(Integer[].class, () -> Array.boxArray(Array.randomInteger()));
        randomSupplier.put(Long[].class, () -> Array.boxArray(Array.randomLong()));
        randomSupplier.put(Float[].class, () -> Array.boxArray(Array.randomFloat()));
        randomSupplier.put(Double[].class, () -> Array.boxArray(Array.randomDouble()));
        //
        randomSupplier.put(Class.class, Random::getRandomPrimitiveClass);//java moment
        randomSupplier.put(StringBuilder.class, FunctionalPoolObject.StringBuilder::obtain);
        randomSupplier.put(String.class, WordGenerator::randomString);
        randomSupplier.put(String[].class, WordGenerator::randomWordArray);
        randomSupplier.put(CharSequence.class, WordGenerator::randomWord);
        randomSupplier.put(Object.class, randomSupplier.get(String.class));
        randomSupplier.put(Object[].class, randomSupplier.get(Long[].class));
    }
    
    public static int getRandomColor() {
        return atomicRandom.get().nextInt(0xffffff + 1);
    }
    
    public static String getRandomHexColor() {
        int getNextInt = Random.getInt(5000000, 16777215);
        return String.format("#%06x", getNextInt);
    }
    
    public static String getHex(int h) {
        return String.format("#%06x", h);
    }
    
    public static boolean getBool(float chance) {
        return getFloat() < chance;
    }
    
    public static float getFloat() {
        return atomicRandom.get().nextFloat();
    }
    
    public static double getDouble() {
        return atomicRandom.get().nextDouble();
    }
    
    public static long getLong() {
        return atomicRandom.get().nextLong();
    }
    
    public static boolean getBool() {
        return atomicRandom.get().nextBoolean();
    }
    
    public static int getInt() {
        return atomicRandom.get().nextInt();
    }
    
    public static int getInt(int max) {
        return getInt(0, max);
    }
    
    public static int getInt(int min, int max) {
        return atomicRandom.get().nextInt((max - min) + 1) + min;
    }
    
    public static <T> T getRandom(Iterator<T> arr) {
        ArrayList<T> rand = new ArrayList<>();
        while (arr.hasNext()) rand.add(arr.next());
        return getRandom(rand);
    }
    
    public static <T> T getRandom(Iterable<T> arr) {
        ArrayList<T> rand = new ArrayList<>();
        for (T e : arr)
            rand.add(e);
        return getRandom(rand);
    }
    
    public static <T> T getRandom(List<T> arrays) {
        if (arrays.size() == 0) return null;
        int rnd = getInt(arrays.size() - 1);
        return arrays.get(rnd);
    }
    
    public static <T> T getRandom(T[] arrays) {
        if (arrays.length == 0) return null;
        int rnd = getInt( arrays.length - 1);
        return arrays[rnd];
    }
    
    public static long getLong(long min, long max) {
        return min + ((long) (atomicRandom.get().nextDouble() * (max - min)));
    }
    
    public static long getLong(long max) {
        return getLong(0, max);
    }
    
    public static double getDouble(double min, double max) {
        return min + (atomicRandom.get().nextDouble() * (max - min));
    }
    
    public static double getDouble(double max) {
        return getDouble(0, max);
    }
    
    public static float getFloat(float min, float max) {
        return min + (atomicRandom.get().nextFloat() * (max - min));
    }
    
    public static float getFloat(float max) {
        return getFloat(0, max);
    }
    
    public static short getShort() {
        return (short) getInt(Short.MIN_VALUE, Short.MAX_VALUE);
    }
    
    public static byte getByte() {
        byte[] bytes = new byte[1];
        getNextBytes(bytes);
        return bytes[0];
    }
    
    @ParamClamp(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static String getString(int length, int min, int max) {
        if (length < 0) return "";
        StringBuilder sb = FunctionalPoolObject.StringBuilder.obtain();
        
        for (int i = 0; i < length; i++) {
            sb.append((char) getInt(min, max));
        }
        String s = sb.toString();
        FunctionalPoolObject.StringBuilder.free(sb);
        return s;
    }
    
    public static char getChar() {
        return (char) getInt(Character.MIN_VALUE, Character.MAX_VALUE);
    }
    
    public static byte getByte(byte min, byte max) {
        return (byte) (min + (getByte() * (max - min)));
    }
    
    public static Locale getLocale() {
        return Random.getRandom(Locale.getAvailableLocales());
    }
    
    public static void main(String[] args) {
        for (Map.Entry<Class<?>, Supplier<?>> s : randomSupplier.entrySet()) {
            Object o = s.getValue().get();
            if (!(o.getClass().isPrimitive() == s.getKey().isPrimitive())) continue;
            assert s.getKey().isInstance(o) || s.getKey() == o.getClass() : "Not instance: " + s.getKey().getCanonicalName() + ", " + o.getClass().getCanonicalName();
        }
    }
    
    public static Class<?> getRandomPrimitiveClass() {
        return getRandom(primitiveClazz);
    }
    
    public static <T> T getRandom(Class<T> type) {
        if (randomSupplier.containsKey(type)) return type.cast(randomSupplier.get(type));
        return null;
    }
}
