package Atom.Algorithm.Evolution;

import Atom.Class.Factory;
import Atom.Class.StopCondition;
import Atom.Math.Array;
import Atom.Math.Meth;
import Atom.Struct.TriUnion;
import Atom.Struct.Union;
import Atom.Time.Time;
import Atom.Time.Timer;
import Atom.Utility.Pool;
import Atom.Utility.Random;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class EvolutionEngine<G> extends Thread {
    protected final ThreadLocal<Evaluator<G>> evaluator;
    protected final ExecutorService service;
    protected final Factory<G> factory;
    protected final ThreadLocal<G[]> population;
    protected final int maxTask;
    protected final ThreadLocal<double[]> scores;
    protected final StopCondition stopCondition;
    protected final Consumer<EvolutionResult<G>> resultHandler;
    protected final ExterminationPolicy<G> exterminationPolicy;
    protected final CrossoverPolicy<G> crossoverPolicy;
    protected final CrossoverRitual<G> crossoverRitual;
    protected final float mutationChance;
    
    private EvolutionEngine(ThreadLocal<Evaluator<G>> evaluator, ExecutorService service, Factory<G> factory, ThreadLocal<G[]> population, int maxTask, ThreadLocal<double[]> scores, StopCondition stopCondition, Consumer<EvolutionResult<G>> resultHandler, ExterminationPolicy<G> exterminationPolicy, CrossoverPolicy<G> crossoverPolicy, CrossoverRitual<G> crossoverRitual, float mutationChance) {
        this.evaluator = evaluator;
        this.service = service;
        this.factory = factory;
        this.population = population;
        this.maxTask = maxTask;
        this.scores = scores;
        this.stopCondition = stopCondition;
        this.resultHandler = resultHandler;
        this.exterminationPolicy = exterminationPolicy;
        this.crossoverPolicy = crossoverPolicy;
        this.crossoverRitual = crossoverRitual;
        this.mutationChance = mutationChance;
        
        
    }
    
    public static void main(String[] args) throws InterruptedException {
        Time[] histories = new Time[]{new Time(), new Time(), new Time(TimeUnit.MILLISECONDS, 1000)};
        System.out.println(Arrays.deepToString(histories));
        Arrays.sort(histories, Comparator.comparing(Time::getSrc));
        System.out.println(Arrays.deepToString(histories));
        histories.clone()[1].src = 1;
        System.out.println(Arrays.deepToString(histories));
        int geneLength = 10, population = 20;
        Factory<byte[]> factory = () -> Array.randomByte(geneLength);
        Factory<byte[][]> matrixFactory = () -> {
            byte[][] bytes = new byte[population][geneLength];
            for (int i = 0, bytesLength = bytes.length; i < bytesLength; i++) {
                bytes[i] = factory.obtain();
            }
            return bytes;
        };
        CrossoverRitual<byte[]> crossoverRitual = (s1, s2) -> {
            
            boolean b = Random.getBool();
            byte[] s3 = b ? s1 : s2;
            s3 = s3.clone();
            int length = Random.getInt(s3.length - 1);
            int start = Random.getInt(s3.length - length);
            int start1 = Random.getInt(s3.length - length);
            for (int i = 0; i < length; i++) {
                s3[start + i] = b ? s2[i + start1] : s1[i + start1];
            }
            return s3;
        };
        
        CrossoverPolicy<byte[]> crossoverPolicy = (gene1, gene2, average, max, min, score1, score2) -> {
            if (gene1 == gene2 || Arrays.equals(gene1, gene2)) return false;
            if ((score1 == max || score2 == max) || (score1 > average && score2 > average) && Random.getBool(0.7f))
                return true;
            return Random.getBool(0.4f);
        };
        ExterminationPolicy<byte[]> exterminationPolicy = (scores, average, min, max, population1) -> {
            if (scores < average && Random.getBool(0.7f)) return true;
            if (scores == min && Random.getBool(0.9f)) return true;
            return Random.getBool(0.01f);
        };
        Timer timer = new Timer(TimeUnit.SECONDS, 2);
        Consumer<EvolutionResult<byte[]>> resultHandler = s -> {
            if (timer.get()) System.out.println(s.toString());
        };
        Evaluator<byte[]> evaluator = s -> {
            long d = 0;
            long max = (long) s.length * Byte.MAX_VALUE;
            for (byte b : s) d += b;
            return (double) d / max;
        };
        EvolutionEngine<byte[]> evolutionEngine = new Builder<byte[]>()//
                .setFactory(factory)//
                .setPopulation(ThreadLocal.withInitial(matrixFactory))//
                .setCrossoverRitual(crossoverRitual)//
                .setCrossoverPolicy(crossoverPolicy)//
                .setExterminationPolicy(exterminationPolicy)//
                .setResultHandler(resultHandler)//
                .setEvaluator(ThreadLocal.withInitial(() -> evaluator))//
                .build();
        evolutionEngine.start();
        evolutionEngine.join();
    }
    
    
    protected void assignRandom(G[] population) {
        for (int i = 0; i < population.length; i++) {
            if (population[i] == null) population[i] = factory.obtain();
        }
    }
    
    protected void eval(double[] scores, G[] population) {
        if (scores.length != population.length)
            throw new IllegalArgumentException("Scores Length != Population Length(" + population.length + ", " + scores.length + ")");
        for (int i = 0; i < scores.length; i++) {
            scores[i] = evaluator.get().eval(population[i]);
        }
    }
    
    protected void exterminate(double[] scores, G[] population) {
        if (scores.length != population.length)
            throw new IllegalArgumentException("Scores Length != Population Length(" + population.length + ", " + scores.length + ")");
        double average = Meth.avg(scores), min = Meth.min(scores), max = Meth.max(scores);
        boolean unNilled = false;
        for (int i = 0; i < scores.length; i++) {
            boolean b = exterminationPolicy.shouldExterminate(scores[i], average, min, max, population[i]);
            if (Random.getBool(0.4f) && !unNilled) b = false;
            if (scores[i] == max && !unNilled) b = false;//shall survive
            if (b){
                population[i] = null;
            }else unNilled = true;
        }
        //if (!unNilled) throw new RuntimeException("All population is wiped out");
        
    }
    
    protected G crossover(G g1, G g2) {
        return crossoverRitual.crossover(g1, g2);
    }
    
    protected void sealFate(double[] scores, G[] population) {
        TriUnion<Double, Integer, G>[] pops = new TriUnion[population.length];
        for (int i = 0; i < pops.length; i++) {
            pops[i] = new TriUnion<>(scores[i], i, population[i]);
        }
        
        Arrays.sort(pops, Comparator.comparing(Union::getItemA));
        double average = Meth.avg(scores), min = Meth.min(scores), max = Meth.max(scores);
        
        for (TriUnion<Double, Integer, G> s : pops) {
            if (s.getItemC() == null){
                G candidate1 = null;
                G candidate2 = null;
                double candidate1Score = 0, candidate2Score = 0;
                while (candidate1 == null) {
                    int i = Random.getInt(population.length - 1);
                    candidate1 = population[i];
                    candidate1Score = scores[i];
                }
                while (candidate2 == null) {
                    int i = Random.getInt(population.length - 1);
                    candidate2 = population[i];
                    candidate2Score = scores[i];
                }
                
                
                if (Random.getBool(mutationChance)) population[s.getItemB()] = factory.obtain();
                else if (crossoverPolicy.allowed(candidate1, candidate2, average, max, min, candidate1Score, candidate2Score)){
                    population[s.getItemB()] = crossover(candidate1, candidate2);
                }
                
            }
        }
    }
    
    protected EvolutionResult<G> run0(long generation) {
        assignRandom(population.get());
        eval(scores.get(), population.get());
        exterminate(scores.get(), population.get());
        sealFate(scores.get(), population.get());
        return new EvolutionResult<>(population.get(), generation, scores.get());
    }
    
    @Override
    public void run() {
        int max = maxTask;
        long generation = 0;
        Future<EvolutionResult<G>>[] futuresResult = new Future[max];
        int index = 0;
        EngineStopCondition engineStopCondition = null;
        if (stopCondition instanceof EngineStopCondition){
            engineStopCondition = (EngineStopCondition) stopCondition;
        }
        
        while (!stopCondition.stop()) {
            long gen = generation++;
            futuresResult[index++] = service.submit(() -> run0(gen));
            if (index == max - 1){
                for (int i = 0, futuresResultLength = futuresResult.length; i < futuresResultLength; i++) {
                    Future<EvolutionResult<G>> s = futuresResult[i];
                    if (s == null) continue;
                    try {
                        EvolutionResult<G> f = s.get();
                        resultHandler.accept(f);
                    }catch(InterruptedException | ExecutionException e){
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                    futuresResult[i] = null;
                }
                index = 0;
            }
            if (engineStopCondition != null) engineStopCondition.setIndex(index).setGeneration(generation);
        }
    }
    
    public static abstract class EngineStopCondition implements StopCondition {
        protected long generation;
        protected int index;
        
        private void setGeneration(long generation) {
            this.generation = generation;
        }
        
        private EngineStopCondition setIndex(int index) {
            this.index = index;
            return this;
        }
    }
    
    public static class Builder<G> {
        protected ThreadLocal<Evaluator<G>> evaluator;
        protected ExecutorService service = Pool.parallelAsync;
        protected Factory<G> factory;
        protected ThreadLocal<G[]> population;
        protected int maxTask = Runtime.getRuntime().availableProcessors() * 4;
        protected ThreadLocal<double[]> scores;
        protected StopCondition stopCondition = new Timer(TimeUnit.SECONDS, 2);
        protected Consumer<EvolutionResult<G>> resultHandler = s -> {};
        protected ExterminationPolicy<G> exterminationPolicy;
        protected CrossoverPolicy<G> crossoverPolicy;
        protected CrossoverRitual<G> crossoverRitual;
        
        protected float mutationChance = 0.4f;
        
        public float getMutationChance() {
            return mutationChance;
        }
        
        public Builder<G> setMutationChance(float mutationChance) {
            this.mutationChance = mutationChance;
            return this;
        }
        
        public CrossoverRitual<G> getCrossoverRitual() {
            return crossoverRitual;
        }
        
        public Builder<G> setCrossoverRitual(CrossoverRitual<G> crossoverRitual) {
            this.crossoverRitual = crossoverRitual;
            return this;
        }
        
        public ExterminationPolicy<G> getExterminationPolicy() {
            return exterminationPolicy;
        }
        
        public Builder<G> setExterminationPolicy(ExterminationPolicy<G> exterminationPolicy) {
            this.exterminationPolicy = exterminationPolicy;
            return this;
        }
        
        public CrossoverPolicy<G> getCrossoverPolicy() {
            return crossoverPolicy;
        }
        
        public Builder<G> setCrossoverPolicy(CrossoverPolicy<G> crossoverPolicy) {
            this.crossoverPolicy = crossoverPolicy;
            return this;
        }
        
        public EvolutionEngine<G> build() {
            if (scores == null) scores = ThreadLocal.withInitial(() -> new double[population.get().length]);
            return new EvolutionEngine<>(evaluator, service, factory, population, maxTask, scores, stopCondition, resultHandler, exterminationPolicy, crossoverPolicy, crossoverRitual, mutationChance);
        }
        
        public ThreadLocal<Evaluator<G>> getEvaluator() {
            return evaluator;
        }
        
        public Builder<G> setEvaluator(ThreadLocal<Evaluator<G>> evaluator) {
            this.evaluator = evaluator;
            return this;
        }
        
        public ExecutorService getService() {
            return service;
        }
        
        public Builder<G> setService(ExecutorService service) {
            this.service = service;
            return this;
        }
        
        public Factory<G> getFactory() {
            return factory;
        }
        
        public Builder<G> setFactory(Factory<G> factory) {
            this.factory = factory;
            return this;
        }
        
        public ThreadLocal<G[]> getPopulation() {
            return population;
        }
        
        public Builder<G> setPopulation(ThreadLocal<G[]> population) {
            this.population = population;
            return this;
        }
        
        
        public int getMaxTask() {
            return maxTask;
        }
        
        public Builder<G> setMaxTask(int maxTask) {
            this.maxTask = maxTask;
            return this;
        }
        
        public ThreadLocal<double[]> getScores() {
            return scores;
        }
        
        public Builder<G> setScores(ThreadLocal<double[]> scores) {
            this.scores = scores;
            return this;
        }
        
        public StopCondition getStopCondition() {
            return stopCondition;
        }
        
        public Builder<G> setStopCondition(StopCondition stopCondition) {
            this.stopCondition = stopCondition;
            return this;
        }
        
        public Consumer<EvolutionResult<G>> getResultHandler() {
            return resultHandler;
        }
        
        public Builder<G> setResultHandler(Consumer<EvolutionResult<G>> resultHandler) {
            this.resultHandler = resultHandler;
            return this;
        }
    }
}
