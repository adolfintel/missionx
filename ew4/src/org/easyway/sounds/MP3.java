/* @Author Do$$e
 *
 */
package org.easyway.sounds;

import java.io.BufferedInputStream;

import java.io.InputStream;
import javazoom.jl.player.Player;

public class MP3 {

    private String filename;
    private Player player;

    // constructor that takes the name of an MP3 file
    public MP3(String filename) {

        // --- prepare the path ---
        {
            if (filename.startsWith("/")) {
                filename = filename.substring(1);
            }
            int index;
            // path = path.replaceAll("\\", "/");
            while ((index = filename.indexOf("\\")) != -1) {
                filename = filename.substring(0, index) + '/' + filename.substring(index + 1);
            }
            this.filename = filename;
            // openClipStream();
        }
        //this.filename = filename;
    }

    public void close() {
        if (player != null) {
            player.close();
        }
    }

    // play the MP3 file to the sound card
    public void play() {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResource(filename).openStream();
            //FileInputStream fis     = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(is);
            player = new Player(bis);
        } catch (Exception e) {
            System.out.println("can't play mp3 :" + filename);
            System.out.println(e);
        }
        // run in new thread to play in background
        new Thread() {

            public void run() {
                try {
                    player.play();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }.start();

    }
}

