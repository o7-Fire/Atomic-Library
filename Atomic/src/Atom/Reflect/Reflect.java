package Atom.Reflect;

import Atom.Annotation.MethodFuzzer;
import Atom.Annotation.TestParamImpl;
import Atom.Manifest;
import Atom.Math.Array;
import Atom.Math.Matrix;
import Atom.String.WordGenerator;
import Atom.Struct.Filter;
import Atom.Struct.FunctionalPoolObject;
import Atom.Utility.Random;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Reflect {
    
    public static DebugType DEBUG_TYPE;
    public static boolean debug;
    public static final HashSet<String> legalMainSignature = new HashSet<>();
    static {
        DEBUG_TYPE = getDebugType();
        String s = "public static void main(String a[])\n" + "static public void main(String a[])\n" + "public static void main(String[] a)\n" + "static public void main(String[] a)\n" + "public static void main(String... a)\n" + "static public void main(String... a)";
        legalMainSignature.addAll(Arrays.asList(s.split("\\n")));
    }
    
    public static String getPlatform() {
        String jvmName = System.getProperty("java.vm.name", "").toLowerCase();
        String osName = System.getProperty("os.name", "").toLowerCase();
        String osArch = System.getProperty("os.arch", "").toLowerCase();
        String abiType = System.getProperty("sun.arch.abi", "").toLowerCase();
        String libPath = System.getProperty("sun.boot.library.path", "").toLowerCase();
        if (jvmName.startsWith("dalvik") && osName.startsWith("linux")){
            osName = "android";
        }else if (jvmName.startsWith("robovm") && osName.startsWith("darwin")){
            osName = "ios";
            osArch = "arm";
        }else if (osName.startsWith("mac os x") || osName.startsWith("darwin")){
            osName = "macosx";
        }else{
            int spaceIndex = osName.indexOf(' ');
            if (spaceIndex > 0){
                osName = osName.substring(0, spaceIndex);
            }
        }
        if (osArch.equals("i386") || osArch.equals("i486") || osArch.equals("i586") || osArch.equals("i686")){
            osArch = "x86";
        }else if (osArch.equals("amd64") || osArch.equals("x86-64") || osArch.equals("x64")){
            osArch = "x86_64";
        }else if (osArch.startsWith("aarch64") || osArch.startsWith("armv8") || osArch.startsWith("arm64")){
            osArch = "arm64";
        }else if ((osArch.startsWith("arm")) && ((abiType.equals("gnueabihf")) || (libPath.contains("openjdk-armhf")))){
            osArch = "armhf";
        }else if (osArch.startsWith("arm")){
            osArch = "arm";
        }
        return osName + "-" + osArch;
    }
    
    public static DebugType getDebugType() {
        debug = true;
        if (System.getProperty("intellij.debug.agent") != null) return DebugType.IntellijAgent;
        try {
            if (java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0){
                return DebugType.AgentLib;
            }
        }catch(Throwable ignored){}
        debug = false;
        return DebugType.None;
    }
    
    public static void main(String[] args) {
        int[] ints = Array.randomInteger();
        int[][] matrix = new int[2][2];
        System.out.println(ints.getClass().getComponentType());
        System.out.println(Array.boxArray(ints).getClass().getComponentType());
        System.out.println(ints.getClass().getComponentType());
        int[][] matrixs = (int[][]) getRandomPrimitive(matrix.getClass());
        System.out.println(matrixs.getClass());
        Object tensor = java.lang.reflect.Array.newInstance(int.class, 1, 2, 3);
        System.out.println(tensor);
        Array.random(tensor);
        System.out.println(Arrays.deepToString((Object[]) tensor));
    
    }
    
    public static <T> Object getRandomPrimitiveArray(Class<T> type) {
        if (type.isArray()){
            Class<?> finalType = type.getComponentType();
            int rank = 1;
            while (finalType.getComponentType() != null) {
                rank++;
                finalType = finalType.getComponentType();
            }
            if (rank == 1){
                return getRandomPrimitiveArray(finalType);
            }
            Object[] array = null;
            if (rank == 2){
                array = Matrix.makeMatrix(finalType, Random.getInt(100), Random.getInt(100));
            }
            if (rank == 3){
                array = Matrix.makeTensor(finalType, Random.getInt(100), Random.getInt(100), Random.getInt(100));
            }
            if (array == null)
                array = (Object[]) java.lang.reflect.Array.newInstance(finalType, Array.getRandomDimension(rank));
            Array.random(array);
            return array;
    
        }
    
        if (type.equals(String.class)) return (T) WordGenerator.randomWordArray();
        if (type == char.class || type == Character.class) if (type.isPrimitive()) return Array.randomChar();
        else return Array.boxArray(Array.randomChar());
        if (type == boolean.class || type == Boolean.class) if (type.isPrimitive()) return Array.randomBoolean();
        else return Array.boxArray(Array.randomBoolean());
        if (type == int.class || type == Integer.class) if (type.isPrimitive()) return Array.randomInteger();
        else return Array.boxArray(Array.randomInteger());
        if (type == long.class || type == Long.class) if (type.isPrimitive()) return Array.randomLong();
        else return Array.boxArray(Array.randomLong());
        if (type == double.class || type == Double.class) if (type.isPrimitive()) return Array.randomDouble();
        else return Array.boxArray(Array.randomDouble());
        if (type == float.class || type == Float.class) if (type.isPrimitive()) return Array.randomFloat();
        else return Array.boxArray(Array.randomFloat());
        if (type == short.class || type == Short.class) if (type.isPrimitive()) return Array.randomShort();
        else return Array.boxArray(Array.randomShort());
        if (type == byte.class || type == Byte.class) if (type.isPrimitive()) return Array.randomByte();
        else return Array.boxArray(Array.randomByte());
    
        throw new IllegalArgumentException("Not a primitive array: " + type);
    }
    
    static MethodFuzzer testParam = new TestParamImpl();
    
    public static Object getRandomPrimitive(Class<?> type) {
        return getRandomPrimitive(type, null);
    }
    
    public static Object getRandomPrimitive(Class<?> type, MethodFuzzer param) {
        boolean paramed = param != null;
        if (type.isArray()){
            return getRandomPrimitiveArray(type);
        }
        if (type.equals(String.class))
            return paramed ? WordGenerator.newWord(Random.getInt(param.minString(), param.maxString())).toString() : WordGenerator.randomWord().toString();
        if (type == char.class || type == Character.class) return Random.getChar();
        if (type == boolean.class || type == Boolean.class) return Random.getBool();
        if (type == int.class || type == Integer.class)
            return paramed ? Random.getInt(param.minInteger(), param.maxInteger()) : Random.getInt();
        if (type == long.class || type == Long.class)
            return paramed ? Random.getLong(param.minLong(), param.maxLong()) : Random.getLong();
        if (type == double.class || type == Double.class) return Random.getDouble();
        if (type == float.class || type == Float.class) return Random.getFloat();
        if (type == short.class || type == Short.class) return Random.getShort();
        if (type == byte.class || type == Byte.class)
            return paramed ? Random.getByte(param.minByte(), param.maxByte()) : Random.getByte();
        Object o = Random.getRandom(type);
        if (o != null) return o;
        throw new IllegalArgumentException("Not a primitive: " + type);
    }
    
    public static Object[] createRandomParam(Method m) {
        Object[] objectParam = new Object[m.getParameterTypes().length];
        if (objectParam.length == 0) return objectParam;
        Class<?>[] parameterTypes = m.getParameterTypes();
        MethodFuzzer param = m.getAnnotation(MethodFuzzer.class);
        if (param != null){
            if (param.skip()) throw new IllegalArgumentException("Skip method: " + m.getName() + ", annotation skip");
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> e = parameterTypes[i];
            objectParam[i] = getRandomPrimitive(e, param);
        }
        return objectParam;
    }
    
    public enum DebugType {AgentLib, IntellijAgent, DevEnvironment, UserPreference, None}
    
    
    public static int callerOffset() {
        int def = 1;
        if (OS.isAndroid) def++;
        return def;
    }
    
    public static Object[] parseParameters(List<String> s, Parameter[] parameters) {
        return parseParameters(s.toArray(new String[0]), parameters);
    }
    
    
    public static Object[] parseParameters(String[] s, Parameter[] params) {
        ArrayList<Object> param = new ArrayList<>();
        int i = 0;
        for (Parameter p : params) {
            Class<?> c = p.getType();
            if (s.length - i == 0)
                throw new IllegalArgumentException("No argument left/Not enough argument for param: " + p);
            String current = s[0];
            if (c.isPrimitive() || c.equals(String.class)) {
                Object o = Reflect.parseStringToPrimitive(current, c);
                if (o == null)
                    throw new IllegalArgumentException("Failed to parse: " + current + " for param: " + p);
                param.add(o);
            }else {
                throw new IllegalArgumentException(p + " is not primitive parameter");
            }
            i++;
        }
        return param.toArray(new Object[0]);
    }
    
    public static String getCallerClass() {
        return Thread.currentThread().getStackTrace()[callerOffset() + 2].getClassName();
    }
    
    public static StackTraceElement getCallerClassStackTrace(int offset) {
        return Thread.currentThread().getStackTrace()[callerOffset() + 2 + offset];
    }
    
    public static StackTraceElement getCallerClassStackTrace() {
        return Thread.currentThread().getStackTrace()[callerOffset() + 2];
    }
    
    public static Object[] parseStringToPrimitiveArray(String[] datas, Class<?>[] types) {
        if (datas.length != types.length)
            throw new IllegalArgumentException("Data and Type length not match: " + datas.length + ", " + types.length);
        Object[] objects = new Object[types.length];
        for (int i = 0; i < objects.length; i++) {
            objects[i] = parseStringToPrimitive(datas[i], types[i]);
        }
        return objects;
    }
    
    public static Object parseStringToPrimitive(String data, Class<?> type) {
        if (type.equals(String.class)) return data;
        if (data.length() == 0) return null;
        
        if (type == boolean.class || type == Boolean.class){
            if (data.equalsIgnoreCase("true")) return true;
            else if (data.equalsIgnoreCase("false")) return false;
            else return null;
        }
        if (type == char.class || type == Character.class) return data.toCharArray()[0];
        if (type == int.class || type == Integer.class) return Integer.parseInt(data);
        if (type == long.class || type == Long.class) return Long.parseLong(data);
        if (type == double.class || type == Double.class) return Double.parseDouble(data);
        if (type == float.class || type == Float.class) return Float.parseFloat(data);
        if (type == short.class || type == Short.class) return Short.parseShort(data);
        if (type == byte.class || type == Byte.class) return Byte.parseByte(data);
        
        return null;
    }
    
    
    public static void restart(File jar, List<String> classpath) throws FileNotFoundException {
        if (!jar.exists()) throw new FileNotFoundException(jar.getAbsolutePath());
        StringBuilder cli = getRestartArg();
        cli.append("-cp ");
        for (String s : classpath)
            cli.append(s).append(System.getProperty("path.separator"));
        if (System.getProperty("path.separator").equals(String.valueOf(cli.charAt(cli.length() - 1)))) {
            cli = new StringBuilder(cli.substring(0, cli.length() - 1));
        }
        cli.append(" jar ");
        cli.append(jar.getAbsolutePath());
        String s = cli.toString();
        runExit(s);
    }
    
    public static StringBuilder getRestartArg() {
        StringBuilder cli = FunctionalPoolObject.StringBuilder.obtain();
        ;
        cli.append(System.getProperty("java.home")).append(File.separator).append("bin").append(File.separator).append("java").append(OS.isWindows ? ".exe " : " ");
        try {
            for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
                cli.append(jvmArg).append(" ");
            }
        }catch(Throwable ignored){
        }
        return cli;
    }
    
    public static void runExit(String cmd) {
        runExit(cmd, null, null);
    }
    
    public static void runExit(String cmd, String[] envp, File dir) {
        new Thread(() -> {
            try {
                Runtime.getRuntime().exec(cmd, envp, dir);
            }catch(IOException e){
                e.printStackTrace();
            }
        }).start();
        System.exit(0);
    }
    
    public static void restart(String mainClass, String... args) {
        try {
            restart(mainClass, Arrays.asList(ManagementFactory.getRuntimeMXBean().getClassPath().split(File.pathSeparator)), args);
        }catch(Exception e){
        
        }
    }
    
    public static void restart(String mainClass, Iterable<String> classpath, String... args) {
        StringBuilder cli = getRestartArg();
        cli.append("-cp ");
        for (String s : classpath)
            cli.append(s).append(System.getProperty("path.separator"));
        if (System.getProperty("path.separator").equals(String.valueOf(cli.charAt(cli.length() - 1)))){
            cli = new StringBuilder(cli.substring(0, cli.length() - 1));
        }
        cli.append(" ");
        cli.append(mainClass);
        for (String s : args)
            cli.append(" ").append(s);
        String s = cli.toString();
        runExit(s);
    }
    
    public static String getMainClassName() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String s = null;
        if (trace.length > 0) {
            s = trace[trace.length - 1].getClassName();
            if (s.equals(Thread.class.getName())) s = null;
        }
        return s;
    }
    
    public static String getClasspath(Class<?> c) {
        return c.getName().replace('.', '/') + ".class";
    }
    
    public static File getCurrentJar(Class<?> clazz) {
        try {
            return new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath());
        }catch (Throwable t) {
            try {
                return new File(Manifest.internalRepo.getResource(getClasspath(clazz)).getFile());//this is brilliant
            }catch (Throwable tt) {
                return null;//bruh
            }
        }
    }
    
    public static File getCurrentJar(Object clazz) {
        //more fool proof
        return getCurrentJar(clazz.getClass());
    }
    
    public static File getCurrentJar() {
        //even more fool proof
        return getCurrentJar(Reflect.class);
    }
    
    public static <E> E getField(Class<?> clazz, String name, Object object) {
        ArrayList<Field> result = findDeclaredField(clazz, f -> f.getName().equals(name));
        E e = null;
        for (Field f : result) {
            try {
                e = (E) f.get(object);
            }catch (Throwable ignored) {
            }
        }
        return e;
    }
    
    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameter) throws NoSuchMethodException {
        return clazz.getMethod(name, parameter);
    }
    
    public static Method getMethod(Class<?> clazz, String name, Object object) {
        ArrayList<Method> f = findMethod(clazz, s -> s.getName().equals(name), object);
        if (f == null || f.isEmpty()) return null;
        else return f.get(0);
    }
    
    public static ArrayList<Method> findMethod(Class<?> clazz, Filter<Method> filter, Object object) {
        ArrayList<Method> result = new ArrayList<>();
        try {
            Method[] methods = object.getClass().getDeclaredMethods();
            for (Method m : methods) {
                if (filter.accept(m)) result.add(m);
            }
            for (Method m : methods) {
                try {
                    m.setAccessible(true);
                }catch (Throwable ignored) {
                }
            }
            return result;
        }catch (Throwable ignored) {
        }
        try {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method m : methods) {
                if (filter.accept(m)) result.add(m);
            }
            for (Method m : methods) {
                try {
                    m.setAccessible(true);
                }catch (Throwable ignored) {
                }
            }
            
        }catch (Throwable ignored) {
        }
        if (result.isEmpty()) return null;
        else return result;
    }
    
    public static ArrayList<Field> findDeclaredField(Class<?> clazz, Filter<Field> filter) {
        return findDeclaredField(clazz, filter, null);
    }
    
    public static ArrayList<Field> findDeclaredField(Class<?> clazz, Filter<Field> filter, Object object) {
        ArrayList<Field> result = new ArrayList<>();
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field f : fields) {
                if (filter.accept(f)) result.add(f);
            }
            for (Field f : result) {
                try {
                    f.setAccessible(true);
                }catch (Throwable ignored) {
                }
            }
            return result;
        }catch (Throwable ignored) {
        }
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                if (filter.accept(f)) result.add(f);
            }
            for (Field f : result) {
                try {
                    f.setAccessible(true);
                }catch (Throwable ignored) {
                }
            }
        }catch (Throwable ignored) {
        }
        if (result.isEmpty()) return null;
        else return result;
    }
    
    public static Object invoke(Method m, Object o, Object... arg) throws InvocationTargetException, IllegalAccessException {
        m.setAccessible(true);
        return m.invoke(o, arg);
    }
    
    @Deprecated
    //help its broke
    public static <T> ArrayList<Class<T>> getAllExtendedOrImplementedTypesRecursively(Class<?> clazz) {
        List<Class<?>> res = new ArrayList<>();
        
        do {
            res.add(clazz);
            
            // First, add all the interfaces implemented by this class
            Class<?>[] interfaces = clazz.getInterfaces();
            if (interfaces.length > 0) {
                res.addAll(Arrays.asList(interfaces));
                
                for (Class<?> interfaced : interfaces) {
                    res.addAll(getAllExtendedOrImplementedTypesRecursively(interfaced));
                }
            }
            
            // Add the super class
            Class<?> superClass = clazz.getSuperclass();
            
            // Interfaces does not have java,lang.Object as superclass, they have null, so break the cycle and return
            if (superClass == null) {
                break;
            }
            
            // Now inspect the superclass
            clazz = superClass;
        }while (!"java.lang.Object".equals(clazz.getCanonicalName()));
        ArrayList<Class<T>> classes = new ArrayList<>();
        for (Class<?> c : res) {
            try {
                classes.add((Class<T>) c);
            }catch (Throwable ignored) {
            
            }
        }
        return classes;
    }
    
    
    //static
    public static <E> E getRandomField(Class<E> type) {
        return getRandomField(type, null);
    }
    
    //dynamic
    public static <E> E getRandomField(Class<E> type, Object o) {
        ArrayList<E> arrayList = new ArrayList<>();
        for (Field f : type.getDeclaredFields()) {
            try {
                if (type.isInstance(f.get(o))) arrayList.add((E) f.get(o));
            }catch (Throwable ignored) {
            }
        }
        return Random.getRandom(arrayList);
    }
    
    public static ArrayList<Package> findPackages(Filter<Package> filter) {
        ArrayList<Package> a = new ArrayList<>();
        for (Package p : Package.getPackages())
            if (filter.accept(p)) a.add(p);
        return a;
    }
    
    public static ArrayList<Package> findPackages(Filter<Package> filter, ClassLoader classLoader) {
        ArrayList<Package> a = new ArrayList<>();
        for (Package p : (Package[]) getField(ClassLoader.class, "getPackages", classLoader))
            if (filter.accept(p)) a.add(p);
        return a;
    }
    
}
