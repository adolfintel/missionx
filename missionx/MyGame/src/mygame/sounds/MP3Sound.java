/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.sounds;

import org.easyway.sounds.MP3;

/**
 *
 * @author Do$$e
 */
public class MP3Sound {

    private String path;
    private MP3 p;

    public MP3Sound(String path) {
        this.path = path;
        p = new MP3(path);
        p.play();
        if(SoundBox.precaching) p.close();
    }

}
