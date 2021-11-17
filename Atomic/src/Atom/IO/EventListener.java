package Atom.IO;

import Atom.Class.Event;

@FunctionalInterface
public interface EventListener<T extends Event> {
    void onEvent(T event);
}
