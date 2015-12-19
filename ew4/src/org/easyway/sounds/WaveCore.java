/* EasyWay Game Engine
 * Copyright (C) 2006 Daniele Paggi.
 *  
 * Written by: 2006 Daniele Paggi<dshnt@hotmail.com>
 *   
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * * @Author Daniele Paggi, Do$$e
 */
package org.easyway.sounds;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

import org.easyway.utils.Utility;

/**
 * Play wave sounds
 */
public class WaveCore implements LineListener {

    /**
     * the stream of audio
     */
    private InputStream clipStream;
    /**
     * the path of audio
     */
    private String path;
    /**
     * the audio clip
     */
    private Clip clip;
    /**
     * indicates if the clip is playing or not
     */
    // private boolean playing;
    /**
     * indicates if the clip is looping or not
     */
    private boolean loop;
    /**
     * the clip's volume
     */
    private float volume;
    /**
     * the wave player thread
     */
    //private WaveThread player;

    // ----------------------------------------------------------
    // ----------------------- STATIC METHOD --------------------
    // ----------------------------------------------------------
    /**
     * indicates if is avaiable the wave audio
     */
    private static boolean available = true;
    /**
     * indicates if is supported the volume
     */
    private static boolean volumeSupported;
    

    static {
        Thread thread = new Thread() {

            public final void run() {
                try {
                    URL sample = WaveCore.class.getResource("test.wav");
                    AudioInputStream ain = AudioSystem.getAudioInputStream(sample);
                    AudioFormat format = ain.getFormat();

                    DataLine.Info info = new DataLine.Info(Clip.class, ain.getFormat(), ((int) ain.getFrameLength() * format.getFrameSize()));
                    Clip clip = (Clip) AudioSystem.getLine(info);

                    clip.open(ain);

                    Control.Type volType = FloatControl.Type.VOLUME;
                    volumeSupported = clip.isControlSupported(volType);
                    //the next 2 lines has been commented for debugging purpuse.
                    //clip.drain();
                    //clip.close();
                } catch (Exception e) {
                    Utility.error("Your computer doesn't support Wave audio",
                            "WaveTest");
                    available = false;
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    // ----------------------------------------------------------
    // ----------------------- CONSTRUCTORS ---------------------
    // ----------------------------------------------------------
    /**
     * Creates new wave audio renderer.
     */
    public WaveCore(String path) {
        // --- prepare the path ---
        // InputStream is;
        // InputStream is = null;
        {
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            int index;
            // path = path.replaceAll("\\", "/");
            while ((index = path.indexOf("\\")) != -1) {
                path = path.substring(0, index) + '/' + path.substring(index + 1);
            }
            this.path = path;
//			openClipStream();
        }

        // --- load the file as stream ---
        // clipStream = null;
        // try {
        // clipStream = Thread.currentThread().getContextClassLoader()
        // .getResource(path).openStream();
        // } catch (Exception e) {
        // Utility.error("EXCEPTION: Wave '" + path + "' was not found!",
        // "WaveCore(String)", e);
        // }
        assert clipStream != null;

        volume = 1.0f;
    }

    protected WaveCore(WaveCore wave) {
        this.path = wave.path;
//		openClipStream();
        // try {
        // clipStream.close();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        this.volume = wave.volume;
        this.loop = false; // if we are cloning a wave.. the loop should be
        // this.playing = false;
        // false..

        playSound();
    /*
    player = new WaveThread();
    player.setDaemon(false);
    player.start();
     */
    }

    protected void openClipStream() {
        try {
            clipStream = Thread.currentThread().getContextClassLoader().getResource(path).openStream();
        } catch (IOException e) {
            Utility.error("EXCEPTION: Wave '" + path + "' was not found!",
                    "WaveCore(String)", e);
        }
    }

    // ----------------------------------------------------------
    // ----------------------- USER METHODS ---------------------
    // ----------------------------------------------------------
    /**
     * rewind the clip and then execute it
     * 
     * @throws LineUnavailableException
     */
    public void replay() {
        if (clip != null) {
            if (clip.isOpen()) {
                clip.removeLineListener(this);
                clip.flush();
                clip.drain();
                clip.stop();
                clip.setMicrosecondPosition(0);
                clip.setFramePosition(0);
                clip.addLineListener(this);
                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }
                //System.out.println("RE");
                return;
            }
            //System.out.println("NEW");
            play();

        }
    }

    /**
     * play a new instance of the sound
     */
    public void play() {
        if (clip != null && clip.isOpen()) {
            new WaveCore(this);
            return;
        }
        playSound();
    /*
    player = new WaveThread();
    player.setPriority(Thread.MAX_PRIORITY);
    player.setDaemon(false);
    player.start();
     */
    }

    public void stop() {
        if (clip == null) {
            return;
        }

        clip.stop();
        clip.setMicrosecondPosition(0);
        clip.removeLineListener(this);
        clip.close();
        clip = null;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean value) {

        if (loop == value) {
            return;
        }
        loop = value;

        if (clip != null && clip.isOpen()) {
            clip.stop();
            play();
        // if (loop) {
        // clip.loop(Clip.LOOP_CONTINUOUSLY);
        // } else {
        // clip.start();
        // }
        }
    }

    // ----------------------------------------------------------
    // ------------------------- LINSTENER ----------------------
    // ----------------------------------------------------------
    /**
     * Notify when the sound is stopped externally.
     */
    public void update(LineEvent e) {
        if (e.getType() == LineEvent.Type.STOP) {
            clip.stop();
            clip.setMicrosecondPosition(0);
            clip.removeLineListener(this);
            clip.close();
            clip = null;
        }
    }

    // ----------------------------------------------------------
    // -------------------------- VOLUME ------------------------
    // ----------------------------------------------------------
    public void setVolume(float volume) {
        if (clip == null) {
            this.volume = volume;
            return;
        } else if (this.volume == volume) {
            return;
        }
        if (isVolumeSupported() == false) {
            Utility.error("Settng wave volume not supported",
                    "WaveCore.setVolume(float)");
            return;
        }

        this.volume = volume;

        Control.Type volType = FloatControl.Type.VOLUME;
        FloatControl control = (FloatControl) clip.getControl(volType);
        control.setValue(volume);
    }

    public float getVolume() {
        return volume;
    }

    public boolean isVolumeSupported() {
        return volumeSupported;
    }

    // ----------------------------------------------------------
    // ------------------------- GETS SETS ----------------------
    // ----------------------------------------------------------
    public String getAudioFile() {
        return path;
    }

    public InputStream getAudioStream() {
        return clipStream;
    }

    public boolean isPlaying() {
        return clip != null && clip.isOpen();
    }

    public boolean isAvailable() {
        return available;
    }

    protected void playSound() {

        if (clip != null && clip.isOpen()) {
            return;
        }

        // if (clipStream == null)
        // return;

        try {
            openClipStream();
            AudioInputStream ain = AudioSystem.getAudioInputStream(clipStream);
            AudioFormat format = ain.getFormat();

            if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                AudioFormat temp = new AudioFormat(
                        // convert to PCM
                        AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2,
                        format.getFrameRate(), true);
                ain = AudioSystem.getAudioInputStream(temp, ain);
                format = temp;
            }
            DataLine.Info info = new DataLine.Info(Clip.class, ain.getFormat(), ((int) ain.getFrameLength() * format.getFrameSize()));

            clip = (Clip) AudioSystem.getLine(info);
            clip.addLineListener(WaveCore.this);
            try{
                clip.open(ain);
            }catch(Exception e){return;}
            // playing = true;
            if (loop) {
                if (clip != null) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            } else {
                if (clip != null) {
                    clip.start();
                }
            }

            // the volume of newly loaded audio always 1.0f
            // float oldVolume = volume;
            if (volume != 1.0f) {
                volume = 1.0f;
                setVolume(volume);
            }
        } catch (Exception e) {
            if (clip != null) {
                clip = null;
            }
            Utility.error("Error playing the wav: '" + path + "'",
                    "WaveCore.play()", e);
        }

    }

    // ----------------------------------------------------------
    // ----------------------- WAVE THREAD ----------------------
    // ----------------------------------------------------------
    private class WaveThread extends Thread {

        public final void run() {
            playSound();
        }
    }
}
