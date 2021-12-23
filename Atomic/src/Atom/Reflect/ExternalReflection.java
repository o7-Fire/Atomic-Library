package Atom.Reflect;

import Atom.Encoding.EncoderJson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.serializers.JsonSerializer;
import org.reflections.util.ConfigurationBuilder;

import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
//require org.reflection, not included
public class ExternalReflection {
    public static WeakHashMap<Integer, WeakHashMap<Class, ArrayList<Class>>> cachedExtendedJson = new WeakHashMap<>();
    
    public static <E> List<Class<? extends E>> getExtendedClassFromJson(String json, Class<E> e) {
        return getExtendedClassFromJson(json, e, Reflect.class.getClassLoader(), true, new ArrayList<>());
    }
    
    public static <E> List<Class<? extends E>> getExtendedClassFromJson(String json, Class<E> e, ClassLoader cl, boolean addSubtype, ArrayList<Class<? extends E>> ar) {
        if (cachedExtendedJson.containsKey(json.hashCode()) && cachedExtendedJson.get(json.hashCode()).containsKey(e)){
            ArrayList<Class> cache = cachedExtendedJson.get(json.hashCode()).get(e);
            for (Class c : cache)
                try {
                    ar.add(c);
                }catch(Throwable ignored){}
        }else{
            JsonObject jo = EncoderJson.parseJson(json).getAsJsonObject().get("store").getAsJsonObject().get("storeMap").getAsJsonObject().get("SubTypesScanner").getAsJsonObject();
            ar.addAll(getExtendedClassFromJson(jo, e, cl, addSubtype, ar));
            WeakHashMap<Class, ArrayList<Class>> weakHashMap = new WeakHashMap<>();
            weakHashMap.put(e, new ArrayList<>(ar));
            cachedExtendedJson.put(json.hashCode(), weakHashMap);
        }
        return ar;
    }
    
    
    public static <E> List<Class<? extends E>> getExtendedClassFromJson(JsonObject jo, Class<E> e, ClassLoader cl, boolean addSubtype, ArrayList<Class<? extends E>> ar) {
        JsonElement target = jo.get(e.getName());
        if (target != null){
            for (JsonElement j : target.getAsJsonArray()) {
                try {
                    String c = j.getAsString();
                    Class raw = cl.loadClass(c);
                    Class<? extends E> h = (Class<? extends E>) raw;
                    if (ar.contains(h)) continue;
                    ar.add(h);
                    if (addSubtype) if (Modifier.isAbstract(h.getModifiers()) || h.isInterface())
                        getExtendedClassFromJson(jo, raw, cl, true, ar);
                    
                }catch(Throwable ignored){
                
                }
            }
        }
        return ar;
    }
    
    public static Reflections getReflection(InputStream is, ClassLoader cl) {
        ConfigurationBuilder config = ConfigurationBuilder.build().setSerializer(new JsonSerializer());
        config.setClassLoaders(new ClassLoader[]{cl});
        Reflections reflections = new Reflections(config);
        reflections.collect(is);
        return reflections;
    }
    
    public static <E> Set<Class<? extends E>> getExtendedClass(String packageName, Class<E> e) {
        Reflections reflections = new Reflections(packageName, SubTypesScanner.class);
        return reflections.getSubTypesOf(e);
    }
    
    public static <E> Set<Class<? extends E>> getExtendedClass(String packageName, Class<E> e, ClassLoader cl) {
        Reflections reflections = new Reflections(packageName, SubTypesScanner.class, cl);
        return reflections.getSubTypesOf(e);
    }
    
}
