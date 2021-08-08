package Atom.Utility;

import Atom.Reflect.Reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;

public class TestingUtility {
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
    
    public static void methodFuzzer(Object object, Method[] methods, boolean ignoreIncompatibleParam) throws InvocationTargetException, IllegalAccessException {
        for (Method m : methods) {
            if (Modifier.isPublic(m.getModifiers()) && Modifier.isStatic(m.getModifiers())){
                Object[] objectParam;
                try {
                    objectParam = Reflect.createRandomParam(m);
                    m.invoke(object, objectParam);
                }catch(InvocationTargetException e){
                    if (e.getCause() instanceof IllegalArgumentException){
                        if (ignoreMessage.contains(e.getMessage())) continue;
                        if (ignoreIncompatibleParam){
                            e.printStackTrace();
                            continue;
                        }
                    }
                    throw e;
                }catch(IllegalArgumentException e){
                    if (ignoreMessage.contains(e.getMessage())) continue;
                    if (ignoreIncompatibleParam){
                        e.printStackTrace();
                        continue;
                    }
                    throw e;
                }
                
            }
        }
    }
}
