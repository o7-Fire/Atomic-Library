package Atom.Utility;

import Atom.Annotation.MethodFuzzer;
import Atom.Class.Factory;
import Atom.Math.Array;
import Atom.Math.Meth;
import Atom.String.WordGenerator;
import Atom.Struct.FunctionalPoolObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

//Random get more random when used really often
public class Random extends java.util.Random {
    //slow ?
    public static ThreadLocal<java.util.Random> atomicRandom = ThreadLocal.withInitial(java.util.Random::new);
    public static final Map<Class<?>, Factory<?>> randomSupplier = new HashMap<>();
    
    public static synchronized void seed(long seed) {
        get().setSeed(seed);
    }
    
    public static java.util.Random get() {
        return atomicRandom.get();
    }
    
    public static void getNextBytes(byte[] bytes) {
        get().nextBytes(bytes);
    }
    
    public static int getNextInt() {
        return get().nextInt();
    }
    
    public static int getNextInt(int bound) {
        return get().nextInt(bound);
    }
    
    public static long getNextLong() {
        return get().nextLong();
    }
    
    public static boolean getNextBoolean() {
        return get().nextBoolean();
    }
    
    public static float getNextFloat() {
        return get().nextFloat();
    }
    
    public static double getNextDouble() {
        return get().nextDouble();
    }
    
