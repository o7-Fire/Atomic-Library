package Atom.Reflect;

public final class JavaVersion {
    private static final int majorJavaVersion = Runtime.version().feature();

    private JavaVersion() {
    }

    public static int getMajorJavaVersion() {
        return majorJavaVersion;
    }

    public static boolean isJava9OrLater() {
        return majorJavaVersion >= 9;
    }
}
