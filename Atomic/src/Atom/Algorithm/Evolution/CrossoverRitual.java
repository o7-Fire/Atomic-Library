package Atom.Algorithm.Evolution;

@FunctionalInterface
public interface CrossoverRitual<G> {
    G crossover(G g1, G g2);
}
