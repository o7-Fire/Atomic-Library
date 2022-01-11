package Atom.Utility;

import java.io.PrintStream;
import java.io.PrintWriter;

public abstract class Log {
    protected String[] sLevel = new String[]{"[Debug]", "[Info]", "[Warn]", "[Error]"};
    public int level = 3;

    public static Log def() {
        return new PrintWriterLog();
    }

    protected void output(Object raw, int level) {
        if (this.level > level) return;

        output(sLevel[level] + " " + raw);
    }

    protected void output(String tag, Object raw, int level) {
        if (this.level > level) return;
    
        output(sLevel[level] + " " + tag + ": " + raw);
    }
    
    protected void output(Object raw) {

    }
    
    public void debug(Object s) {
        output(s, 0);
    }
    
    public void info(Object s) {
        output(s, 1);
    }
    
    public void warn(Object s) {
        output(s, 2);
    }
    
    public void err(Object s) {
        output(s, 3);
    }
    
    public void debug(String tag, Object s) {
        output(tag, s, 0);
    }
    
    public void info(String tag, Object s) {
        output(tag, s, 1);
    }

    public void warn(String tag, Object s) {
        output(tag, s, 2);
    }

    public void err(String tag, Object s) {
        output(tag, s, 3);
    }


    public static class PrintWriterLog extends Log {
        PrintWriter writer;

        public PrintWriterLog(PrintWriter writer) {
            this.writer = writer;
        }

        public PrintWriterLog(PrintStream stream) {
            this(new PrintWriter(stream));
        }

        public PrintWriterLog() {
            this(System.out);
        }

        public void output(Object raw) {
            writer.println(raw);
        }
    }
}