    public static synchronized double getNextGaussian() {
        return get().nextGaussian();
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static IntStream getInts(long streamSize) {
        return get().ints(streamSize);
    }
    
    public static IntStream getInts() {
        return get().ints();
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static IntStream getInts(long streamSize, int randomNumberOrigin, int randomNumberBound) {
        return get().ints(streamSize, randomNumberOrigin, randomNumberBound);
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static IntStream getInts(int randomNumberOrigin, int randomNumberBound) {
        return get().ints(randomNumberOrigin, randomNumberBound);
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static LongStream getLongs(long streamSize) {
        return get().longs(streamSize);
    }
    
    public static LongStream getLongs() {
        return get().longs();
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static LongStream getLongs(long streamSize, long randomNumberOrigin, long randomNumberBound) {
        return get().longs(streamSize, randomNumberOrigin, randomNumberBound);
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static LongStream getLongs(long randomNumberOrigin, long randomNumberBound) {
        return get().longs(randomNumberOrigin, randomNumberBound);
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static DoubleStream getDoubles(long streamSize) {
        return get().doubles(streamSize);
    }
    
    public static DoubleStream getDoubles() {
        return get().doubles();
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static DoubleStream getDoubles(long streamSize, double randomNumberOrigin, double randomNumberBound) {
        return get().doubles(streamSize, randomNumberOrigin, randomNumberBound);
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static DoubleStream getDoubles(double randomNumberOrigin, double randomNumberBound) {
        return get().doubles(randomNumberOrigin, randomNumberBound);
    }
    
    public static String getString() {
        return getString(8);
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static String getString(int length) {
        return getString(length, 125);
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
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
        return get().nextInt(0xffffff + 1);
    }
    
    public static int[] getIntegerArrayWithSum() {
        return getIntegerArrayWithSum(Random.getInt(200));
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static int[] getIntegerArrayWithSum(int sum) {
        return getIntegerArrayWithSum(sum, Random.getInt(20));
    }
    
    @MethodFuzzer(maxLong = 20, minLong = 1, minInteger = 1, maxInteger = 20)
    public static int[] getIntegerArrayWithSum(int sum, int count) {
        
        int[] vals = new int[count];
        sum -= count;
        
        for (int i = 0; i < count - 1; ++i) {
            vals[i] = getInt(sum);
        }
        vals[count - 1] = sum;
        
        java.util.Arrays.sort(vals);
        for (int i = count - 1; i > 0; --i) {
            vals[i] -= vals[i - 1];
        }
        for (int i = 0; i < count; ++i) {++vals[i];}
        return vals;
        
    }
    
    //generate for another type
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static float[] getFloatArrayWithSum(float sum, int count) {
        
        float[] vals = new float[count];
        sum -= count;
        
        for (int i = 0; i < count - 1; ++i) {
            vals[i] = getFloat(sum);
        }
        vals[count - 1] = sum;
        
        java.util.Arrays.sort(vals);
        for (int i = count - 1; i > 0; --i) {
            vals[i] -= vals[i - 1];
        }
        for (int i = 0; i < count; ++i) {++vals[i];}
        return vals;
        
    }
    
    public static float[] getFloatArrayWithSum() {
        return getFloatArrayWithSum(1);
    }
    
    public static float[] getFloatArrayWithSum(float sum) {
        return getFloatArrayWithSum(sum, getInt(1, 20));
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static double[] getDoubleArrayWithSum(double sum, int count) {
        
        double[] vals = new double[count];
        sum -= count;
        
        for (int i = 0; i < count - 1; ++i) {
            vals[i] = getDouble(sum);
        }
        vals[count - 1] = sum;
        
        java.util.Arrays.sort(vals);
        for (int i = count - 1; i > 0; --i) {
            vals[i] -= vals[i - 1];
        }
        for (int i = 0; i < count; ++i) {++vals[i];}
        return vals;
        
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static byte[] getByteArrayWithSum(byte sum, int count) {
        
        byte[] vals = new byte[count];
        sum -= count;
        
        for (int i = 0; i < count - 1; ++i) {
            vals[i] = getByte(sum);
        }
        vals[count - 1] = sum;
        
        java.util.Arrays.sort(vals);
        for (int i = count - 1; i > 0; --i) {
            vals[i] -= vals[i - 1];
        }
        for (int i = 0; i < count; ++i) {++vals[i];}
        return vals;
        
    }
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static short[] getShortArrayWithSum(short sum, int count) {
        
        short[] vals = new short[count];
        sum -= count;
        
        for (int i = 0; i < count - 1; ++i) {
            vals[i] = getShort(sum);
        }
        vals[count - 1] = sum;
        
        java.util.Arrays.sort(vals);
        for (int i = count - 1; i > 0; --i) {
            vals[i] -= vals[i - 1];
        }
        for (int i = 0; i < count; ++i) {++vals[i];}
        return vals;
        
    }
    
    public static short getShort(short min, short max) {
        return (short) (min + (getShort() * (max - min)));
    }
    
    public static short getShort(short max) {
        return getShort((short) 0, max);
    }
    
    
    private static byte getByte(byte max) {
        return getByte((byte) 0, max);
    }
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
    public static long[] getLongArrayWithSum(long sum, int count) {
        
        long[] vals = new long[count];
        sum -= count;
        
        for (int i = 0; i < count - 1; ++i) {
            vals[i] = getLong(sum);
        }
        vals[count - 1] = sum;
        
        java.util.Arrays.sort(vals);
        for (int i = count - 1; i > 0; --i) {
            vals[i] -= vals[i - 1];
        }
        for (int i = 0; i < count; ++i) {++vals[i];}
        return vals;
        
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
        return get().nextFloat();
    }
    
    public static double getDouble() {
        return get().nextDouble();
    }
    
    public static long getLong() {
        return get().nextLong();
    }
    
    public static boolean getBool() {
        return get().nextBoolean();
    }
    
    public static int getInt() {
        return get().nextInt();
    }

    //note add by 1 if used for get random array size
    //don't subtract by 1 if used for array index
    public static int getInt(int max) {
        return getInt(0, max);
    }

    public static int getInt(int min, int max) {
        return get().nextInt((max - min) + 1) + min;
    }

    public static <T> T get(Class<T> clazz) {
        Factory<T> t = (Factory<T>) randomSupplier.get(clazz);
        if (t == null) {
            return null;
        }
        return t.get();
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
    
    @NotNull
    public static <T> T getRandom(T[] arrays) {
        if (arrays.length == 0) return null;
        int rnd = getInt( arrays.length - 1);
        return arrays[rnd];
    }
    
    public static long getLong(long min, long max) {
        return min + ((long) (get().nextDouble() * (max - min)));
    }
    
    public static long getLong(long max) {
        return getLong(0, max);
    }
    
    public static double getDouble(double min, double max) {
        return min + (get().nextDouble() * (max - min));
    }
    
    @MethodFuzzer(maxLong = 1000, minLong = 1, minInteger = 1, maxInteger = 1000)
    public static float[][] generateWhiteNoise(int width, int height) {
        return generateWhiteNoise(new float[width][height]);
    }
    
    @MethodFuzzer(maxLong = 1000, minLong = 1, minInteger = 1, maxInteger = 1000)
    public static float[][] generateSmoothNoise(float[][] baseNoise, int octave) {
        int width = baseNoise.length;
        int height = baseNoise[0].length;
        
        float[][] smoothNoise = new float[width][height];
        
        int samplePeriod = 1 << octave; // calculates 2 ^ k
        float sampleFrequency = 1.0f / samplePeriod;
        
        for (int i = 0; i < width; i++) {
            // calculate the horizontal sampling indices
            int sample_i0 = (i / samplePeriod) * samplePeriod;
            int sample_i1 = (sample_i0 + samplePeriod) % width; // wrap around
            float horizontal_blend = (i - sample_i0) * sampleFrequency;
            
            for (int j = 0; j < height; j++) {
                // calculate the vertical sampling indices
                int sample_j0 = (j / samplePeriod) * samplePeriod;
                int sample_j1 = (sample_j0 + samplePeriod) % height; // wrap
                // around
                float vertical_blend = (j - sample_j0) * sampleFrequency;
                
                // blend the top two corners
                float top = Meth.interpolate(baseNoise[sample_i0][sample_j0],
                        baseNoise[sample_i1][sample_j0],
                        horizontal_blend);
                
                // blend the bottom two corners
                float bottom = Meth.interpolate(baseNoise[sample_i0][sample_j1],
                        baseNoise[sample_i1][sample_j1],
                        horizontal_blend);
                
                // final blend
                smoothNoise[i][j] = Meth.interpolate(top, bottom, vertical_blend);
            }
        }
        
        return smoothNoise;
    }
    
    
    @MethodFuzzer(maxLong = 1000, minLong = 1, minInteger = 1, maxInteger = 1000)
    public static float[][] generatePerlinNoise(int width, int height, float persistance, float amplitude) {
        return generatePerlinNoise(width, height, 5, persistance, amplitude);
    }
    
    /**
     * Generates Perlin Noise
     *
     * @param width       width of the noise array
     * @param height      height of the noise array
     * @param octaveCount number of octaves to generate
     * @param persistance how much each octave contributes to the final noise
     * @param amplitude   the maximum value of the noise
     * @return
     */
    @MethodFuzzer(maxLong = 1000, minLong = 1, minInteger = 1, maxInteger = 1000)
    public static float[][] generatePerlinNoise(int width, int height, int octaveCount, float persistance, float amplitude) {
        return generatePerlinNoise(generateWhiteNoise(width, height), octaveCount, persistance, amplitude);
    }
    
    
    /**
     * Generates Perlin Noise based on the white noise array
     * Parameters.
     *
     * @param baseNoise   - 2D array of floats
     * @param octaveCount - number of octaves to generate
     * @param persistance - how much each octave contributes to the final noise
     * @param amplitude   - the maximum possible height of the noise
     * @return - 2D array of floats
     */
    @MethodFuzzer(maxLong = 1000, minLong = 1, minInteger = 1, maxInteger = 1000)
    public static float[][] generatePerlinNoise(float[][] baseNoise, int octaveCount, float persistance, float amplitude) {
        int width = baseNoise.length;
        int height = baseNoise[0].length;
        
        float[][][] smoothNoise = new float[octaveCount][][]; // an array of 2D
        // arrays
        // containing
        
        
        // generate smooth noise
        for (int i = 0; i < octaveCount; i++) {
            smoothNoise[i] = generateSmoothNoise(baseNoise, i);
        }
        
        float[][] perlinNoise = new float[width][height];
        
        float totalAmplitude = 0.0f;
        
        // blend noise together
        for (int octave = octaveCount - 1; octave >= 0; octave--) {
            amplitude *= persistance;
            totalAmplitude += amplitude;
            
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
                }
            }
        }
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                perlinNoise[i][j] /= totalAmplitude;
                perlinNoise[i][j] = (float) (Math.floor(perlinNoise[i][j] * 25));
            }
        }
        
        return perlinNoise;
    }
    
    public static float[][] generateWhiteNoise(float[][] noise) {
        if (noise == null) return null;
        if (noise.length == 0) throw new IllegalArgumentException("Array cannot be empty");
        for (int i = 0; i < noise.length; i++) {
            for (int j = 0; j < noise[0].length; j++) {
                noise[i][j] = (float) (getDouble() % 1);
            }
        }
        
        return noise;
    }
    
    public static double getDouble(double max) {
        return getDouble(0, max);
    }
    
    public static float getFloat(float min, float max) {
        return min + (get().nextFloat() * (max - min));
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
    
    @MethodFuzzer(maxLong = 10, minLong = 1, minInteger = 1, maxInteger = 10)
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
        for (Map.Entry<Class<?>, Factory<?>> s : randomSupplier.entrySet()) {
            Object o = s.getValue().get();
            if (!(o.getClass().isPrimitive() == s.getKey().isPrimitive())) continue;
            assert s.getKey().isInstance(o) || s.getKey() == o.getClass() : "Not instance: " + s.getKey().getCanonicalName() + ", " + o.getClass().getCanonicalName();
        }
    }
    
    public static Class<?> getRandomPrimitiveClass() {
        return getRandom(primitiveClazz);
    }
    
    public static <T> T getRandom(Class<T> type) {
        if (randomSupplier.containsKey(type)){
            return (T) randomSupplier.get(type).get();
        }
        return null;
    }
}
