package Atom.Utility.Meta;

import Atom.Reflect.Reflect;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;

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
    
    /** Assigns a random value to every non-final field of an object. Inaccessible or unsupported fields are skipped. */
    public static void fieldFuzzer(Object object) {
        if (object == null) return;
        fieldFuzzer(object, object.getClass().getDeclaredFields());
    }
    
    public static void fieldFuzzer(Object object, Field[] fields) {
        for (Field f : fields) {
            if (Modifier.isFinal(f.getModifiers())) continue;
            try {
                f.setAccessible(true);
                f.set(object, Reflect.getRandomPrimitive(f.getType()));
            }catch (Throwable e) {
                err.println("Field fuzzer skipped " + f.getDeclaringClass().getName() + "." + f.getName() + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Creates an instance of a class with a randomly parameterized constructor. Constructors whose parameters cannot be
     * fuzzed are skipped when {@code ignoreIncompatibleParam} is true.
     *
     * @return the new instance, or null when no constructor succeeded
     */
    public static Object newInstanceFuzzer(Class<?> type, boolean ignoreIncompatibleParam) {
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
            Object[] params;
            try {
                params = randomParams(constructor.getParameterTypes());
            }catch (IllegalArgumentException e) {
                if (!ignoreIncompatibleParam && !ignoreMessage.contains(e.getMessage())) throw e;
                continue;
            }
            try {
                constructor.setAccessible(true);
                return constructor.newInstance(params);
            }catch (Throwable e) {
                if (!ignoreMessage.contains(String.valueOf(e.getMessage()))) {
                    err.println("Instance fuzzer failed for " + type.getName() + ": " + e.getMessage());
                }
            }
        }
        return null;
    }
    
    public static Object newInstanceFuzzer(Class<?> type) {
        return newInstanceFuzzer(type, true);
    }
    
    /** Runs the field, constructor and method fuzzers over every concrete class found in a package. */
    public static void packageFuzzer(String packageName, boolean ignoreIncompatibleParam) {
        packageFuzzer(packageName, TestingUtility.class.getClassLoader(), ignoreIncompatibleParam);
    }
    
    public static void packageFuzzer(String packageName, ClassLoader classLoader, boolean ignoreIncompatibleParam) {
        for (Class<?> type : Reflect.findClasses(packageName, classLoader)) {
            if (type.isPrimitive() || type.isInterface() || type.isEnum() || Modifier.isAbstract(type.getModifiers())) continue;
            Object instance = newInstanceFuzzer(type, ignoreIncompatibleParam);
            if (instance != null) fieldFuzzer(instance);
            try {
                methodFuzzer(instance, type.getDeclaredMethods(), ignoreIncompatibleParam);
            }catch (Throwable e) {
                err.println("Package fuzzer failed on " + type.getName() + ": " + e.getMessage());
            }
        }
    }
    
    private static Object[] randomParams(Class<?>[] types) {
        Object[] params = new Object[types.length];
        for (int i = 0; i < types.length; i++) params[i] = Reflect.getRandomPrimitive(types[i]);
        return params;
    }
}
