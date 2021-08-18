package Atom.Struct;

import Atom.Exception.CircularReferenceException;
import Atom.Time.Time;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

//linked list..... ftw
public class History<T> implements Serializable {
    protected final T current;
    protected final long currentTime;
    protected History<T> past;
    protected transient History<T> future;//circular ref
    protected Time time;
    
    {
        if (past != null && past.future == null) past.future = this;
        if (future != null && future.past == null) future.past = this;
    }
    
    public History(T val) {
        this(val, null);
    }
    
    public History(T value, History<T> past) {
        this.past = past;
        current = value;
        currentTime = System.currentTimeMillis();
    }
    
    @Nullable
    public History<T> getPast() {
        if (past == this) throw new CircularReferenceException("Past == Current," + this);
        return past;
    }
    
    @Nullable
    public History<T> getFuture() {
        if (future == this) throw new CircularReferenceException("Future == Current," + this);
        return future;
    }
    
    
    public T get() {
        return current;
    }
    
    public History<T> future(T object) {
        if (object == null) return future = null;
        return future = new History<>(object, this);
    }
    
    public Time asTime() {
        if (time != null) return time;
        return time = new Time(TimeUnit.MILLISECONDS, currentTime);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = FunctionalPoolObject.StringBuilder.obtain();
        sb.append("Past:").append(getPast() == null ? "unknown" : getPast().current).append("\n");
        sb.append("Current:").append(current).append("\n");
        sb.append("Future:").append(getFuture() == null ? "unknown" : getFuture().current).append("\n");
        return sb.toString();
    }
}
