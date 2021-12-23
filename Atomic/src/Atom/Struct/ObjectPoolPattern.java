package Atom.Struct;

import Atom.Class.Factory;

public interface ObjectPoolPattern<T> extends Factory<T> {
    
    /**
     * store object in pool and reset it if can
     *
     * @param object
     */
    void free(T object);
    
    /**
     * store all object in pool and reset them if can
     *
     * @param list
     */
    void free(Iterable<T> list);
    
    /**
     * clear all object in pool and dispose them if can
     */
    void clear();
}
