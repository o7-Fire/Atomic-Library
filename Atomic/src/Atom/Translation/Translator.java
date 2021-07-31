package Atom.Translation;

import Atom.Exception.GetRealException;
import Atom.Manifest;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.Future;

public interface Translator {
    default void setCharset(Charset charset) {
        throw new GetRealException();
    }
    
    default Charset charset() {
        return Manifest.charset;
    }
    
    /**
     * Throw {@link RuntimeException} if can't return translated text
     */
    Future<String> translate(Locale from, Locale to, String text);
}
