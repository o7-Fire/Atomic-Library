package Atom.Annotation;

import java.lang.annotation.Annotation;
//what is this?
public class TestParamImpl implements MethodFuzzer {
    @Override
    public byte maxByte() {
        return Byte.MAX_VALUE;
    }
    
    @Override
    public byte minByte() {
        return Byte.MIN_VALUE;
    }
    
    @Override
    public long maxLong() {
        return Long.MAX_VALUE;
    }
    
    @Override
    public long minLong() {
        return Long.MIN_VALUE;
    }
    
    @Override
    public int maxInteger() {
        return Integer.MAX_VALUE;
    }
    
    @Override
    public int minInteger() {
        return Integer.MIN_VALUE;
    }
    
    @Override
    public int minString() {
        return 5;
    }
    
    @Override
    public int maxString() {
        return 20;
    }
    
    @Override
    public float maxFloat() {
        return 1f;
    }
    
    @Override
    public float minFloat() {
        return 0f;
    }
    
    @Override
    public double maxDouble() {
        return 1d;
    }
    
    @Override
    public double minDouble() {
        return 0d;
    }
    
    @Override
    public boolean skip() {
        return false;
    }
    
    @Override
    public Class<? extends Annotation> annotationType() {
        return this.getClass();
    }
}
