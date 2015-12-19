/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.rightPanels;

/**
 *
 * @author RemalKoil
 */
public class Property {

    Object propObj;
    String propName;
    boolean isReadOnly;

    public Property(Object propObj, String propName, boolean isReadOnly) {
        this.propObj = propObj;
        this.propName = propName;
        this.isReadOnly = isReadOnly;
    }

    public String getPropName() {
        return propName;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public Object getPropObj() {
        return propObj;
    }

    @Override
    public String toString() {
        return propName;
    }
}
