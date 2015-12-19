/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.system;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniele
 */
public class PackageUtility {

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    /*public static Class[] getClasses(String packageName)
    throws ClassNotFoundException, IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    assert classLoader != null;
    String path = packageName.replace('.', '/');
    Enumeration<URL> resources = classLoader.getResources(path);
    List<File> dirs = new ArrayList<File>();
    while (resources.hasMoreElements()) {
    URL resource = resources.nextElement();
    dirs.add(new File(resource.getFile()));
    }
    ArrayList<Class> classes = new ArrayList<Class>();
    System.out.println("dir size: "+dirs.size());
    for (File directory : dirs) {
    classes.addAll(findClasses(directory, packageName));
    }
    return classes.toArray(new Class[classes.size()]);
    }*/
    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    /*private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
    List<Class> classes = new ArrayList<Class>();
    if (!directory.exists()) {
    System.out.println("don't exists");
    return classes;
    }
    File[] files = directory.listFiles();
    for (File file : files) {
    if (file.isDirectory()) {
    assert !file.getName().contains(".");
    classes.addAll(findClasses(file, packageName + "." + file.getName()));
    } else if (file.getName().endsWith(".class")) {
    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
    }
    }
    return classes;*/
    public static Class[] getClasses(String pckgname)
            throws ClassNotFoundException {
        ArrayList<Class> classes = new ArrayList<Class>();
        // Get a File object for the package
        File directory = null;
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }

            String path = pckgname.replace('.', '/');
            URL resource = cld.getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("No resource for " + path);
            }

            //directory = new File(resource.getFile());
            directory = new File(URLDecoder.decode(resource.getPath(),"UTF-8"));

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(PackageUtility.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(pckgname + " (" + directory + ") does not appear to be a valid package");
        }

        if (directory.exists()) {
            // Get the list of the files contained in the package
            String[] files = directory.list();
            for (int i = 0; i <
                    files.length; i++) {
                // we are only interested in .class files
                if (files[i].endsWith(".class")) {
                    // removes the .class extension
                    classes.add(Class.forName(pckgname + '.' + files[i].substring(0, files[i].length() - 6)));
                }

            }
        } else {
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package");
        }

        Class[] classesA = new Class[classes.size()];
        classes.toArray(classesA);
        return classesA;
    }
}
