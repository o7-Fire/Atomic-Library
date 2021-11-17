package Atom.IO;

import Atom.Class.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventManager<T extends EventListener<E>, E extends Event> {
    public final List<T> listeners = Collections.synchronizedList(new ArrayList<>());
    
    public void fire(E event) {
        for (T listener : new ArrayList<>(listeners)) {
            listener.onEvent(event);
        }
    }
    
    public void addListener(T listener) {
        listeners.add(listener);
    }
}
