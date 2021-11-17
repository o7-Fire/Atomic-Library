package Atom.IO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class GenericEventManager {
    protected final HashMap<Class<?>, List<Consumer<?>>> eventMap = new HashMap<>();
    
    public <T> void addEventListener(Class<T> eventType, Consumer<T> listener) {
        List<Consumer<?>> list = eventMap.getOrDefault(eventType, new ArrayList<>());
        list.add(listener);
        if (!eventMap.containsKey(eventType)){
            eventMap.put(eventType, list);
        }
    }
    
    public <T> void removeEventListener(Class<T> eventType, Consumer<T> listener) {
        List<Consumer<?>> list = eventMap.get(eventType);
        if (list != null) list.remove(listener);
    }
    
    public <T> void removeAllEventListener(Class<T> eventType) {
        eventMap.remove(eventType);
    }
    
    public <T> void fireEvent(T event) {
        List<Consumer<?>> list = eventMap.get(event.getClass());
        if (list != null){
            list = new ArrayList<>(list);
            for (Consumer<?> listener : list) {
                Consumer<T> castedListener = (Consumer<T>) listener;
                castedListener.accept(event);
            }
        }
    }
}
