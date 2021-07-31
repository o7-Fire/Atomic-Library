package Atom.Translation;

import Atom.Encoding.Encoder;
import Atom.Utility.Pool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;

//need python installed and translate-cli
public class TranslateCLIPythonTranslator implements Translator {
    
    public static final String binPath;
    
    static {
        if (System.getProperty("CLITranslator.bin") != null) binPath = System.getProperty("CLITranslator.bin");
        else binPath = "translate-cli";
    }
    
    public final Map<String, String> cache = Collections.synchronizedMap(new HashMap<>());
    
    
    @Override
    public Future<String> translate(Locale from, Locale to, String text) {
        return Pool.submit(() -> {
            
            String key = from.getLanguage() + "-" + to.getLanguage() + "-" + text;
            if (cache.containsKey(key)) return cache.get(key);
            String[] cmd = new String[]{binPath, "-f", from.getLanguage(), "-t", to.getLanguage(), "-o", '"' + text + '"'};
            //System.out.print("  " + Arrays.toString(cmd) + ": ");
            String h = new String(Encoder.readAllBytes(new ProcessBuilder(cmd).start().getInputStream()), charset());
            //System.out.print(h);
            cache.put(key, h);
            return h;
            
        });
    }
}
