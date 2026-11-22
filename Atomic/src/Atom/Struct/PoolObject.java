//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Atom.Struct;


import Atom.Class.Disposable;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

/**
 * A pool of objects that can be reused to avoid allocation.
 *
 * <p>Behavior matches Arc v8's {@code arc.util.pooling.Pool}: objects are popped from the end of the free list (O(1)), the
 * maximum only limits how many objects are stored (objects are still reset when the pool is full), and {@link #peak} is public.
 */
@SuppressWarnings("unchecked")
public abstract class PoolObject<T> implements ObjectPoolPattern<T> {
    /** The maximum number of objects that will be pooled. */
    protected final int max;
    public static final Map<Class<?>, PoolObject<?>> pools = Collections.synchronizedMap(new HashMap<>());
    /** The highest number of free objects. Can be reset any time. */
    public int peak;
    protected final List<T> freeObjects;
    
    public static <E> PoolObject<E> getPool(Class<E> eClass) {
        if (pools.containsKey(eClass)) return (PoolObject<E>) pools.get(eClass);
        return null;
    }
    
    public PoolObject() {
        this(16, Integer.MAX_VALUE);
    }
    
    public PoolObject(int initialCapacity) {
        this(initialCapacity, Integer.MAX_VALUE);
    }
    
    public PoolObject(int initialCapacity, int max) {
        //no multithreading
        this.freeObjects = new ArrayList<>(initialCapacity);
        this.max = max;
    }
    
    public static <E> E get(Class<E> eClass) {
        PoolObject<E> poolObject = getPool(eClass);
        if (poolObject == null) return null;
        return poolObject.obtain();
    }
    
    public static void release(java.lang.Object o) {
        PoolObject poolObject = getPool(o.getClass());
        if (poolObject != null) poolObject.free(o);
    }
    
    @Override
    public void free(Iterable<T> Iterable) {
        if (Iterable == null){
            throw new IllegalArgumentException("Iterable cannot be null.");
        }else{
            List<T> freeObjects = this.freeObjects;
            int max = this.max;
            for (T object : Iterable) {
                if (object != null){
                    if (freeObjects.size() < max){
                        freeObjects.add(object);
                    }
                    
                    this.reset(object);
                }
            }
            
            this.peak = Math.max(this.peak, freeObjects.size());
        }
    }
    
    public int getPeak() {
        return peak;
    }
    
    public int getMax() {
        return max;
    }
    
    protected abstract T newObject();
    
    public T obtain() {
        T that = this.freeObjects.isEmpty() ? this.newObject() : this.freeObjects.remove(this.freeObjects.size() - 1);
        if (that == null) {
            that = this.newObject();
        }
        return that;
    }
    
    /**
     * Return to list or ignore if the list is full. The object is always reset, even when it is not stored.
     */
    public void free(T object) {
        if (object == null) {
            throw new IllegalArgumentException("object cannot be null.");
        }
        if (this.freeObjects.size() < this.max) {
            this.freeObjects.add(object);
            this.peak = Math.max(this.peak, this.freeObjects.size());
        }
        this.reset(object);
    }
    
    /** Arc v8 compatible alias for {@link #free(Iterable)}. */
    public void freeAll(Iterable<T> objects) {
        free(objects);
    }
    
    protected void reset(T object) {
        if (object instanceof Object) {
            ((Object) object).reset();
        }
    }
    
    
    public void clear() {
        if (freeObjects.size() == 0) return;
        boolean isDisposable = freeObjects.get(0) instanceof Disposable || freeObjects.get(0) instanceof Closeable;
        if (isDisposable){
            for (T obj : this.freeObjects) {
                if (obj instanceof Closeable){
                    try {
                        ((Closeable) obj).close();
                    }catch(IOException ignored){
                    
                    }
                }
                if (obj instanceof Disposable){
                    ((Disposable) obj).dispose();
                }
            }
        }
        this.freeObjects.clear();
    }
    
    public int getFree() {
        return this.freeObjects.size();
    }
    
    public interface Object {
        void reset();
    }
    
    /** Arc v8 compatible name for a resettable pooled object. */
    public interface Poolable extends Object {
        @Override
        void reset();
    }
}
