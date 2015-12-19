/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.easyway.ueditor2.system;


import java.net.URL;

public class ClassLocation {

	private String className;

	private URL url;

	private ClassLoader classLoader;

	public ClassLocation(ClassLoader classLoader, String className, URL url) {
		this.className = className;
		this.url = url;
		this.classLoader = classLoader;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}


	@Override
	public int hashCode() {
		return (className == null) ? 0 : className.hashCode();
	}

	public boolean equals(ClassLocation classLocation) {
		if (classLocation == null)
			return false;

		return (className.equals(classLocation.className));
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ClassLocation)
			return equals((ClassLocation) o);
		else
			return false;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}


}


