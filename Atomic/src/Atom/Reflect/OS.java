package Atom.Reflect;

public class OS {
    public static boolean isWindows = getProperty("os.name").contains("Windows");
    public static boolean isLinux = getProperty("os.name").contains("Linux");
    public static boolean isMac = getProperty("os.name").contains("Mac");
    public static boolean isIos = false;
    public static boolean isAndroid = false;
    public static boolean isARM = getProperty("os.arch").startsWith("arm") || getProperty("os.arch").startsWith("aarch64");
    public static boolean is64Bit = getProperty("os.arch").contains("64") || getProperty("os.arch").startsWith("armv8");
    
    static {
        if (getProperty("java.runtime.name").contains("Android Runtime") || getProperty("java.vm.vendor").contains("The Android Project") || getProperty("java.vendor").contains("The Android Project")) {
            isAndroid = true;
            isWindows = false;
            isLinux = false;
            isMac = false;
            is64Bit = false;
        }
    
        if (getProperty("moe.platform.name").equals("iOS") || !isAndroid && !isWindows && !isLinux && !isMac){
            isIos = true;
            isAndroid = false;
            isWindows = false;
            isLinux = false;
            isMac = false;
            is64Bit = false;
        }
    
    }
    
    public static String getPlatform() {
        String jvmName = System.getProperty("java.vm.name", "").toLowerCase();
        String osName = System.getProperty("os.name", "").toLowerCase();
        String osArch = System.getProperty("os.arch", "").toLowerCase();
        String abiType = System.getProperty("sun.arch.abi", "").toLowerCase();
        String libPath = System.getProperty("sun.boot.library.path", "").toLowerCase();
        if (jvmName.startsWith("dalvik") && osName.startsWith("linux")){
            osName = "android";
        }else if (jvmName.startsWith("robovm") && osName.startsWith("darwin")){
            osName = "ios";
            osArch = "arm";
        }else if (osName.startsWith("mac os x") || osName.startsWith("darwin")){
            osName = "macosx";
        }else{
            int spaceIndex = osName.indexOf(' ');
            if (spaceIndex > 0){
                osName = osName.substring(0, spaceIndex);
            }
        }
        if (osArch.equals("i386") || osArch.equals("i486") || osArch.equals("i586") || osArch.equals("i686")){
            osArch = "x86";
        }else if (osArch.equals("amd64") || osArch.equals("x86-64") || osArch.equals("x64")){
            osArch = "x86_64";
        }else if (osArch.startsWith("aarch64") || osArch.startsWith("armv8") || osArch.startsWith("arm64")){
            osArch = "arm64";
        }else if ((osArch.startsWith("arm")) && ((abiType.equals("gnueabihf")) || (libPath.contains("openjdk-armhf")))){
            osArch = "armhf";
        }else if (osArch.startsWith("arm")){
            osArch = "arm";
        }
        return osName + "-" + osArch;
    }
    
    public static String getProperty(String s) {
        return System.getProperty(s, "null");
    }
    
}
