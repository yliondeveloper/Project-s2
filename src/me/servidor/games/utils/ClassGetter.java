package me.servidor.games.utils;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.plugin.java.JavaPlugin;

public class ClassGetter {

	public static ArrayList<Class<?>> getClassesForPackage(JavaPlugin plugin, String packageName) {
		
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		CodeSource src = plugin.getClass().getProtectionDomain().getCodeSource();
		
		if (src != null) {
			src.getLocation().getPath();
			processJarfile(src.getLocation(), packageName, classes);
		}
		
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Class<?>> classi = new ArrayList<Class<?>>();
		
		for (Class<?> classy : classes) {
			names.add(classy.getSimpleName());
			classi.add(classy);
		}
		
		classes.clear();
		
		Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
		
		for (String s : names) {
			for (Class<?> classy : classi) {
				if (classy.getSimpleName().equals(s)) {
					classes.add(classy);
					break;
				}
			}
		}
		
		return classes;
	}

	private static Class<?> loadClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unexpected ClassNotFoundException loading class '" + className + "'");
		}
	}

	private static void processJarfile(URL resource, String pkgname, ArrayList<Class<?>> classes) {
		String relPath = pkgname.replace('.', '/'), resPath = resource.getPath().replace("%20", " "), jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
		JarFile jarFile;
		
		try {
			jarFile = new JarFile(jarPath);
		} catch (IOException e) {
			throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
		}
		
		Enumeration<JarEntry> entries = jarFile.entries();
		
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String entryName = entry.getName();
			String className = null;
			
			if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length()))
				className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
			
			if (className != null)
				classes.add(loadClass(className));
		}
		
		try { jarFile.close(); } catch (IOException e) { e.printStackTrace(); }
	}
	
}