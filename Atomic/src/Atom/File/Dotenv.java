package Atom.File;

import Atom.Encoding.Encoder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Dotenv {
    // I  hate property encoding
    public static boolean load(){
       return load(new File(FileUtility.cwd(), ".env"));
    }
    
    public static boolean load(File file) {
        if(!FileUtility.exists(file.getAbsolutePath()))return false;
        try {
            return load(file.toURI().toURL());
        }catch(MalformedURLException e){
            return false;
        }
    }
    
    public static boolean load(URL url) {
    
        try (InputStream is = url.openStream()){
           String s =  Encoder.readString(is);
           return load(s);
        }catch(IOException e){
            return false;
        }
    }
    
    public static boolean load(String data) {
        for (String s : data.split("\n")){
            s = s.trim();
            if(s.startsWith("#"))continue;
            if(!s.contains("="))continue;
            String[] ss = s.split("=", 2);
            if(ss.length != 2)continue;
            String key = ss[0];
            String value = ss[1];
            System.setProperty(key, value);
        }
        return true;
    }
}
