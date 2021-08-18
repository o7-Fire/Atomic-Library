package Atom.Algorithm.Evolution;

@FunctionalInterface
public interface Evaluator<G> {
    
    double eval(G g);
    
}
