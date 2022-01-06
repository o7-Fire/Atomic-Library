package Atom.Utility;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Notify {
    public static final NotifyService service = new NotifyService();

    public static <T> void onC(Class<T> clazz, Consumer<T> listener) {
        service.onC(clazz, listener);
    }

    public static <T> void onT(T obj, Consumer<T> listener) {
        service.onT(obj, listener);
    }

    public static <T> void removeClass(Class<T> clazz, Consumer<T> listener) {
        service.removeClass(clazz, listener);
    }

    public static void removeType(Object obj, Consumer<?> listener) {
        service.removeType(obj, listener);
    }

    public static void removeType(Object obj) {
        service.removeType(obj);
    }

    public static void fireC(Object... objs) {
        service.fireC(objs);
    }

    public static <T> void fireC(T obj) {
        service.fireC(obj);
    }

    public static <T> void fire0(Object t, T obj) {
        service.fireC(t, obj);
    }

    public static void fireT(Object obj) {
        service.fireT(obj);
    }

    //test
    public static void main(String[] args) {
        AtomicInteger passCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        Notify.onT(Notify.class, notify -> {
            System.out.println("Notify: " + notify.hashCode());
            passCount.getAndIncrement();
        });
        Notify.onT("LoL", c -> {
            System.out.println(c);
            assert c.equals("LoL") : "Expected: LoL, Actual: " + c;
            passCount.getAndIncrement();
        });
        Notify.onC(Object.class, o -> {
            System.out.println("Shouldn't be invoked");
            failCount.getAndIncrement();
        });
        Notify.onC(String.class, s -> {
            assert s.equals("LoL") : "Expected: LoL, Actual: " + s;
            passCount.getAndIncrement();
        });
        Notify.fireT(Notify.class);//should not invoke
        Notify.fireT("LoL");//should invoke
        Notify.fireC(new Notify());//should invoke
        Notify.removeType(Object.class);
        Notify.fireC(new Object());//should not invoke
        Notify.fireC("LoL");//should invoke
        Notify.removeType(Notify.class);
        assert passCount.get() == 3 : "passCount: " + passCount.get();
        assert failCount.get() == 0 : "failCount: " + failCount.get();
    }

}



