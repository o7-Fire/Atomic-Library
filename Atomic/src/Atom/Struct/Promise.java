package Atom.Struct;

import Atom.Exception.ShouldNotHappenedException;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class Promise<V> implements Future<V> {
    protected final Object lock = new Object();
    protected CancelPromise cancelPromise;
    protected Consumer<Throwable> onError;
    protected Consumer<V> onFinished;
    protected V result;
    protected Throwable err;
    protected Status status = Status.INCOMPLETE;
    protected short waiterCount = 0;

    public synchronized void setResult(V result) {
        this.result = result;
        status = Status.SUCCESS;
        if (onFinished != null) {
            onFinished.accept(result);
        }
        lock.notifyAll();
    }

    public synchronized void error(Throwable error) {
        if (onError != null) {
            onError.accept(error);
        }
        err = error;
        status = Status.FAILURE;
        lock.notifyAll();
    }

    @Override
    public synchronized boolean cancel(boolean mayInterruptIfRunning) {
        if (cancelPromise == null) {
            return false;
        }
        boolean cancelled = cancelPromise.cancel(mayInterruptIfRunning);
        if (cancelled) {
            status = Status.CANCELLED;
            lock.notifyAll();
        }
        return true;
    }

    @Override
    public boolean isCancelled() {
        return status == Status.CANCELLED;
    }

    @Override
    public boolean isDone() {
        return status != Status.INCOMPLETE;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        try {
            return get(0, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new ShouldNotHappenedException("Should not happen, got timeout but no is timeout set");
        }
    }

    @Override
    public V get(long timeout, @NotNull TimeUnit unit) throws
            InterruptedException,
            ExecutionException,
            TimeoutException {
        try {
            return await(unit.toMillis(timeout));

        } catch (Throwable e) {
            if (e instanceof InterruptedException) {
                throw (InterruptedException) e;
            }
            if (e instanceof TimeoutException) {
                throw (TimeoutException) e;
            }
            throw new ExecutionException(e);
        }
    }

    public V await() throws Throwable {
        return await(0);
    }

    /**
     * @param timeout in milliseconds
     * @return
     * @throws Throwable
     */
    public V await(long timeout) throws Throwable {
        if (isDone()) {
            return result;
        }
        if (Thread.interrupted()) {
            throw new InterruptedException(toString());
        }
        synchronized (lock) {
            while (!isDone()) {
                waiterCount++;
                if (waiterCount >= Short.MAX_VALUE) {
                    throw new IllegalStateException("Too many waiters");
                }
                try {
                    lock.wait(timeout);
                } catch (InterruptedException e) {
                    throw new InterruptedException(toString());
                } finally {
                    waiterCount--;
                }
            }
        }
        if (err != null) {
            throw err;
        }
        if (timeout > 0 && status == Status.INCOMPLETE) {
            throw new TimeoutException(toString());
        }
        return result;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Promise[" + status + "]#" + hashCode();
    }

    public enum Status {INCOMPLETE, SUCCESS, FAILURE, CANCELLED}

    @FunctionalInterface
    public interface CancelPromise {
        boolean cancel(boolean mayInterruptIfRunning);
    }
}
