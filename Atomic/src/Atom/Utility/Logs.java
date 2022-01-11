package Atom.Utility;

import Atom.Class.StringFormatter;

import java.util.Collections;
import java.util.Set;

public class Logs {
    public static final Set<Log> logs = Collections.synchronizedSet(new java.util.HashSet<Log>());
    public static StringFormatter formatterInfo, formatterError, formatterDebug, formatterWarn = s -> s;

    public static void output(Object o) {
        for (Log log : logs) {
            log.output(o);
        }
    }

    public static void info(Object o) {
        output(formatterInfo.format(String.valueOf(o)));
    }

    public static void error(Object o) {
        output(formatterError.format(String.valueOf(o)));
    }

    public static void debug(Object o) {
        output(formatterDebug.format(String.valueOf(o)));
    }

    public static void warn(Object o) {
        output(formatterWarn.format(String.valueOf(o)));
    }

    public static void addLog(Log log) {
        logs.add(log);
    }

    public static void removeLog(Log log) {
        logs.remove(log);
    }
}
