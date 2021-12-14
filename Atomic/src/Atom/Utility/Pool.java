package Atom.Utility;

import Atom.Reflect.Reflect;
import Atom.Time.Time;

import java.util.concurrent.*;
import java.util.function.Supplier;

//wtf ?
public class Pool {

    public static Supplier<ExecutorService> serviceSupplier = () -> Executors.newCachedThreadPool(r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setName(t.getName() + "-Atomic-Pool");
        t.setDaemon(true);
        return t;
    });

    public static ExecutorService service = serviceSupplier.get();

    public static Supplier<ExecutorService> parallelSupplier = () -> Executors.newFixedThreadPool(Math.max(
            Runtime.getRuntime().availableProcessors() - 1, 1), r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setName(t.getName() + "-Atomic-Executor");
        t.setDaemon(true);
        return t;
    });

    public static ExecutorService parallelAsync = parallelSupplier.get();

    public static ThreadFactory daemonFactory = r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName(Reflect.getCallerClassStackTrace(1).toString());
        return t;
    };


    public static Future<?> async(RunnableFuture<?> future) {
        return parallelAsync.submit(future);
    }
    
    public static <V> Future<V> async(Callable<V> future) {
        return parallelAsync.submit(future);
    }
    
    public static Future<?> async(Runnable r) {
        return parallelAsync.submit(r);
    }
    
    public static Future<?> submit(RunnableFuture<?> future) {
        return service.submit(future);
    }

    public static <V> Future<V> submit(Callable<V> future) {
        return service.submit(future);
    }

    public static Future<?> submit(Runnable r) {
        return service.submit(r);
    }

    public static void awaitParallel() {
        await(parallelAsync);
        service.shutdownNow();
        parallelAsync = parallelSupplier.get();
    }

    public static void awaitService() {
        await(service);
        service.shutdownNow();
        service = serviceSupplier.get();
    }

    public static void await(ExecutorService service) {
        await(service, new Time(TimeUnit.SECONDS, 120));
    }

    public static void await(ExecutorService service, Time time) {
        try {
            service.awaitTermination(time.src, time.tu);
        } catch (InterruptedException e) {

        }
    }

    public static Thread thread(Runnable r) {
        Thread t = new Thread(r);
        t.setName(Reflect.getCallerClassStackTrace().toString());
        return t;
    }

    public static int recommendedThreadCount() {
        int mRtnValue = 0;
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long mTotalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        int mAvailableProcessors = runtime.availableProcessors();
        
        long mTotalFreeMemory = freeMemory + (maxMemory - mTotalMemory);
        mRtnValue = (int) (mTotalFreeMemory / 4200000000L);
        
        int mNoOfThreads = mAvailableProcessors - 1;
        if (mNoOfThreads < mRtnValue) mRtnValue = mNoOfThreads;
        
        return mRtnValue;
    }
    
    public static Thread daemon(Runnable r) {
        return daemonFactory.newThread(r);
    }
}
