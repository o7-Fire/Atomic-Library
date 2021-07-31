package Atom.Translation;

import Atom.Reflect.UnThread;
import Atom.Struct.InstantFuture;
import Atom.Utility.Pool;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.Future;

public abstract class CachedTranslator implements Translator {
    //public final Map<String, Properties> staticCache = Collections.synchronizedMap(new HashMap<>());
    public final Properties cache = new Properties();
    public File cacheFile = new File("cache-translation.properties"), cacheFileBackup = new File("backup-cache-translation.properties");
    public long lastSize = 0;
    private Charset charset = Charset.forName("UTF-8");
    
    public static String getKey(Locale from, Locale to, String text) {
        return from.getLanguage() + "-" + to.getLanguage() + "-" + text;
    }
    
    @Override
    public void setCharset(Charset charset) {
        this.charset = charset;
    }
    
    @Override
    public Charset charset() {
        return charset;
    }
    
    public void loadCache() throws IOException {
        if (!cacheFile.exists()) return;
        try(Reader reader = new InputStreamReader(new FileInputStream(cacheFile), charset())) {
            cache.load(reader);
        }catch(IOException e){
            try(Reader reader = new InputStreamReader(new FileInputStream(cacheFileBackup), charset())) {
                cache.load(reader);
            }
        }
        lastSize = cache.size();
    }
    
    public void purgeCache() {
        cache.clear();
        cacheFile.delete();
    }
    
    public void saveHook(long interval) {
        Pool.daemon(() -> {
            while (!Thread.interrupted()) {
                UnThread.sleep(interval);
                if (lastSize != cache.size()){
                    try {
                        saveCache();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    public void saveShutdownHook() {
        Runtime.getRuntime().addShutdownHook(Pool.thread(() -> {
            try {
                saveCache();
            }catch(IOException e){
                e.printStackTrace();
            }
        }));
    }
    
    public void saveCache() throws IOException {
        try(Writer writer = new OutputStreamWriter(new FileOutputStream(cacheFile), charset())) {
            cache.store(writer, "Shared");
        }
        try(Writer writer = new OutputStreamWriter(new FileOutputStream(cacheFileBackup), charset())) {
            cache.store(writer, "Dont kill process in middle of saving, or killing process, or cache gone");
        }
        lastSize = cache.size();
    }
    
    @Override
    @Nullable
    public Future<String> translate(Locale from, Locale to, String text) {
        String key = getKey(from, to, text);
        if (cache.containsKey(key)){
            return (InstantFuture<String>) () -> cache.getProperty(key);
        }
        return null;
    }
}
