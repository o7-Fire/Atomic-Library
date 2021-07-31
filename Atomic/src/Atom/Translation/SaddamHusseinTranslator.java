package Atom.Translation;

import Atom.String.WordGenerator;
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
    }
    
    @Override
    public Future<String> translate(Locale from, Locale to, String text) {
        return (InstantFuture<String>) () -> Utility.capitalizeEnforce(WordGenerator.newWord(text.length() < 4 ? 5 : text.length()).toString());
    }
}
