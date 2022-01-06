package Atom.Utility;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

//why we have 2 type register?
public class NotifyService {
    public final Map<Object, Set<Consumer<?>>> listeners = Collections.synchronizedMap(new java.util.HashMap<>());


    public void onC(Class<?> clazz, Runnable listener) {
        onC(clazz, c -> listener.run());
    }

    /**
     * Triggered when an object has the same class
     *
     * @param clazz    class to listen for
     * @param listener give object to this listener
     * @param <T>
     */
    public <T> void onC(Class<T> clazz, Consumer<T> listener) {
        listeners.computeIfAbsent(clazz, k -> Collections.synchronizedSet(new java.util.HashSet<>())).add(listener);
    }

    public void onT(Object object, Runnable listener) {
        onT(object, c -> listener.run());
    }

    /**
     * Triggered when an object has same hashcode
     *
     * @param obj      object to listen for, not recommended if the object has random hashcode
     * @param listener give object to this listener
     * @param <T>
     */
    public <T> void onT(T obj, Consumer<T> listener) {
        Object id = sanitize(obj);
        listeners.computeIfAbsent(id, k -> Collections.synchronizedSet(new java.util.HashSet<>())).add(listener);
    }

    public void removeClass(Class<?> clazz) {
        listeners.remove(clazz);
    }

    public <T> void removeClass(Class<T> clazz, Consumer<T> listener) {
        listeners.computeIfPresent(clazz, (k, v) -> {
            v.remove(listener);
            return v;
        });
    }

    protected Object sanitize(Object obj) {
        if (obj.getClass() == Class.class) return new ClassWrapper((Class<?>) obj);
        return obj;
    }

    public void removeType(Object obj, Consumer<?> listener) {
        obj = sanitize(obj);
        listeners.computeIfPresent(obj, (k, v) -> {
            v.remove(listener);
            return v;
        });
    }

    public void removeType(Object obj) {
        obj = sanitize(obj);
        listeners.remove(obj);
    }


    public final void fireC(Object... objs) {
        for (Object obj : objs) {
            fireC(obj);
        }
    }

    public <T> void fireC(T obj) {
        Class<?> clazz = obj.getClass();
        fire0(clazz, obj);
    }

    public boolean hasListenerClass(Class<?> clazz) {
        return listeners.containsKey(clazz);
    }

    public boolean hasListenerType(Object obj) {
        Object id = sanitize(obj);
        return listeners.containsKey(id);
    }

    public <T> void fire0(Object o, T obj) {
        listeners.computeIfPresent(o, (k, v) -> {
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

    public void fireT(Object obj) {
        Object id = sanitize(obj);
        fire0(id, obj);
    }

    private static class ClassWrapper {
        private final Class<?> clazz;

        public ClassWrapper(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClassWrapper that = (ClassWrapper) o;

            return clazz.equals(that.clazz);
        }

        @Override
        public int hashCode() {
            return clazz.hashCode() >> 4;
        }
    }
}
