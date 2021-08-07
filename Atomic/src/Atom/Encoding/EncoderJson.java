package Atom.Encoding;

import com.google.gson.*;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class EncoderJson {
    public static Gson gson = new Gson();
    public static WeakHashMap<String, JsonElement> cache = new WeakHashMap<>();
    
    public static JsonElement parseJson(String s) {
        if (!cache.containsKey(s)){
            cache.put(s, JsonParser.parseString(s));
        }
        return cache.get(s);
    }
    
    public static void jsonToMap(JsonElement jo, String prefix, Map<String, String> map) {
        
        if (jo.isJsonNull()){
            map.put(prefix, "null");
            return;
        }else if (jo.isJsonObject()){
            for (Map.Entry<String, JsonElement> s : jo.getAsJsonObject().entrySet()) {
                String pre = prefix + (prefix.isEmpty() ? "" : ".") + s.getKey();
                if (s.getValue() instanceof JsonPrimitive){
                    map.put(pre, s.getValue().getAsString());
                }
                if (s.getValue() instanceof JsonObject){
                    jsonToMap(s.getValue(), pre, map);
                }
            }
        }else if (jo.isJsonArray()){
            int i = 0;
            for (JsonElement s : jo.getAsJsonArray()) {
                String pre = prefix + (prefix.isEmpty() ? "" : ".") + i++;
                jsonToMap(s, prefix, map);
            }
        }else if (jo.isJsonPrimitive()){//in case
            map.put(prefix, jo.getAsJsonPrimitive().getAsString());
        }
    }
    
    public static void jsonToMap(JsonElement jo, Map<String, String> s) {
        jsonToMap(jo, "", s);
    }
    
    public static Map<String, String> jsonToMap(JsonElement jo) {
        HashMap<String, String> map = new HashMap<>();
        jsonToMap(jo, map);
        return map;
    }
    
    
}
