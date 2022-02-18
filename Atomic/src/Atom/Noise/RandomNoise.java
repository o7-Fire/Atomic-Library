package Atom.Noise;

import java.util.Random;

public class RandomNoise implements Noise {
    protected final Random random;
    
    
    public RandomNoise(long seed) {
        this.random = new Random(seed);
    }
    
    public RandomNoise() {
        this(new Random().nextLong());
    }
    
    @Override
    public double noise(double x, double y) {
        return random.nextDouble();
    }
    
    @Override
    public float noiseNormalized(double x, double y) {
        return random.nextFloat();
    }
    
    @Override
    public double noise(double x, double y, double z) {
        return random.nextDouble();
    }
    
    @Override
    public float noiseNormalized(double x, double y, double z) {
        
        return random.nextFloat();
    }
}
