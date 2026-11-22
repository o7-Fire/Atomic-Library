package Atom.Translation;

import Atom.Encoding.EncoderURL;
import Atom.Struct.FunctionalPoolObject;
import Atom.Utility.Pool;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jsoup.parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleTranslate extends CachedTranslator {
     
     @Deprecated
     public static final String regex = "class=\"result-container\">([^<]*)<\\/div>";
     @Deprecated
     public static final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
     
     public static final String endpoint = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s";
     
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
     
     /**
      * Extracts the translated text from the {@code translate_a/single} JSON response.
      *
      * @return the translation, or null if the response has no usable translation
      */
     public static String parseJson(String response) {
         if (response == null || response.isEmpty()) return null;
         JsonElement root = JsonParser.parseString(response);
         if (root == null || !root.isJsonArray() || root.getAsJsonArray().size() == 0) return null;
         JsonElement sentencesElement = root.getAsJsonArray().get(0);
         if (!sentencesElement.isJsonArray()) return null;
         
         StringBuilder result = new StringBuilder();
         for (JsonElement sentence : sentencesElement.getAsJsonArray()) {
             if (!sentence.isJsonArray()) continue;
             JsonArray parts = sentence.getAsJsonArray();
             if (parts.size() == 0 || parts.get(0).isJsonNull()) continue;
             result.append(parts.get(0).getAsString());
         }
         return result.toString();
     }
     
     /** @deprecated the {@code /m} page is no longer fetched; use {@link #parseJson(String)}. */
     @Deprecated
     public static String parseHTML(String html) {
         Matcher matcher = pattern.matcher(html);
         matcher.find();
         String match = matcher.group(1);
         if (match == null || match.isEmpty()) return null;
         return Parser.unescapeEntities(match, false);
     }
     
     public String translate(String text, String langFrom, String langTo) throws IOException {
         String translated = parseJson(fetch(text, langFrom, langTo));
         if (translated == null || translated.trim().isEmpty()) return null;
         if (text.equalsIgnoreCase(translated)) return null;
         return translated;
     }
     

     /** Fetches the JSON . Throws an {@link IOException} if the request fails.*/
     public String fetch(String text, String langFrom, String langTo) throws IOException {
         String encodedText = EncoderURL.encode(text.trim(), charset());
         String urlString = String.format(endpoint, langFrom, langTo, encodedText);
         HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
         connection.setConnectTimeout(5000);
         connection.setReadTimeout(10000);
         connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
         try {
             int status = connection.getResponseCode();
             if (status < 200 || status >= 300) {
                 throw new IOException("Google Translate HTTP " + status);
             }
             try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset()))) {
                 StringBuilder html = FunctionalPoolObject.StringBuilder.obtain();
                 String line;
                 while ((line = br.readLine()) != null) html.append(line).append("\n");
                 String s = html.toString();
                 FunctionalPoolObject.StringBuilder.free(html);
                 return s;
             }
         } finally {
             connection.disconnect();
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
