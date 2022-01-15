package Atom.Utility;

import javax.script.ScriptException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class AdvancedMeth {
    public static void main(String[] args) {
        try {
            System.out.println(eval("1+2"));
        } catch (ScriptException e) {
            e.printStackTrace();
        }


    }


    //no
    public static String findInverseFunction(Function<Integer, Integer> f) {
        Map<Integer, Integer> map = new LinkedHashMap<>(1000);
        for (int i = 0; i < 1e3; i++) {
            map.put(i, f.apply(i));
        }
        for (int i = 0; i < 1e3; i++) {
            if (map.get(f.apply(i)) != i) {
                return "Error: " + i + " -> " + f.apply(i);
            }
        }
        return "No error";
    }

    public static int eval(String s) throws ScriptException {
        Object output;
        output = DesktopUtility.eval(s);
        if (output instanceof Integer) {
            return (Integer) output;
        } else {
            throw new ScriptException("Error: " + output.toString());
        }
    }
}
