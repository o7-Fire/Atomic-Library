package Atom.Struct;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FunctionalPoolObject<T> extends PoolObject<T> {
    //tf is this
    public static final FunctionalPoolObject<StringBuilder> StringBuilder = new FunctionalPoolObject<>(s -> s.setLength(0), StringBuilder::new);//should you ?
    
    static {
        PoolObject.pools.put(java.lang.StringBuilder.class, StringBuilder);
    }
    
    protected final Consumer<T> reset;
    protected final Supplier<T> supplier;
    
    public FunctionalPoolObject(Supplier<T> supplier) {
        this(null, supplier);
    }
    
    public FunctionalPoolObject(Consumer<T> reset, Supplier<T> supplier) {
        this.reset = reset;
        this.supplier = supplier;
    }
    
    public static void main(String[] args) {
        StringBuilder sb = StringBuilder.obtain().append("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        StringBuilder.free(sb);
        assert sb.length() == 0;
    }
    
    @Override
    protected void reset(T object) {
        if (reset != null) reset.accept(object);
        else super.reset(object);
    }
    
    @Override
    protected T newObject() {
        return supplier.get();
    }
}
