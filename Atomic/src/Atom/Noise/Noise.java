package Atom.Noise;

import java.io.Serializable;

public interface Noise extends Serializable {
    double noise(double x, double y);
    
    default float noiseNormalized(double x, double y) {
        return (float) ((noise(x, y) + 1) / 2);
    }
    
    default double noise(double x, double y, double z) {
        return noise(x, y) % z;//lol
    }
    
    default float noiseNormalized(double x, double y, double z) {
        return (float) ((noise(x, y, z) + 1) / 2);
    }
}
