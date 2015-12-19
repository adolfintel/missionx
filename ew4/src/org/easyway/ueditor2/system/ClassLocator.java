/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.system;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassLocator {

    private String[] packageNames;
    private ClassLoader classLoader;
    private String packageName;
    private List<ClassLocation> classLocations = new LinkedList<ClassLocation>();

    public ClassLocator(String... packageNames) throws ClassNotFoundException {
        this(Thread.currentThread().getContextClassLoader(), packageNames);
    }

    public ClassLocator(ClassLoader classLoader, String... packageNames) throws ClassNotFoundException {
        this.classLoader = classLoader;
        this.packageNames = packageNames;

        if (classLoader == null) {
            throw new ClassNotFoundException("Can't get class loader.");
        }

    }

    public List<ClassLocation> getAllClassLocations() throws ClassNotFoundException, IOException {
        synchronized (this) {
            classLocations.clear();


            for (String packageName : packageNames) {
                this.packageName = packageName;

                String path = packageName.replace('.', '/');
                Enumeration<URL> resources = classLoader.getResources(path);
                if (resources == null || !resources.hasMoreElements()) {
                    throw new ClassNotFoundException("No resource for " + path);
                }

                while (resources.hasMoreElements()) {
                    URL resource = resources.nextElement();
                    if (resource.getProtocol().equalsIgnoreCase("FILE")) {
                        loadDirectory(resource);
                    } else if (resource.getProtocol().equalsIgnoreCase("JAR")) {
                        loadJar(resource);
                    } else {
                        throw new ClassNotFoundException("Unknown protocol on class resource: " + resource.toExternalForm());
                    }
                }
            }


            return classLocations;
        }
    }

    private void loadJar(URL resource) throws IOException {
        JarURLConnection conn = (JarURLConnection) resource.openConnection();
        JarFile jarFile = conn.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        String packagePath = packageName.replace('.', '/');

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if ((entry.getName().startsWith(packagePath) || entry.getName().startsWith("WEB-INF/classes/" + packagePath)) &&
                    entry.getName().endsWith(".class")) {
                // TODO
                //URL url = new URL("jar:" + new URL("file", null, JavaUtility.slashify(jarFile.getName(), false)).toExternalForm() + "!/" + entry.getName());
                URL url = new URL("jar:" + new URL("file", null, jarFile.getName()).toExternalForm() + "!/" + entry.getName());

                String className = entry.getName();
                if (className.startsWith("/")) {
                    className = className.substring(1);
                }
                className = className.replace('/', '.');

                className = className.substring(0, className.length() - ".class".length());

                ClassLocation classLocation = new ClassLocation(classLoader, className, url);
                addClassLocation(classLocation);
            }
        }


    }

    private void loadDirectory(URL resource) throws IOException {
        loadDirectory(packageName, resource.getFile());

    }

    private void loadDirectory(String packageName, String fullPath) throws IOException {
        File directory = new File(fullPath);
        if (!directory.isDirectory()) {
            fullPath = fullPath.replaceAll("%20", " ");
            directory = new File(fullPath);
            if (!directory.isDirectory()) {
                throw new IOException("Invalid directory " + directory.getAbsolutePath());
            }
        }

        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                loadDirectory(packageName + '.' + file.getName(), file.getAbsolutePath());
            } else if (file.getName().endsWith(".class")) {
                String simpleName = file.getName();
                simpleName = simpleName.substring(0, simpleName.length() - ".class".length());
                String className = String.format("%s.%s", packageName, simpleName);
                ClassLocation location = new ClassLocation(classLoader, className, new URL("file", null, file.getAbsolutePath()));
                addClassLocation(location);
            }
        }
    }

    private void addClassLocation(ClassLocation classLocation) throws IOException {
        if (classLocations.contains(classLocation)) {
            throw new IOException("Duplicate location found for: " + classLocation.getClassName());
        }

        classLocations.add(classLocation);
    }
}
