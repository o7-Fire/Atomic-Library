package Atom.Annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ParamClamp {
    byte maxByte() default Byte.MAX_VALUE;
    
    byte minByte() default Byte.MIN_VALUE;
    
    long maxLong() default Long.MAX_VALUE;
    
    long minLong() default Long.MIN_VALUE;
    
    int maxInteger() default Integer.MAX_VALUE;
    
    int minInteger() default Integer.MIN_VALUE;
    
    int minString() default 5;
    
    int maxString() default 20;
    
    boolean skip() default false;
    
    
}

