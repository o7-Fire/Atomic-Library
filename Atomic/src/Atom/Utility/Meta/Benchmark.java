package Atom.Utility.Meta;

import Atom.Math.Meth;
import Atom.Time.Time;
import Atom.Utility.Utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Benchmark {
    protected String measurement;
    protected Time startTime = new Time(TimeUnit.MILLISECONDS);
    protected ArrayList<BenchmarkResult> results = new ArrayList<>();
    
    
    public Benchmark(TimeUnit unit) {
        measurement = Utility.capitalizeEnforce(unit.toString());
    }
    
    protected void printAdd(String method, double[] times) {
        results.add(new BenchmarkResult(method, times, this));
    }
    
    
    public void print(boolean sort) {
        System.out.println();
        System.out.println("Benchmark Results" + (sort ? ": Sorted by average times" : ""));
        System.out.println("----------------");
        if (sort) results.sort(null);
        for (BenchmarkResult r : results) {
            print(r.name, r.times);
        }
        results.clear();
        System.out.println();
        System.out.println("Total Time: " + startTime.elapsedS());
        System.out.println("Total memory allocated: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime()
                .freeMemory()) / 1e6 + "MB");
        System.out.println();
    }
    
    protected void print(String method, double[] times) {
        System.out.println();
        
        System.out.println(method + " max: " + BigDecimal.valueOf(Meth.max(times)) + " " + measurement);
        System.out.println(method + " min: " + BigDecimal.valueOf(Meth.min(times)) + " " + measurement);
        System.out.println(method + " avg: " + BigDecimal.valueOf(Meth.avg(times)) + " " + measurement);
    }
    
    public void benchmark(Runnable r, String name) {
        double[] times = new double[(int) 1e3];
        System.out.println();
        System.out.println("Benchmarking " + name);
        System.out.println("Iteration: " + times.length);
        Time t = new Time(TimeUnit.MILLISECONDS);
        long ramBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        for (int i = 0; i < times.length; i++) {
            long start = System.nanoTime();
            r.run();
            times[i] = ((System.nanoTime() - start) / 1e6);//nano to millis
        }
        long ramAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Memory Increase: " + (ramAfter - ramBefore) / 1e6 + "MB");
        System.out.println("Total Time: " + t.elapsedS());
        
        printAdd(name, times);
        
    }
    
    private static class BenchmarkResult implements Comparable<BenchmarkResult> {
        public double[] times;
        public String name;
        private Benchmark benchmark;
        
        public BenchmarkResult(String name, double[] times, Benchmark benchmark) {
            this.name = name;
            this.times = times;
            this.benchmark = benchmark;
        }
        
        @Override
        public String toString() {
            return name + " max: " + BigDecimal.valueOf(Meth.max(times)) + " " + benchmark.measurement + System.lineSeparator() + name + " min: " + BigDecimal.valueOf(
                    Meth.min(times)) + " " + benchmark.measurement + System.lineSeparator() + name + " avg: " + BigDecimal.valueOf(
                    Meth.avg(times)) + " " + benchmark.measurement;
        }
        
        @Override
        public int compareTo(BenchmarkResult o) {
            return Double.compare(Meth.avg(times), Meth.avg(o.times));
        }
    }
}
