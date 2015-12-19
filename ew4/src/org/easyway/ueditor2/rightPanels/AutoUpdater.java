/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.rightPanels;

import org.easyway.utils.TimerJGM;

/**
 *
 * @author Daniele
 */
public class AutoUpdater extends TimerJGM {

    BeanEditor beanEditor;

    public AutoUpdater(int delay, BeanEditor beanEditor) {
        super(delay);
        this.beanEditor = beanEditor;
    }

    @Override
    public void onTick() {
        if (beanEditor != null) {
            beanEditor.updateBeanInfo();
        }
    }
}
