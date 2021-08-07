package Atom.Scrapper;

import Atom.API.API;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

public class UrbanDictionary implements API {
    
    public static void main(String[] args) throws IOException {
        scrap("kys");
    }
    
    @Nullable
    public static Map<String, String>[] scrap(String term) throws IOException {
        Document d = Jsoup.connect("https://www.urbandictionary.com/define.php?term=" + URLEncoder.encode(term)).followRedirects(true).get();
        if (d.getElementById("content") == null){
            return null;
        }
        return null;
    }
    
    
    public static void sendDefinition(Element def) {
        String title = Jsoup.parse(def.childNode(1).toString()).text();
        String description = Jsoup.parse(def.childNode(2).toString().replaceAll("<br>", "BL")).text().replaceAll("BL", "\n");
        String example = Jsoup.parse(def.childNode(3).toString().replaceAll("<br>", "BL")).text().replaceAll("BL", "\n");
        
        
    }
    
    @Override
    public String getDescription() {
        return "www.urbandictionary.com";
    }
}
