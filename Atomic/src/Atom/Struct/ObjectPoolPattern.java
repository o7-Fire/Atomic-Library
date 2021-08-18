package Atom.Struct;

import Atom.Class.Factory;

public interface ObjectPoolPattern<T> extends Factory<T> {
    
    void free(T object);
    
    void free(Iterable<T> list);
    
    void clear();
}
