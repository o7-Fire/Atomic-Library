package Atom.Classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

//that a lot of exception
public class StaticClassloader {
	private static final URLClassLoader systemClassLoader = new JarClassLoader(ClassLoader.getSystemClassLoader());
	
	//totally legit interface for modified and non modified URLClassloader
	public static Package[] getPackages() throws IllegalAccessException {
		return ClassLoader.getSystemClassLoader().getDefinedPackages();
	}
	
	public static Package[] getPackages(URLClassLoader loader) {
		return loader.getDefinedPackages();
	}
	
	public static void addURL(URL url) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		addURL(getURLSystemCl(), url);
	}
	
	public static boolean isAlreadyLoaded(File jar) throws IllegalAccessException, MalformedURLException {
		return isAlreadyLoaded(getURLSystemCl(), jar.toURI().toURL());
	}
	
	public static boolean isAlreadyLoaded(URL url) throws IllegalAccessException {
		return isAlreadyLoaded(getURLSystemCl(), url);
	}
	
	public static boolean isAlreadyLoaded(URLClassLoader loader, URL url) {
		for (java.net.URL it : loader.getURLs()) {
			if (it.equals(url)) {
				return true;
			}
		}
		return false;
	}
	
	public static void loadJar(File jar) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, IOException {
		loadJar(getURLSystemCl(), jar);
	}
	
	//index load class inside jar for classloader to search
	public static void loadJar(URLClassLoader loader, File file) throws UnsupportedClassVersionError, IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
		if (!StaticClassloader.isAlreadyLoaded(loader, file.toURI().toURL())) addURL(loader, file);
		List<String> classNames = new ArrayList<>();
		ZipInputStream zip = new ZipInputStream(new FileInputStream(file.getAbsolutePath()));
		for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
			if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
				// This ZipEntry represents a class. Now, what class does it represent?
				String className = entry.getName().replace('/', '.'); // including ".class"
				classNames.add(className.substring(0, className.length() - ".class".length()));
			}
		}
		for (String s : classNames) {
			if (s.equals("module-info")) continue;
			loader.loadClass(s);
		}
	}
	
	public static void addURL(File jar) throws IllegalAccessException, MalformedURLException, NoSuchMethodException, InvocationTargetException {
		addURL(getURLSystemCl(), jar);
	}
	
	public static void addURL(URLClassLoader loader, File jar) throws MalformedURLException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		addURL(loader, jar.toURI().toURL());
	}
	
	public static void addURL(URLClassLoader loader, URL url) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		if (isAlreadyLoaded(loader, url)) return;
		if (loader instanceof AtomClassLoader) {
			((AtomClassLoader) loader).addURL(url);
		}else if (loader instanceof JarClassLoader) {
			((JarClassLoader) loader).addURL(url);
		}else {
			throw new IllegalAccessException("Cannot add URL to " + loader.getClass().getName() + "; use AtomClassLoader");
		}
	}
	
	public static URLClassLoader getURLSystemCl() throws IllegalAccessException {
		return systemClassLoader;
	}
}
