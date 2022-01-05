package Atom.Utility;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Notify {
    public static final Map<Object, Set<Consumer<?>>> listeners = Collections.synchronizedMap(new java.util.HashMap<>());

    public static <T> void on(Class<T> clazz, Consumer<T> listener) {
        listeners.computeIfAbsent(clazz, k -> Collections.synchronizedSet(new java.util.HashSet<>())).add(listener);
    }

    public static void on(Object obj, Consumer<?> listener) {
        listeners.computeIfAbsent(obj, k -> Collections.synchronizedSet(new java.util.HashSet<>())).add(listener);
    }

    public static <T> void off(Class<T> clazz, Consumer<T> listener) {
        listeners.computeIfPresent(clazz, (k, v) -> {
            v.remove(listener);
            return v;
        });
    }

    public static void off(Object obj, Consumer<?> listener) {
        listeners.computeIfPresent(obj, (k, v) -> {
            v.remove(listener);
            return v;
        });
    }

    public static void off(Object obj) {
        listeners.remove(obj);
    }

    @SafeVarargs
    public static <T> void fire(T... objs) {
        for (T obj : objs) {
            fire(obj);
        }
    }

    public static <T> void fire(T obj) {
        Class<?> clazz = obj.getClass();
        fire(clazz, obj);
    }

    public static <T> void fire(Class<?> clazz, T obj) {
        listeners.computeIfPresent(clazz, (k, v) -> {
            for (Consumer<?> c : v) {
                try {
                    Consumer<T> cc = (Consumer<T>) c;
                    cc.accept(obj);
                } catch (ClassCastException ignored) {

                }
            }
            return v;
        });
    }

    public static void run(Object obj) {
        listeners.computeIfPresent(obj, (k, v) -> {
            v.forEach(c -> c.accept(null));
            return v;
        });
    }

    //test
    public static void main(String[] args) {
        boolean[] testCase = {false, false, true};
        Notify.on(Notify.class, notify -> {
            System.out.println("Notify: " + notify.hashCode());
            testCase[0] = true;
        });
        Notify.on("LoL", c -> {
            System.out.println(c);
            testCase[1] = true;
        });
        Notify.on(Object.class, o -> {
            System.out.println("Shouldn't be invoked");
            testCase[2] = false;
        });
        Notify.run(Notify.class);
        Notify.run("LoL");
        Notify.fire(Notify.class, new Notify());
        Notify.off(Object.class);
        Notify.fire(Object.class, new Object());
        boolean failed = false;
        for (int i = 0; i < testCase.length; i++) {
            if (!testCase[i]) {
                failed = true;
                System.out.println("TestCase " + i + " failed");
            }
        }
        if (!failed) {
            System.out.println("All TestCase passed");
        } else {
            throw new AssertionError("TestCase failed");
        }
    }

}



