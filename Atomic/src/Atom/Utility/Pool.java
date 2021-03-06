package Atom.Utility;

import Atom.Reflect.Reflect;

import java.util.concurrent.*;

//wtf ?
public class Pool {
    public static ExecutorService service = Executors.newCachedThreadPool(r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setName(t.getName() + "-Atomic-Pool");
        t.setDaemon(true);
        return t;
    });
    public static ExecutorService parallelAsync = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1, r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setName(t.getName() + "-Atomic-Executor");
        t.setDaemon(true);
        return t;
    });
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
