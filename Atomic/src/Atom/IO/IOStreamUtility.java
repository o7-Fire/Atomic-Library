package Atom.IO;

import Atom.Struct.FunctionalPoolObject;

import java.io.*;
import java.util.function.Consumer;

public class IOStreamUtility {
    
    public static Thread readInputAsync(InputStream i, Consumer<String> handler, char delimiter) {
        return new Thread(() -> {
            try {
                readInputSync(i, handler, delimiter);
            }catch(IOException e){
                e.printStackTrace();
            }
        });
    }
    
    public static OutputStream getReader(Consumer<String> handler) {
        return new OutputStream() {
            private final StringBuilder string = FunctionalPoolObject.StringBuilder.obtain();
            ;
            private final StringBuilder instrumental = FunctionalPoolObject.StringBuilder.obtain();
            ;
    
            @Override
            public void write(int x) {
                if ((char) x == '\n'){
                    handler.accept(instrumental.toString());
                    instrumental.setLength(0);
                }else this.instrumental.append((char) x);
                this.string.append((char) x);
            }
            
            public String toString() {
                return this.string.toString();
            }
        };
    }
    
    public static void readInputSync(InputStream stream, Consumer<String> handler, char delimiter) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        StringBuilder out = FunctionalPoolObject.StringBuilder.obtain();
        InputStreamReader in = new InputStreamReader(stream);
        
        int charsRead;
        while ((charsRead = in.read()) > 0) {
            if (charsRead == delimiter) {
                handler.accept(out.toString());
                out.setLength(0);
                continue;
            }
            out.append((char) charsRead);
        }
        FunctionalPoolObject.StringBuilder.free(out);
    }
    
    public static String readInputSync(InputStream stream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = FunctionalPoolObject.StringBuilder.obtain();
        Reader in = new InputStreamReader(stream);
        int charsRead;
        while ((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
            out.append(buffer, 0, charsRead);
        }
        String s = out.toString();
        FunctionalPoolObject.StringBuilder.free(out);
        return s;
    }
}
