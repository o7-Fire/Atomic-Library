package Atom.Translation;

import Atom.Encoding.EncoderURL;
import Atom.Exception.GetRealException;
import Atom.Reflect.UnThread;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//Using url fetch method provide inaccurate translation, like loss of special char, or loss of meaning
//selenium not included
public class ChromeDriverDeepLTranslator extends CachedTranslator {
    public static final String textAreaSource = "/html/body/div[2]/div[1]/div[3]/div[4]/div[1]/div[2]/div[1]/textarea", textAreaTarget = "/html/body/div[2]/div[1]/div[3]/div[4]/div[3]/div[3]/div[1]/textarea";
    public static final String link = "https://www.deepl.com/translator#";
    public static final String test = link + "ja/en/回視聴";
    private static final ThreadLocal<String> previousTranslated = ThreadLocal.withInitial(String::new);
    private static final ThreadLocal<RemoteWebDriver> webDriverProvider = ThreadLocal.withInitial(ChromeDriver::new);
    private static final ExecutorService executors = Executors.newFixedThreadPool(1, r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setName(t.getName() + "-" + ChromeDriverDeepLTranslator.class.getSimpleName());
        t.setDaemon(true);
        return t;
    });
    
    static {
        if (System.getProperty("webdriver.chrome.driver") == null){
            System.setProperty("webdriver.chrome.driver", "chromedriver");
        }
        File f = new File("chromedriver");
        if (!f.exists()) throw new GetRealException("Chromedriver binary not found: " + f.getAbsolutePath());
        assert !textAreaSource.equals(textAreaTarget);
    }
    
    public long waitEach = 480;
    public long waitLock = 90;
    
    public ChromeDriverDeepLTranslator() {
    
    }
    
    //test
    public static void main(String[] args) throws Throwable {
        ChromeDriver chromeDriver = new ChromeDriver();
        chromeDriver.get(test);
        WebElement src = chromeDriver.findElement(By.xpath(textAreaSource));
        WebElement target = chromeDriver.findElement(By.xpath(textAreaTarget));
        assert src.getAttribute("value").equals("Viewing");
        assert target.getAttribute("value").equals("回視聴");
        src.clear();
        assert src.getAttribute("value").equals("") : "Not cleared";
        src.sendKeys("東方ヴォ");
        System.out.println(target.getAttribute("value"));
        assert src.getAttribute("value").equals("東方ヴォ");
        System.out.println(target.getAttribute("value"));
    }
    
    public String translate(RemoteWebDriver webDriver, Locale from, Locale to, String text) {
        String key = getKey(from, to, text);
        if (cache.containsKey(key)) return cache.getProperty(key);
        if (webDriver == null) webDriver = webDriverProvider.get();
        boolean previous = webDriver.getCurrentUrl().startsWith(link);
        if (!previous){
            webDriver.get(link + from.getLanguage() + "/" + to.getLanguage() + "/" + text);
            UnThread.sleep(5000);
            previous = true;
        }
        WebElement src = webDriver.findElement(By.xpath(textAreaSource));
        WebElement target = webDriver.findElement(By.xpath(textAreaTarget));
        WebElement loading = webDriver.findElement(By.xpath("/html/body/div[2]/div[1]/div[3]/div[4]/div[3]/div[3]/div[3]"));
        
        //System.out.print("    " + key + ": ");
        if (previous){
            boolean meh = false;
            while (!meh) {
                while (loading.isDisplayed()) UnThread.sleep(waitLock);
                src.clear();
                while (loading.isDisplayed() || !src.getAttribute("value").isEmpty() || !target.getAttribute("value").isEmpty())
                    UnThread.sleep(waitLock);
                src.sendKeys(text);
                while (loading.isDisplayed()) UnThread.sleep(waitLock);
                UnThread.sleep(waitEach);
                meh = src.getAttribute("value").equals(text);
            }
        }else{
            while (loading.isDisplayed()) UnThread.sleep(waitLock);
            UnThread.sleep(waitEach);
        }
        
        
        if (text.length() < 1800){//max url 2000 char mmm
            boolean loop = false;
            int max = 100;
            int i = 0;
            while (loop) {
                if (webDriver.getCurrentUrl().split("/").length == 6)
                    loop = !webDriver.getCurrentUrl().split("/")[5].equals(EncoderURL.encode(text, charset()));
                UnThread.sleep(waitLock);
                i++;
                if (max < i) break;
            }
        }
        
        String codeLang = webDriver.getCurrentUrl();
        codeLang = codeLang.substring(codeLang.indexOf('#') + 1);
        String[] codeLangArr = codeLang.split("/");
        
        String val = target.getAttribute("value");
        if (codeLangArr.length > 2){
            if (!codeLangArr[0].equals(from.getLanguage()) || !codeLangArr[1].equals(to.getLanguage())) val = text;
        }
        cache.setProperty(key, val);
        previousTranslated.set(val);
        //System.out.println(val);
        src.clear();
        return val;
    }
    
    @Override
    public Future<String> translate(Locale from, Locale to, String text) {
        return executors.submit(() -> translate(null, from, to, text));
    }
}
