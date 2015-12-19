/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.logic;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

/**
 *
 * @author Do$$e
 */
public class GamePad {

    private static Controller gp = null;
    public static int NUM_BUTTONS = 31;
    private static int[] buttonsIdx = new int[NUM_BUTTONS];
    private static boolean initialised = false;

    public static boolean initialise() {
        Controller[] c = ControllerEnvironment.getDefaultEnvironment().getControllers();
        for (int i = 0; i < c.length; i++) {
            System.out.println("Input device: " + c[i].getName() + " " + c[i].getType().toString());
            if (c[i].getType().toString().equalsIgnoreCase("Stick") || c[i].getType().toString().equalsIgnoreCase("Gamepad")) {
                gp = c[i];
            }
        }
        if (gp == null) {
            initialised = false;
            return false;
        } else {
            int bn = 0;
            Component[] comps = gp.getComponents();
            for (int i = 0; i < comps.length; i++) {
                if (isButton(comps[i])) {
                    buttonsIdx[bn] = i;
                    bn++;
                }
            }
            //System.out.println("" + bn + " buttons");
            while (bn < NUM_BUTTONS) {
                buttonsIdx[bn] = -1;
                bn++;
            }
            initialised = true;
            return true;
        }
    }

    public static boolean isInitialised() {
        return initialised;
    }

    public static Controller getController() {
        return gp;
    }

    public static Component[] getComponents() {
        if(gp!=null) return gp.getComponents(); else return null;
    }

    public static float getXAxis() {
        if(gp==null) return 0;
        pollDevice();
        for (int i = 0; i < gp.getComponents().length; i++) {
            if (gp.getComponents()[i].getIdentifier() == Component.Identifier.Axis.X) {
                return gp.getComponents()[i].getPollData();
            }
        }
        return 0;
    }

    public static float getYAxis() {
        if(gp==null) return 0;
        pollDevice();
        for (int i = 0; i < gp.getComponents().length; i++) {
            if (gp.getComponents()[i].getIdentifier() == Component.Identifier.Axis.Y) {
                return gp.getComponents()[i].getPollData();
            }
        }
        return 0;
    }

    public static float getRZAxis() {
        if(gp==null) return 0;
        pollDevice();
        for (int i = 0; i < gp.getComponents().length; i++) {
            if (gp.getComponents()[i].getIdentifier() == Component.Identifier.Axis.RZ) {
                return gp.getComponents()[i].getPollData();
            }
        }
        return 0;
    }

    public static float getZAxis() {
        if(gp==null) return 0;
        pollDevice();
        for (int i = 0; i < gp.getComponents().length; i++) {
            if (gp.getComponents()[i].getIdentifier() == Component.Identifier.Axis.Z) {
                return gp.getComponents()[i].getPollData();
            }
        }
        return 0;
    }

    public static boolean isButtonPressed(int n) {
        if(gp==null) return false;
        pollDevice();
        Component[] comps = gp.getComponents();
        if ((n < 0) || (n >= NUM_BUTTONS)) {
            return false;
        }
        int index = buttonsIdx[n];
        if (index < 0 || index > 31) {
            return false;
        }
        float value = comps[buttonsIdx[n]].getPollData();
        return ((value == 0.0f) ? false : true);
    }

    private static boolean isButton(Component c) {
        if(gp==null) return false;
        if (!c.isAnalog() && !c.isRelative()) {
            String className = c.getIdentifier().getClass().getName();
            System.out.println(className);
            if (className.endsWith("Button")) {
                return true;
            }
        }
        return false;
    }

    private static void pollDevice() {
        if(gp==null) return;
        if (!gp.poll()) {
            System.out.println("Gamepad error. Exiting program");
            System.exit(0);
        }
    }



    public static boolean isGamePadPlugged() {
        boolean b = false;
        Controller[] c = ControllerEnvironment.getDefaultEnvironment().getControllers();
        for (int i = 0; i < c.length; i++) {
            System.out.println("Input device: " + c[i].getName() + " " + c[i].getType().toString());
            if (c[i].getType().toString().equalsIgnoreCase("Stick") || c[i].getType().toString().equalsIgnoreCase("Gamepad")) {
                b = true;
            }
        }
        return b;
    }
}
