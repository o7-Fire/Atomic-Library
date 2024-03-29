package Atom.Encoding;

import Atom.Struct.FunctionalPoolObject;

import java.io.*;
import java.util.*;

public class Encoder {
    private static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    
    public static String base64Encode(byte[] bytes) {
        return getString(Base64.getEncoder().encode(bytes));
    }
    
    public static byte[] base64Decode(String s) {
        return Base64.getDecoder().decode(s);
    }
    
    public static String getString(byte[] bytes) {
        return new String(bytes);
    }
    
    
    public static ByteArrayOutputStream toByteArrayOutputStream(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        
        int nRead;
        byte[] data = new byte[16384];
        
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        
        return buffer;
    }
    
    public static String readString(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = FunctionalPoolObject.StringBuilder.obtain();
        for (String line; (line = r.readLine()) != null; ) {
            total.append(line).append('\n');
        }
        String s = total.toString();
        FunctionalPoolObject.StringBuilder.free(total);
        return s;
    }
    
    //copied from java
    public static byte[] readAllBytes(InputStream is, int len) throws IOException {
        if (len < 0){
            throw new IllegalArgumentException("len < 0");
        }
        
        List<byte[]> bufs = null;
        byte[] result = null;
        int total = 0;
        int remaining = len;
        int n;
        do {
            byte[] buf = new byte[Math.min(remaining, DEFAULT_BUFFER_SIZE)];
            int nread = 0;
            
            // read to EOF which may read more or less than buffer size
            while ((n = is.read(buf, nread, Math.min(buf.length - nread, remaining))) > 0) {
                nread += n;
                remaining -= n;
            }
            
            if (nread > 0){
                if (MAX_BUFFER_SIZE - total < nread){
                    throw new OutOfMemoryError("Required array size too large");
                }
                total += nread;
                if (result == null){
                    result = buf;
                }else{
                    if (bufs == null){
                        bufs = new ArrayList<>();
                        bufs.add(result);
                    }
                    bufs.add(buf);
                }
            }
            // if the last call to read returned -1 or the number of bytes
            // requested have been read then break
        }while (n >= 0 && remaining > 0);
        
        if (bufs == null){
            if (result == null){
                return new byte[0];
            }
            return result.length == total ? result : Arrays.copyOf(result, total);
        }
        
        result = new byte[total];
        int offset = 0;
        remaining = total;
        for (byte[] b : bufs) {
            int count = Math.min(b.length, remaining);
            System.arraycopy(b, 0, result, offset, count);
            offset += count;
            remaining -= count;
        }
        
        return result;
    }
    
    public static byte[] readAllBytes(InputStream is) throws IOException {
        return readAllBytes(is, Integer.MAX_VALUE);
    }
    
    public static Properties propertiesFromString(String s) {
        Properties p = new Properties();
        try {
            p.load(new StringReader(s));
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        return p;
    }
    
    public static String getStringFromProperties(Properties p, Object comments) {
        StringWriter writer = new StringWriter();
        try {
            p.store(writer, comments + "");
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        return writer.getBuffer().toString();
    }
    
    public static String property(Map<String, String> se) {
        StringBuilder sb = FunctionalPoolObject.StringBuilder.obtain();
        for (Map.Entry<String, String> s : se.entrySet())
            sb.append(s.getKey()).append("=").append(s.getValue()).append("\n");
        String s = sb.toString();
        FunctionalPoolObject.StringBuilder.obtain();
        return s;
    }
    
    public static HashMap<String, String> parseProperty(String se) {
        HashMap<String, String> te = new HashMap<>();
        for (String s : se.split("\n")) {
            if (s.endsWith("\r")) s = s.substring(0, s.length() - 1);
            if (!s.startsWith("#")) te.put(s.split("=")[0], s.split("=")[1]);
        }
        return te;
    }
    
    public static HashMap<String, String> parseProperty(InputStream is) throws IOException {
        String se = new String(readAllBytes(is));
        return parseProperty(se);
    }
    
    public String convertFromBaseToBase(String str, int fromBase, int toBase) {
        return Long.toString(Long.parseLong(str, fromBase), toBase);
    }
    
    public long stringToLong(String s) {
        return stringToLong(s, 26);
    }
    
    public long stringToLong(String s, int base) {
        return Long.parseLong(s, base);
    }
}
