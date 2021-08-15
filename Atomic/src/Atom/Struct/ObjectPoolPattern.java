package Atom.Struct;

public interface ObjectPoolPattern<T> {
    T obtain();
    
    void free(T object);
    
    void free(Iterable<T> list);
    
    void clear();
}
