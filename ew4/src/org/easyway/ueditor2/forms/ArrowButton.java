/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.forms;

import javax.swing.JToggleButton;
import org.easyway.system.StaticRef;

/**
 *
 * @author Daniele
 */
public class ArrowButton extends JToggleButton {

    Thread t;
    boolean can;

    private void moveCamera(final double speedx, final double speedy) {
        if (t == null) {
            can = true;
            (t = new Thread() {

                @Override
                public void run() {
                    while (can) {
                        StaticRef.getCamera().move(speedx, speedy);
                        try {
                            Thread.sleep(10);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Thread.yield();
                    }
                    t = null;
                }
            }).start();
        } else {
            can = false;
        }
    }
    public double xspeed,  yspeed;

    public double getXspeed() {
        return xspeed;
    }

    public void setXspeed(double xspeed) {
        this.xspeed = xspeed;
    }

    public double getYspeed() {
        return yspeed;
    }

    public void setYspeed(double yspeed) {
        this.yspeed = yspeed;
    }

    public ArrowButton() {

        addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent e) {
                moveCamera(xspeed, yspeed);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                setSelected(true);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                setSelected(false);
            }
        });

    }
}
