package Atom.Class;

import java.util.function.Supplier;

public interface Factory<T> extends Supplier<T> {
    T obtain();
    
    @Override
    default T get() {
        return obtain();
    }
}
