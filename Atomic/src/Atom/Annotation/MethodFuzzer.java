package Atom.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * prevent OutOfMemoryError due to fuzzing ridiculous param values
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodFuzzer {
    byte maxByte() default Byte.MAX_VALUE;

    byte minByte() default Byte.MIN_VALUE;

    long maxLong() default Long.MAX_VALUE;

    long minLong() default Long.MIN_VALUE;

    int maxInteger() default Integer.MAX_VALUE;

    int minInteger() default Integer.MIN_VALUE;

    int minString() default 5;

    int maxString() default 20;

    float maxFloat() default 1f;

    float minFloat() default 0f;

    double maxDouble() default 1d;

    double minDouble() default 0d;

    boolean skip() default false;


}

