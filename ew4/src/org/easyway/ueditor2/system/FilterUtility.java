/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.system;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniele
 */
public class FilterUtility {

    public static ArrayList<Object> filter(List<Object> list, String name) {
        if (list == null) {
            return null;
        }
        ArrayList<Object> outList = new ArrayList<Object>(list.size());
        for (Object obj : list) {
            if (obj.toString().toUpperCase().contains(name.toUpperCase())) {
                outList.add(obj);
            }
        }
        return outList;
    }

    public static <T> ArrayList<T> filter(List<Object> list, Class<T> clazz) {
        if (list == null) {
            return null;
        }
        ArrayList<T> outList = new ArrayList<T>(list.size());
        for (Object obj : list) {
            if (isInstanceOf(obj, clazz)) {
                outList.add( (T)obj);
            }
        }
        return outList;
    }

    public static boolean isInstanceOf(Class<?> clazz, Class<?> test) {
        try {
            clazz.asSubclass(test);
            return true;
        }catch(ClassCastException e) {
            return false;
        }

    }

    public static boolean isInstanceOf(Object obj, Class<?> clazz) {
        try {
            clazz.cast(obj);
            return true;
        } catch (ClassCastException e) {
            return false;
        }
    }
}
