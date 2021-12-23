package Atom.Noise;

public interface Noise {
    double noise(double x, double y);
    
    default double noise(double x, double y, double z) {
        return noise(x, y) % z;//lol
    }
}
