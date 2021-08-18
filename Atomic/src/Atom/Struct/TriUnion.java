package Atom.Struct;

public class TriUnion<A, B, C> extends Union<A, B> {
    protected C itemC;
    
    public TriUnion() {
    }
    
    public TriUnion(A itemA, B itemB, C itemC) {
        super(itemA, itemB);
        this.itemC = itemC;
    }
    
    public C getItemC() {
        return itemC;
    }
    
    public TriUnion<A, B, C> setItemC(C itemC) {
        this.itemC = itemC;
        return this;
    }
}
