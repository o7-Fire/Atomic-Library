package Atom.Utility;


import Atom.Exception.ShouldNotHappenedException;
import Atom.Reflect.OS;
import Atom.Struct.FunctionalPoolObject;
import Atom.Struct.UnstableFunction;
import com.google.gson.Gson;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class Utility {
    
    public static String toHumanReadableSize(double size) {
        if (size < 1024){
            return size + " B";
        }
        if (size < 1024 * 1024){
            return (size / 1024f) + " KB";
        }
        if (size < 1024 * 1024 * 1024){
            return (size / 1024 / 1024f) + " MB";
        }
        if (size < 1024L * 1024 * 1024 * 1024){
            return (size / 1024 / 1024 / 1024f) + " GB";
        }
        return (size / 1024 / 1024 / 1024 / 1024f) + " TB";
    }
    
    public static boolean openURL(URL url) {
        return openURL(url.toString());
    }

    public static boolean openURL(String url) {
        try {
            if (OS.isWindows) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (OS.isMac) {
                Runtime.getRuntime().exec("open " + url);
            } else if (OS.isLinux) {
                Runtime.getRuntime().exec("xdg-open " + url);
            } else {
                Runtime.getRuntime().exec(url);//work everywhere ?
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Deprecated
    public static boolean isRepeatingPattern(String s) {
        return isRepeatingPattern(s, 5);
    }

    @SafeVarargs
    public static <T> T[] merge(T[]... arrays) {
        int finalLength = 0;
        for (T[] array : arrays) {
            finalLength += array.length;
        }
        
        T[] dest = null;
        int destPos = 0;
        
        for (T[] array : arrays) {
            if (dest == null){
                dest = Arrays.copyOf(array, finalLength);
                destPos = array.length;
            }else{
                System.arraycopy(array, 0, dest, destPos, array.length);
                destPos += array.length;
            }
        }
        return dest;
    }
    
    public static int[] flattenMatrix(int[][] arr) {
        int count = 0;
        for (int[] i : arr) {
            count += i.length;
        }
        int[] vec = new int[count];
        int i = 0;
        for (int[] ts : arr) {
            for (int e : ts) {
                vec[i++] = e;
            }
        }
        return vec;
    }
    
    public static <T> T[] flattenMatrix(T[][] arr) {
        ArrayList<T> list = new ArrayList<>();
        for (T[] ts : arr) {
            list.addAll(Arrays.asList(ts));
        }
        if (list.size() == 0) return null;
        return list.toArray(arr[0]);
    }
    
    public static String repeatThisString(String s, int howMany) {
        StringBuilder sb = new StringBuilder(howMany);
        for (int i = 0; i < howMany; i++) {
            sb.append(s);
        }
        return sb.toString();
    }
    
    public static String classArrayToStringArray(Iterable<Class<?>> classes, boolean canonical) {
        StringBuilder sb = FunctionalPoolObject.StringBuilder.obtain();
        sb.append("[");
        for (Class<?> aClass : classes) {
            if (canonical) sb.append(aClass.getCanonicalName());
            else sb.append(aClass.getSimpleName());
            sb.append(", ");
        }
    
        sb.append("]");
        String s = sb.toString();
        FunctionalPoolObject.StringBuilder.free(sb);
        return s;
    }
    
    public static void getInput(Map<String, Consumer<String>> map) {
        getInput(map, System.out, new BufferedReader(new InputStreamReader(System.in)));
    }
    
    public static void getInput(Map<String, Consumer<String>> map, PrintStream out, BufferedReader br) {
        for (Map.Entry<String, Consumer<String>> s : map.entrySet()) {
            boolean success = false;
            while (!success) try {
                out.println(s.getKey() + ": ");
                s.getValue().accept(br.readLine());
                success = true;
            } catch (IOException | RuntimeException e) {
                out.println(e);
                out.println("Try again");
            }
        }
    }

    public static String capitalizeTitle(String s) {
        return capitalizeTitle(s, false);
    }

    public static String capitalizeTitle(String s, boolean enforce) {
        if (s.length() == 0) return s;
        String[] words = s.split(" ");
        StringBuilder sb = FunctionalPoolObject.StringBuilder.obtain();

        for (String word : words) {
            sb.append(enforce ? capitalizeEnforce(word) : capitalizeFirstLetter(word)).append(" ");
        }
        String result = sb.toString();
        FunctionalPoolObject.StringBuilder.free(sb);
        return result.substring(0, result.length() - 1);
    }
    
    /**
     * Input: "Hello World"
     * <p>
     * Output: "Hello world"
     *
     * @param "string"
     * @return "String"
     */
    public static String capitalizeEnforce(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
    
    public static <T> T getOrDefault(Map<?, T> map, Object key, T def) {
        T t = map.get(key);
        if (t == null) return def;
        return t;
    }
    
    @Deprecated
    public static boolean isRepeatingPattern(String s, int maxRepeatingChar) {
        int parsed = s.replaceAll("(.+?)\\1+", "$1").length();
        parsed = parsed + maxRepeatingChar;
        int unparsed = s.length();
        return unparsed > parsed;
    }
    
    public static String getDownload(String url, String groupId, String artifactId, String version) {
        if (url.endsWith("/")) url = url.substring(0, url.length() - 1);
        return String.format("%s/%s/%s/%s/%s-%s.jar", url, groupId.replace('.', '/'), artifactId, version, artifactId, version);
    }
    
    @Deprecated
    public static String getExtension(File f) {
        String extension = "";
        int i = f.getName().lastIndexOf('.');
        if (i > 0){
            extension = f.getName().substring(i + 1);
        }
        return extension;
    }
    
    public static BufferedReader bufferedReaderForStdIN;//DO NOT CLOSE, unless you want to replace it
    
    public static String input(String question) {
        return input(question, null);
    }
    
    public static String input(String question, String def) {
        return input(question, def, (s) -> s);
    }
    
    public static long inputLong(String question) {
        return inputLong(question, 0L);
    }
    
    public static long inputLong(String question, Long def) {
        return input(question, def, Long::parseLong);
    }
    
    public static int inputInt(String question) {
        return inputInt(question, 0);
    }
    
    public static int inputInt(String question, int def) {
        return input(question, def, Integer::parseInt);
    }
    
    public static double inputDouble(String question) {
        return inputDouble(question, 0.0);
    }
    
    public static double inputDouble(String question, double def) {
        return input(question, def, Double::parseDouble);
    }
    
    public static boolean inputBoolean(String question) {
        return inputBoolean(question, false);
    }
    
    public static boolean inputBoolean(String question, boolean def) {
        return input(question, def, Boolean::parseBoolean);
    }
    
    public static String[] inputS(String question) {
        return inputS(question, new String[]{});
    }
    
    public static String[] inputS(String question, String... def) {
        return input(question, def, ",");
    }
    
    public static String[] input(String question, String[] def, String split) {
        System.out.println(split + " separated");
        return input(question, def, (s) -> s.split(split));
    }
    
    public static <T> T input(String question, T def, UnstableFunction<String, T> parser) {
        try {
            return input(question, def, parser, true);
        }catch(Exception e){
            throw new ShouldNotHappenedException(e);
        }
    }
    
    public static <T> String arrayToString(T def) {
        String defText = null;
        if (def instanceof String[]){
            defText = Arrays.toString((String[]) def);
        }else if (def instanceof int[]){
            defText = Arrays.toString((int[]) def);
        }else if (def instanceof long[]){
            defText = Arrays.toString((long[]) def);
        }else if (def instanceof double[]){
            defText = Arrays.toString((double[]) def);
        }else if (def instanceof float[]){
            defText = Arrays.toString((float[]) def);
        }else if (def instanceof boolean[]){
            defText = Arrays.toString((boolean[]) def);
        }else if (def instanceof char[]){
            defText = Arrays.toString((char[]) def);
        }else if (def instanceof byte[]){
            defText = Arrays.toString((byte[]) def);
        }else if (def instanceof short[]){
            defText = Arrays.toString((short[]) def);
        }else if (def instanceof Object[]){
            defText = Arrays.toString((Object[]) def);
        }
        return defText;
    }
    
    public static <T> T input(String question, T def, UnstableFunction<String, T> parser, boolean loop) throws Exception {
        if (bufferedReaderForStdIN == null)
            bufferedReaderForStdIN = new BufferedReader(new InputStreamReader(System.in));
        String defText = "";
        if (def != null){
            if (def.getClass().isArray()){
                defText = arrayToString(def);
            }else{
                defText = def.toString();
                defText = " [" + defText + "]";
            }
            
        }
        System.out.print(question + " " + defText + ": ");
        while (true) {
            try {
                String s = bufferedReaderForStdIN.readLine();
                if (s.isEmpty() && def != null) return def;
                return parser.apply(s);
            }catch(Exception e){
                if (!loop) throw e;
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    
    //get cpu usage of current process from java api
    //android support ?
    public double getAverageCpuUsage() {
        try {
            return ManagementFactory.getOperatingSystemMXBean()
                    .getSystemLoadAverage() / ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
        }catch(Throwable t){
            return 0;//class don't exists
        }
    }
    
    public static String getDate() {
        
        Calendar c = Calendar.getInstance();
        return String.format(Locale.UK, "%tl:%tM:%tS %tp %tB %te, %tY", c, c, c, c, c, c, c).toString();
        
    }
    
    public static String getDate(long milis) {
    
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(milis);
        return String.format(Locale.UK, "%tl:%tM:%tS %tp %tB %te, %tY", c, c, c, c, c, c, c).toString();
        
    }
    
    
    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
    
    public static <T> T[] toArray(List<T> arr) {
        return (T[]) arr.toArray();
    }
    
    //T t3 = new T[t1.length + t2.length];
    public static <T> T[] joinArray(T[] t1, T[] t2, T[] t3) {
        int m = 0;
        for (int i = 0; i < t1.length; i++) {
            m = i;
            t3[m] = t1[m];
        }
        for (int i = m; i < t2.length; i++) {
            t3[m] = t2[i];
        }
        return t3;
    }
    
    public static String[] getArray(List<String> arrayList) {
        String[] arr = new String[arrayList.size()];
        arr = arrayList.toArray(arr);
        return arr;
    }
    
    public static String shrinkString(String s) {
        return new LinkedHashSet<>(Arrays.asList(s.split("-"))).toString().replaceAll("(^\\[|]$)", "").replace(", ", "-");
    }
    
    public static <T> T getFromJson(String json, Class<T> tClass) {
        return new Gson().fromJson(json, tClass);
    }
    
    public static String toJson(Object clazz) {
        return new Gson().toJson(clazz);
    }
    
    public static String removal(String s, char openChar, char closeChar) {
        StringBuilder sb = FunctionalPoolObject.StringBuilder.obtain();
        boolean delete = false;
        for (char i : s.toCharArray()) {
            if (i == openChar){
                delete = true;
            }else if (i == closeChar){
                delete = false;
            }else{
                if (delete) continue;
                sb.append(i);
            }
        }
        String ss = sb.toString();
        FunctionalPoolObject.StringBuilder.free(sb);
        return ss;
    }
    
    public static String shrinkChar(String s) {
        char[] chars = s.toCharArray();
        Set<Character> cr = new LinkedHashSet<>();
        for (char c : chars) cr.add(c);
        StringBuilder sb = FunctionalPoolObject.StringBuilder.obtain();
        for (Character chr : cr) sb.append(chr);
        String ss = sb.toString();
        FunctionalPoolObject.StringBuilder.free(sb);
        return ss;
    }
    
    public static boolean containIntOnly(String s) {
        return (s.matches("[0-9]+"));
    }
    
    //what the fuck, bloat method
    @Deprecated
    public static String removeFirstChar(String s, int howMany) {
        return s.substring(howMany);
    }
    
    @Deprecated
    public static String removeFirstChar(String s) {
        return s.substring(1);
    }
    
    @Deprecated
    public static String[] sliceString(String s, String fix) {
        return s.split(fix, -1);
    }
    
    public static int[] sliceInt(String s, String fix) {
        String[] strArray = s.split(fix);
        int[] intArray = new int[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
            intArray[i] = Integer.parseInt(strArray[i]);
        }
        return intArray;
    }
    
    public static String shuffle(String input) {
        List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while (characters.size() != 0) {
            int randPicker = (int) (Math.random() * characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }
    
    public static String capitalizeFirstLetter(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    
    public static String lowerFirstLetter(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }
    
    public static HashMap<String, String> ArgParser(String[] arg, String prefix) {
        
        HashMap<String, String> args = new HashMap<>();
        
        if (arg.length % 2 != 0) throw new IllegalArgumentException("Not Enough Argument" + Arrays.toString(arg));
        boolean i = true;
        String k = "";
        for (String d : arg) {
            if (d.startsWith(prefix)){
                k = d.replaceFirst(prefix, "");
            }else{
                args.put(k, d);
            }
            i = !i;
        }
        
        return args;
    }
    
    public static int getJavaMajorVersion() {
        String version;
        version = System.getProperty("java.version");
        if (version == null || version.isEmpty()) version = System.getProperty("java.runtime.version");
        if (version.startsWith("1.")){
            version = version.substring(2, 3);
        }else{
            int dot = version.indexOf(".");
            if (dot != -1){
                version = version.substring(0, dot);
            }
        }
        return Integer.parseInt(version);
        
    }
    
    public static String joiner(List<?> datas, Object prefix) {
        return joiner(datas.toArray(new Object[0]), prefix);
    }
    
    public static String joiner(Object[] datas, Object prefix) {
        StringBuilder data = FunctionalPoolObject.StringBuilder.obtain();
        for (int i = 0; i < datas.length; i++) {
            data.append(datas[i]);
            if (i != datas.length - 1) data.append(prefix);
        }
    
        String s = data.toString();
        FunctionalPoolObject.StringBuilder.free(data);
        return s;
    }
    
    public static void convertThreadToInputListener(Consumer<String> listener) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (!Thread.interrupted()) {
            listener.accept(br.readLine());
        }
    }
}
