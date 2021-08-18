package Atom.Algorithm.Evolution;

//mating requirement :troll:
@FunctionalInterface
public interface CrossoverPolicy<G> {
    boolean allowed(G gene1, G gene2, double average, double max, double min, double score1, double score2);
    
}
