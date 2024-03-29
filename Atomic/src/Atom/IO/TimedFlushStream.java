package Atom.IO;

import Atom.Struct.FunctionalPoolObject;

import java.io.IOException;
import java.util.function.Consumer;

public class TimedFlushStream extends OutputStreamString {
    long lastFlush = System.currentTimeMillis();
    final long interval;
    final long[] nextFlush = new long[1];
    final Consumer<String> onFlush;
    
    public TimedFlushStream(long interval, Consumer<String> onFlush) {
        this.interval = interval;
        this.onFlush = onFlush;
        reset();
    }
    
    @Override
    public void flush() throws IOException {
        if (nextFlush[0] > System.currentTimeMillis()) return;
        onFlush.accept(toString());
        reset();
    }
    public void reset() {
        if (stringBuilder != null) FunctionalPoolObject.StringBuilder.free(stringBuilder);
        stringBuilder = FunctionalPoolObject.StringBuilder.obtain();
        ;
        nextFlush[0] = System.currentTimeMillis() + interval;
    }
}
