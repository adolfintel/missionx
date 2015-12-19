/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.xmlParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Daniele Paggi
 */
public class MultiDataCollection {

    ArrayList data = new ArrayList();

    public void addObject(Object obj) {
        if (!data.contains(obj)) {
            data.add(obj);
        } else {
            throw new RuntimeException("object already contained");
        }
    }

    public <T> boolean hasObject(Class<T> classObject) {
        if (data.size() == 0) {
            return false;
        }
        if (classObject.equals(Object.class)) {
            return true;
        }
        for (Object obj : data) {
            if (classObject.isInstance(obj)) {
                return true;
            }
        }
        return false;
    }

    public <T> T pushObject(Class<T> classObject) {
        if (data.size() == 0) {
            return null;
        }
        if (classObject.equals(Object.class)) {
            data.remove(0);
            return (T) data.get(0);
        }
        Iterator iter = data.listIterator();
        Object obj = null;
        while (iter.hasNext()) {
            obj = iter.next();
            if (classObject.isInstance(obj)) {
                iter.remove();
                return (T) obj;
            }
        }
        return null;
    }

    public <T> Collection<T> pushObjects(Class<T> classObject) {
        if (data.size() == 0) {
            return null;
        }
        if (classObject.equals(Object.class)) {
            return data;
        }
        Collection<T> collection = new ArrayList();
        Iterator iter = data.listIterator();
        Object obj = null;
        while (iter.hasNext()) {
            obj = iter.next();
            if (classObject.isInstance(obj)) {
                iter.remove();
                collection.add((T) obj);
            }
        }
        if (collection.size() > 0) {
            return collection;
        }
        return null;
    }

    public <T> T getObject(Class<T> classObject) {
        if (data.size() == 0) {
            return null;
        }
        if (classObject.equals(Object.class)) {
            data.remove(0);
            return (T) data.get(0);
        }
        Iterator iter = data.listIterator();
        Object obj = null;
        while (iter.hasNext()) {
            obj = iter.next();
            if (classObject.isInstance(obj)) {
                // iter.remove();
                return (T) obj;
            }
        }
        return null;
    }

    public <T> Collection<T> getObjects(Class<T> classObject) {
        if (data.size() == 0) {
            return null;
        }
        if (classObject.equals(Object.class)) {
            return data;
        }
        Collection<T> collection = new ArrayList();
        Iterator iter = data.listIterator();
        Object obj = null;
        while (iter.hasNext()) {
            obj = iter.next();
            if (classObject.isInstance(obj)) {
                //iter.remove();
                collection.add((T) obj);
            }
        }
        if (collection.size() > 0) {
            return collection;
        }
        return null;
    }
}
