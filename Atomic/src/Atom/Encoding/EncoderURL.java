package Atom.Encoding;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Map;

public class EncoderURL {
    public static String toASCII(URL url) throws URISyntaxException {
        URI uri = new URI(url.getProtocol(), url.getUserInfo(), IDN.toASCII(url.getHost()), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        return uri.toASCIIString();
    }
    
    public static String toASCII(String urls) throws URISyntaxException, MalformedURLException {
        return toASCII(new URL(urls));
    }
    
    public static String encode(String s, Charset charset) {
        try {
            return URLEncoder.encode(s, charset.name());
        }catch(UnsupportedEncodingException e){
            throw new UnsupportedOperationException(e);
        }
    }
    
    public static String encodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }catch(UnsupportedEncodingException e){
            throw new UnsupportedOperationException(e);
        }
    }
    
    public static String encodeUTF8(Map<String, ?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            if (sb.length() > 0){
                sb.append("&");
            }
            sb.append(String.format("%s=%s", encodeUTF8(entry.getKey()), encodeUTF8(entry.getValue().toString())));
        }
        return sb.toString();
    }
}
