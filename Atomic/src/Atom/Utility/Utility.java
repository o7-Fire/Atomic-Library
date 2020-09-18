package Atom.Utility;


import com.google.gson.Gson;

import java.util.*;

public class Utility {

    public static String shrinkString(String s) {
        return new LinkedHashSet<String>(Arrays.asList(s.split("-"))).toString().replaceAll("(^\\[|\\]$)", "").replace(", ", "-");
    }

    public static <T> T getFromJson(String json, Class<T> tClass) {
        return new Gson().fromJson(json, tClass);
    }

    public static String toJson(Object clazz) {
        return new Gson().toJson(clazz);
    }

    public static String removal(String s, char openChar, char closeChar) {
        StringBuilder sb = new StringBuilder();
        boolean delete = false;
        for (char i : s.toCharArray()) {
            if (i == openChar) {
                delete = true;
            } else if (i == closeChar) {
                delete = false;
            } else {
                if (delete) continue;
                sb.append(i);
            }
        }
        return sb.toString();
    }

    public static String shrinkChar(String s) {
        char[] chars = s.toCharArray();
        Set<Character> cr = new LinkedHashSet<>();
        for (char c : chars) cr.add(c);
        StringBuilder sb = new StringBuilder();
        for (Character chr : cr) sb.append(chr);
        return sb.toString();
    }

    public static boolean containIntOnly(String s) {
        return (s.matches("[0-9]+"));
    }

    public static String removeFirstChar(String s, int howMany) {
        return s.substring(howMany);
    }

    public static String removeFirstChar(String s) {
        return s.substring(1);
    }

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

        if (arg.length % 2 != 0)
            throw new IllegalArgumentException("Not Enough Argument" + Arrays.toString(arg));
        boolean i = true;
        String k = "";
        for (String d : arg) {
            if (d.startsWith(prefix)) {
                k = d.replaceFirst(prefix, "");
            } else {
                args.put(k, d);
            }
            i = !i;
        }

        return args;
    }

    public static int getJavaMajorVersion(){
        String version;
        version = System.getProperty("java.version");
        if(version == null || version.isEmpty())
            version = System.getProperty("java.runtime.version");
        if(version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if(dot != -1) { version = version.substring(0, dot); }
        } return Integer.parseInt(version);

    }

    public static String joiner(String[] datas, String prefix) {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < datas.length; i++) {
            data.append(datas[i]);
            if (i != datas.length - 1)
                data.append(prefix);
        }
        return data.toString();
    }
}
