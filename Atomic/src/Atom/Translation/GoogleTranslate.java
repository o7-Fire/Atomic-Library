package Atom.Translation;

import Atom.Encoding.EncoderURL;
import Atom.Reflect.UnThread;
import Atom.Struct.FunctionalPoolObject;
import Atom.Utility.Pool;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.parser.Parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
      public static final String endpointFallback = "https://translate.google.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s";
      public static final String fallbackEndpoint = "https://api.mymemory.translated.net/get?q=%s&langpair=%s%%7C%s";
      private static final long DEFAULT_RETRY_MILLIS = 60000L;
      private static volatile long googleRetryAfter;
      
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
      
      public static String parseFallbackJson(String response) {
          if (response == null || response.isEmpty()) return null;
          JsonElement root = JsonParser.parseString(response);
          if (root == null || !root.isJsonObject()) return null;
          JsonObject object = root.getAsJsonObject();
          JsonElement status = object.get("responseStatus");
          if (status != null && status.isJsonPrimitive() && !"200".equals(status.getAsString())) return null;
          JsonElement data = object.get("responseData");
          if (data == null || !data.isJsonObject()) return null;
          JsonElement translated = data.getAsJsonObject().get("translatedText");
          if (translated == null || translated.isJsonNull()) return null;
          String result = translated.getAsString();
          if (result == null || result.trim().isEmpty()) return null;
          return Parser.unescapeEntities(result, false);
      }
      
      @Deprecated
      public static String parseHTML(String html) {
          Matcher matcher = pattern.matcher(html);
          matcher.find();
          String match = matcher.group(1);
          if (match == null || match.isEmpty()) return null;
          return Parser.unescapeEntities(match, false);
      }
      
      public String translate(String text, String langFrom, String langTo) throws IOException {
          String response = fetch(text, langFrom, langTo);
          String translated = parseJson(response);
          if (translated == null) translated = parseFallbackJson(response);
          if (translated == null || translated.trim().isEmpty()) return null;
          if (text.equalsIgnoreCase(translated)) return null;
          return translated;
      }
      
      public String fetch(String text, String langFrom, String langTo) throws IOException {
          String source = langFrom == null || langFrom.isEmpty() ? "auto" : langFrom;
          String target = langTo == null || langTo.isEmpty() ? "en" : langTo;
          String encodedText = EncoderURL.encode(text.trim(), charset());
          IOException last = null;
          
          if (System.currentTimeMillis() >= googleRetryAfter) {
              for (String url : new String[]{endpoint, endpointFallback}) {
                  for (int attempt = 0; attempt < 2; attempt++) {
                      try {
                          return fetchUrl(String.format(url, source, target, encodedText));
                      }catch(HttpStatusException e){
                          last = e;
                          if (e.status == 429){
                              googleRetryAfter = System.currentTimeMillis() + (e.retryMillis > 0 ? e.retryMillis : DEFAULT_RETRY_MILLIS);
                              break;
                          }
                          if (e.status < 500) break;
                      }catch(IOException e){
                          last = e;
                      }
                      if (attempt == 0) UnThread.sleep(250L);
                  }
                  if (System.currentTimeMillis() < googleRetryAfter) break;
              }
          }
          
          String fallbackLang = "auto".equalsIgnoreCase(source) ? "autodetect" : source;
          try {
              return fetchUrl(String.format(fallbackEndpoint, encodedText, fallbackLang, target));
          }catch(IOException e){
              if (last != null) e.addSuppressed(last);
              throw e;
          }
      }
      
      private static String fetchUrl(String urlString) throws IOException {
          HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
          connection.setConnectTimeout(5000);
          connection.setReadTimeout(10000);
          connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
          try {
              int status = connection.getResponseCode();
              if (status < 200 || status >= 300) throw new HttpStatusException("Translation HTTP " + status, status, parseRetryAfter(connection));
              try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                  StringBuilder result = FunctionalPoolObject.StringBuilder.obtain();
                  String line;
                  while ((line = br.readLine()) != null) result.append(line).append("\n");
                  String response = result.toString();
                  FunctionalPoolObject.StringBuilder.free(result);
                  return response;
              }
          } finally {
              connection.disconnect();
          }
      }
      
      private static long parseRetryAfter(HttpURLConnection connection) {
          String value = connection.getHeaderField("Retry-After");
          if (value == null || value.trim().isEmpty()) return 0L;
          try {
              return Math.max(0L, Long.parseLong(value.trim()) * 1000L);
          }catch(NumberFormatException ignored){}
          try {
              java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
              format.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
              return Math.max(0L, format.parse(value.trim()).getTime() - System.currentTimeMillis());
          }catch(Exception ignored){
              return 0L;
          }
      }
      
      @Override
      public Future<String> translate(Locale from, Locale to, String text) {
          String key = getKey(from, to, text);
          Future<String> h = super.translate(from, to, text);
          if (h != null) return h;
          return Pool.submit(() -> {
              String translated = null;
              try {
                  translated = translate(text, from.getLanguage(), to.getLanguage());
              }catch(IOException ignored){}
              if (translated == null || translated.trim().isEmpty()) translated = text;
              cache.setProperty(key, translated);
              return translated;
          });
      }
      
      private static final class HttpStatusException extends IOException {
          final int status;
          final long retryMillis;
          
          HttpStatusException(String message, int status, long retryMillis) {
              super(message);
              this.status = status;
              this.retryMillis = retryMillis;
          }
      }
}
