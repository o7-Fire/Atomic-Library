package Atom.API;

import Atom.Encoding.Encoder;
import Atom.Encoding.EncoderJson;
import Atom.Utility.Cache;
import com.google.gson.JsonParser;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class CommonIPTrackerAbstract implements IPLookup {
    Map<String, String> cache = new HashMap<>();
    String api, ip = "";
    
    public CommonIPTrackerAbstract(String ip) {
        ip = ip.trim();
        if (!IPLookup.isValidIPAddress(ip)) throw new IllegalArgumentException("Invalid IP");
        setIP(ip);
    }
    
    @Override
    //assume return json
    public Map<String, String> getProperty() {
        if (cache.containsKey("TrackerProvider")) return cache;
        try {
            URL url = new URL(api);
            url = Cache.tryCache(url);
            String s = Encoder.readString(url.openStream());
            EncoderJson.jsonToMap(JsonParser.parseString(s), cache);
            cache.put("TrackerProvider", this.getClass().getSimpleName());
            return cache;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        
    }
    
    @Override
    public String getIP() {
        return ip;
    }
    
    public void setIP(String ip) {
        this.ip = ip;
        cache.clear();
    }
    
}
