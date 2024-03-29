package Atom;


import Atom.Encoding.Encoder;
import Atom.File.RepoInternal;
import Atom.Utility.Digest;

import java.io.File;
import java.nio.charset.Charset;

import static Atom.Reflect.Reflect.getCurrentJar;

@Deprecated
public class Manifest {
    //remove this
    @Deprecated
    public static String platform = "Atomic";
    public static RepoInternal internalRepo = new RepoInternal();
    public static File currentJar = null, currentFolder = null;
    private static String sha256Hash;
    public static Charset charset = Charset.forName("UTF-8");

    static {
        try {
            currentJar = getCurrentJar();
            currentFolder = currentJar.getParentFile();
        } catch (Throwable ignored) {
        }
        try {
            internalRepo.loadClasspath();
            internalRepo.addRepo(getCurrentJar().toURI().toURL());
        }catch(Throwable ignored){ }
        try {
            sha256Hash = Encoder.getString(Digest.sha256(getCurrentJar()));
        }catch(Throwable aa){
            sha256Hash = aa.toString();
        }
        tryLoadExtension();
    }
    
    
    public static boolean isWindows() {
        return System.getProperty("os.name").toUpperCase().contains("WIN");
    }
    
    public static void tryLoadExtension() {
        try {
            Class.forName("Atom.DesktopManifest");
        }catch(Throwable ignored){
        }
    }
    
    public static Object javac() {
        return javax.tools.ToolProvider.getSystemJavaCompiler();
    }
    
    public static boolean javacExists() {
        try {
            return Manifest.class.getClassLoader().loadClass("javax.tools.ToolProvider").getMethod("getSystemJavaCompiler").invoke(null) != null;//???
        }catch(Throwable t){
            return false;
        }
    }
    
    
    public static String getHash() {
        return sha256Hash;
    }
    
}