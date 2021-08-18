package Atom.Algorithm.Evolution;

@FunctionalInterface
public interface ExterminationPolicy<G> {
    
    boolean shouldExterminate(double scores, double average, double min, double max, G population);
}
