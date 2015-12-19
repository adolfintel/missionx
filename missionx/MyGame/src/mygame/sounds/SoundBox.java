/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.sounds;

import java.io.IOException;
import java.util.Vector;
import mygame.*;
import org.easyway.sounds.WaveCore;

/**
 *
 * @author Do$$e
 */
public class SoundBox {

    private static boolean enabled = true;
    public static boolean precaching = false;
    private static Vector<SBEntry> v = new Vector<SBEntry>();
    private static boolean initialised = false;

    public static void play(String path) {
        if (enabled && CommonVar.useSFX) {
            try {
                new MP3Sound(path);
            } catch (Exception e) {
                System.out.println("SoundBox Error!");
            }
        }
    }

    public static void playForced(String path) {
        if (CommonVar.useSFX) {
            try {
                new MP3Sound(path);
            } catch (Exception e) {
                System.out.println("SoundBox Error!");
            }
        }
    }

    public static void play(int n) {
        if (initialised) {
            if (enabled && CommonVar.useSFX) {
                try {
                    if (n == -1) { //Suono pausa
                        new MP3Sound("sfx/" + CommonVar.skin + "/pause.mp3");
                    }
                    for (SBEntry s : v) {
                        if (s.getN() == n) {
                            new MP3Sound("sfx/" + s.getPath());
                            return;
                        }
                    }
                    if (n != -1) {
                        System.out.println("Warning: sound " + n + " does not exist or the soundTable has not been loaded.");
                    }
                } catch (Exception e) {
                    System.out.println("SoundBox Error!");
                }
            } else {
                //System.out.println("You must initialise the SoundBox before using it!");
            }
        }
    }

    public static void setEnabled(boolean enabled) {
        SoundBox.enabled = enabled;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void preCache() {
        boolean oldStatus = enabled;
        enabled = true;
        precaching = true;
        for (int i = 0; i < v.size(); i++) {
            play(v.get(i).getN());
        }
        precaching = false;
        enabled = oldStatus;
    }

    public static void initSB() {
        if (initialised) {
            return;
        }
        if (CommonVar.useSFX) {
            new MP3Sound("initMP3.mp3");
        }
        Vector<Vector<String>> l = null;
        try {
            l = new CSVToolkit("sfx/soundTable.txt").getOutput();
        } catch (IOException ex) {
            System.out.println("COULDN'T INIT SOUND SYSTEM (IOException in initSB)");
            return;
        }
        for (Vector<String> s : l) {
            try {
                v.addElement(new SBEntry(Integer.parseInt(s.get(0)), s.get(1)));
            } catch (Exception e) {
                System.out.println("Warning: error while parsing soundTable! " + s.get(0) + "," + s.get(1));
            }
        }
        initialised = true;
    }

    private static class SBEntry {

        private String path;
        private int n;

        public SBEntry(int n, String path) {
            this.path = path;
            this.n = n;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

    }
}
