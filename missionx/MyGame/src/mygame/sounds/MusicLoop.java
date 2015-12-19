/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.sounds;

import mygame.*;
import org.easyway.sounds.MP3;
import org.easyway.utils.TimerJGM;

/**
 *
 * @author Do$$e
 */
public class MusicLoop {

    private String path;
    private int msLength;
    MP3 music=null;
    private LoopChronometer lc;
    
    public MusicLoop(String path, int msLength) {
        if (CommonVar.music) {
            this.path = path;
            //if(!new File(path).exists()){System.out.println("Warning: music file "+path+" not found!"); return;}
            this.msLength = msLength;
            lc = new LoopChronometer(msLength);
            lc.start();
        }
    }

    private class LoopChronometer extends TimerJGM {

        public LoopChronometer(int msLength) {
            super(msLength);
                music=new MP3(path);
                music.play();
        }

        @Override
        public void onTick() {
                music.close();
                music=new MP3(path);
                music.play();
        }
    }

    public void stop() {
        if (CommonVar.music) {
            lc.stop();
            if(music!=null) music.close();
        }
    }
}
