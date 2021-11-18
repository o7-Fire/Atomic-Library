package Atom.Utility;

import Atom.Reflect.Reflect;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;

//TODO add field fuzzer, package fuzzer, new Instance fuzzer
public class TestingUtility {
    
    public static PrintStream err = System.err;
    
    public static void methodFuzzer(Method[] methods, boolean ignoreIncompatibleParam) throws InvocationTargetException, IllegalAccessException {
        methodFuzzer(null, methods, ignoreIncompatibleParam);
    }
    
    public static final HashSet<String> ignoreMessage = new HashSet<>();
    
    static {
        ignoreMessage.add("Not a primitive: class java.lang.Class");
        ignoreMessage.add("Not a primitive: class java.lang.Object");
        ignoreMessage.add("Not a primitive array: class java.lang.Class");
        ignoreMessage.add("Not a primitive array: class java.lang.Object");
    }
    
    public static boolean handle(Throwable e, Method m, boolean ignoreIncompatibleParam, Object[] objectParam) {
        while (e != null) {
            if (e instanceof Error){
                err.println();
                err.println(e.getClass().getCanonicalName());
                err.println("Error: " + e.getMessage());
                err.println("Method: " + m.getDeclaringClass().getCanonicalName() + "." + m.getName());
                err.println("Param: " + Arrays.toString(objectParam));
                e.printStackTrace(err);
                err.println();
                throw (Error) e;
            }
            if (e instanceof IllegalArgumentException || e instanceof NegativeArraySizeException || e instanceof ClassCastException || e instanceof IndexOutOfBoundsException){
                if (ignoreMessage.contains(e.getMessage())) return true;
                if (ignoreIncompatibleParam){
                    err.println();
                    err.println(e.getMessage());
                    err.println(m.getDeclaringClass().getCanonicalName() + "." + m.getName());
                    return true;
                }
            }
            e = e.getCause();
        }
        return false;
    
    }
    
    public static void methodFuzzer(Object object, Method[] methods, boolean ignoreIncompatibleParam) throws InvocationTargetException, IllegalAccessException {
        for (Method m : methods) {
            
            if (Modifier.isPublic(m.getModifiers()) && Modifier.isStatic(m.getModifiers())){
    
                Object[] objectParam = new Object[0];
                try {
                    //1 is a fluke
                    //2 is a coincidence
                    //3 is a pattern
                    for (int i = 0; i < 5; i++) {
                        objectParam = Reflect.createRandomParam(m);
                        m.invoke(object, objectParam);
                    }
                }catch(Exception e){
    
                    if (handle(e, m, ignoreIncompatibleParam, objectParam)) continue;
                    throw e;
    
                }
                
            }
        }
    }
}
