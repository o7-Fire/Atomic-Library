package Atom.Struct;

import java.io.Serializable;

public class Union<A, B> implements Serializable {
    protected A itemA;
    protected B itemB;
    
    public Union() {
    
    }
    
    public Union(A itemA, B itemB) {
        this.itemA = itemA;
        this.itemB = itemB;
    }
    
    public A getItemA() {
        return itemA;
    }
    
    public Union<A, B> setItemA(A itemA) {
        this.itemA = itemA;
        return this;
    }
    
    public B getItemB() {
        return itemB;
    }
    
    public Union<A, B> setItemB(B itemB) {
        this.itemB = itemB;
        return this;
    }
}
