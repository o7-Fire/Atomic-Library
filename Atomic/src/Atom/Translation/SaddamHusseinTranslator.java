package Atom.Translation;

import Atom.String.WordGenerator;
import Atom.Struct.FunctionalPoolObject;
import Atom.Struct.InstantFuture;
import Atom.Utility.Utility;

import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

//last resort
public class SaddamHusseinTranslator implements Translator {
    
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Translator translator = new SaddamHusseinTranslator();
        System.out.println(translator.translate(Locale.ENGLISH, Locale.CHINESE, "hi").get());
        System.out.println(translator.translate(Locale.ENGLISH, Locale.CHINESE, "A frankenstein library that nobody ask for").get());
    }
    
    @Override
    public Future<String> translate(Locale from, Locale to, String text) {
        return (InstantFuture<String>) () -> {
            StringBuilder sb = FunctionalPoolObject.StringBuilder.obtain();
            for (String s : text.split(" ")) {
                StringBuilder sbb = WordGenerator.randomWord(s.length());
                sb.append(sbb);
                sb.append(" ");
                FunctionalPoolObject.StringBuilder.free(sbb);
            }
    
            String s = Utility.capitalizeEnforce(sb.toString());
            FunctionalPoolObject.StringBuilder.free(sb);
            return s.trim();
        };
    }
}
