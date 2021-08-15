package Atom.Translation;

import Atom.Encoding.EncoderURL;
import Atom.Struct.FunctionalPoolObject;
import Atom.Utility.Pool;
import org.jsoup.parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//rate limit ?
//html4 unescape not included
public class GoogleTranslate extends CachedTranslator {
    
    public static final String regex = "class=\"result-container\">([^<]*)<\\/div>";
    public static final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
    
    //test
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        GoogleTranslate googleTranslate = new GoogleTranslate();
        assert googleTranslate.translate("what", "fr", "en") == null : "Should return null when language target same";
        System.out.println(googleTranslate.translate("what", "en", "fr"));
        System.out.println(googleTranslate.translate("what", "fr", "en"));
        System.out.println(googleTranslate.translate("\tテストマル\n呪いを解く", "ja", "en"));
        System.out.println(googleTranslate.translate("{@!テストマル\n呪いを解く!!!<<<>>", "ja", "en"));
        System.out.println(googleTranslate.translate(Locale.ENGLISH, Locale.CHINESE, "hi").get());
        System.out.println(googleTranslate.translate(Locale.CHINESE, Locale.ENGLISH, "hi").get().equals("hi"));
        assert googleTranslate.translate(Locale.CHINESE, Locale.ENGLISH, "hi").get().equals("hi") : "yeet";
    }
    
    public static String parseHTML(String html) {
        Matcher matcher = pattern.matcher(html);
        matcher.find();
        String match = matcher.group(1);
        if (match == null || match.isEmpty()) return null;
        return Parser.unescapeEntities(match, false);
    }
    
    public String translate(String text, String langFrom, String langTo) throws IOException {
        String html = fetch(text, langFrom, langTo);
        String translated = parseHTML(html);
        if (text.equalsIgnoreCase(translated)) return null;
        return translated;
    }
    
    public String fetch(String text, String langFrom, String langTo) throws IOException {
        String encodedText = EncoderURL.encode(text.trim(), charset());
        String urlString = String.format("https://translate.google.com/m?hl=en&sl=%s&tl=%s&ie=UTF-8&prev=_m&q=%s", langFrom, langTo, encodedText);
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset()))) {
            StringBuilder html = FunctionalPoolObject.StringBuilder.obtain();
            String line;
            while ((line = br.readLine()) != null) html.append(line).append("\n");
            String s = html.toString();
            FunctionalPoolObject.StringBuilder.free(html);
            return s;
        }
    }
    
    @Override
    public Future<String> translate(Locale from, Locale to, String text) {
        String key = getKey(from, to, text);
        Future<String> h = super.translate(from, to, text);
        if (h != null) return h;
        return Pool.submit(() -> {
            String translated = translate(text, from.getLanguage(), to.getLanguage());
            if (translated == null) translated = text;
            cache.setProperty(key, translated);
            return translated;
        });
    }
}
