//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package Atom.Struct;


import Atom.Class.Disposable;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

public abstract class PoolObject<T> implements ObjectPoolPattern<T> {
    protected final int max;
    public static final Map<Class<?>, PoolObject<?>> pools = Collections.synchronizedMap(new HashMap<>());
    protected int peak;
    protected final List<T> freeObjects;
    
    public static <E> PoolObject<E> getPool(Class<E> eClass) {
        if (pools.containsKey(eClass)) return (PoolObject<E>) pools.get(eClass);
        return null;
    }
    
    public PoolObject() {
        this(16, 2147483647);
    }
    
    public PoolObject(int initialCapacity) {
        this(initialCapacity, 2147483647);
    }
    
    public PoolObject(int initialCapacity, int max) {
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
        return this.freeObjects.size() == 0 ? this.newObject() : this.freeObjects.remove(0);
    }
    
    /**
     * Return to list or ignore if the list is full
     */
    public void free(T object) {
        if (object == null){
            throw new IllegalArgumentException("object cannot be null.");
        }else{
            if (this.freeObjects.size() < this.max){
                this.freeObjects.add(object);
                this.peak = Math.max(this.peak, this.freeObjects.size());
            }
            
            this.reset(object);
        }
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
}
