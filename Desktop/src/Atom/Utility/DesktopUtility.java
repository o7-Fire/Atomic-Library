package Atom.Utility;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class DesktopUtility {
    public static ScriptEngineManager manager;
    public static ThreadLocal<ScriptEngine> jsEngine;

    public static void valid() {
        if (manager == null) {
            manager = new ScriptEngineManager();
        }
        if (manager.getEngineFactories().isEmpty()) {
            throw new RuntimeException("No engine available");
        }
        if (manager.getEngineByName("JavaScript") != null && jsEngine == null) {
            jsEngine = ThreadLocal.withInitial(() -> manager.getEngineByName("JavaScript"));
        }
    }

    public static void main(String[] args) {
        System.out.println("Available engines: " + new ScriptEngineManager().getEngineFactories());
    }

    public static Object eval(String js) throws ScriptException {
        valid();
        if (jsEngine == null) {
            throw new IllegalStateException("No engine available");
        }
        ScriptEngine engine = jsEngine.get();
        return engine.eval(js);
    }
}
