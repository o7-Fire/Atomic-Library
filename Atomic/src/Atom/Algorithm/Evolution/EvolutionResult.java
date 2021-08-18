package Atom.Algorithm.Evolution;

import Atom.Math.Meth;
import Atom.Struct.PoolObject;

public class EvolutionResult<G> implements PoolObject.Object {
    protected G[] population;
    protected long generation;
    protected double[] scores;
    protected double averageScore;
    
    public EvolutionResult(G[] population, long generation, double[] scores) {
        this.population = population;
        this.generation = generation;
        this.scores = scores;
        this.averageScore = Meth.avg(scores);
    }
    
    @Override
    public void reset() {
        scores = null;
        generation = 0;
        population = null;
        averageScore = 0;
    }
    
    @Override
    public String toString() {
        return "\ngeneration=" + generation + "\nAverageScores=" + averageScore;
    }
}
