package Atom.Annotation;

import java.lang.annotation.Annotation;

public class TestParamImpl implements ParamClamp {
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
    public boolean skip() {
        return false;
    }
    
    @Override
    public Class<? extends Annotation> annotationType() {
        return this.getClass();
    }
}
