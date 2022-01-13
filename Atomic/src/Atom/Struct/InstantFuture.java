package Atom.Struct;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
//life hack
@FunctionalInterface
public interface InstantFuture<V> extends Future<V> {
    @Override
    public default boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public default boolean isCancelled() {
        return false;
    }

    @Override
    public default boolean isDone() {
        return true;
    }

    V get();

    @Override
    public default V get(long timeout, @NotNull TimeUnit unit) {
        return get();
    }
}
