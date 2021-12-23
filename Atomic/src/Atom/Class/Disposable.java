package Atom.Class;

import Atom.Struct.ObjectPoolPattern;

/**
 * Auto dispose by {@link Atom.Struct.ObjectPoolPattern} on {@link ObjectPoolPattern#clear()} or for other purpose idk
 */
public interface Disposable {
    public void dispose();
}
