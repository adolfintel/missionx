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
 */
package org.easyway.sounds;

import java.io.InputStream;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;

import org.easyway.utils.Utility;

/**
 * Play a midi sound.
 * 
 */
public class MidiCore implements MetaEventListener {

    // end music
    private static final int MIDI_EOT_MESSAGE = 47;

    // volume
    private static final int GAIN_CONTROLLER = 7;
    /**
     * the file path of midi
     */
    private String path;
    /**
     * the midi stream
     */
    private InputStream stream;
    /**
     * indicates if the midi is playing or not
     */
    private boolean playing;
    /**
     * indicates if the midi is a looping midi or not
     */
    private boolean loop;
    /**
     * the volume of music
     */
    private float volume;
    /**
     * the thread of midi player
     */
    //private MidiThread player;
    private Sequencer sequencer;
    private static int available = 0;
    private static boolean volumeSupported;
    

    static {
        Thread thread = new Thread() {

            public final void run() {
                try {
                    Sequencer sequencer = MidiSystem.getSequencer();
                    volumeSupported = (sequencer instanceof Synthesizer);
                    available = 1;
                } catch (Exception e) {
                    Utility.error("Midi audio isn't supported", "MidiTest");
                    available = -1;
                }
            }
        };
        thread.setDaemon(false);
        thread.start();
    }

    /**
     * Creates new midi audio renderer from a path
     */
    public MidiCore(String path) {
        volume = 1.0f;
        // --- prepare the path ---
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
        // openClipStream();
        }

    }

    /**
     * clone a midi
     * 
     * @param midi
     *            the original MidiCore to clone
     */
    public MidiCore(MidiCore midi) {
        this.path = midi.path;
        this.volume = midi.volume;
        this.playing = false;
        this.loop = midi.loop; // if we are cloning a midi.. the loop should be
    // FALSE.. ( it's very rare that we'll need to clone a MIDI O__O )

    }

    protected void openStream() {
        try {
            stream = Thread.currentThread().getContextClassLoader().getResource(path).openStream();
        } catch (Exception e) {
            Utility.error("EXCEPTION: Midi '" + path + "' was not found!",
                    "MidiCore(String)", e);
        }

    }

    // -----------------------------------------------------------------
    // ---------------------------MUSIC-CONTROLL------------------------
    // -----------------------------------------------------------------
    /**
     * rewind the clip and then execute it
     */
    public void replay() {
        while (available == 0) {
            try {
                Thread.sleep(25L);
            } catch (InterruptedException ex) {
                
            }
        }
        if (available == -1) {
            return;
        }
        stop();

        if (sequencer != null) {
            sequencer.start();
            sequencer.addMetaEventListener(this);
            playing = true;
            return;
        }
        playSound();
    /*
    player = null;
    player = new MidiThread();
    player.setDaemon(false);
    player.start();
     */
    }

    /**
     * play a new instance of the sound
     */
    public void play() {
        while (available == 0) {
            try {
                Thread.sleep(25L);
            } catch (InterruptedException ex) {
                
            }
        }
        if (available == -1) {
            return;
        }
        if (playing) {
            new MidiCore(this).play();
            return;
        }
        playSound();
    /*
    player = new MidiThread();
    player.setDaemon(false);
    player.start();
     */
    }

    public void stop() {
        
        playing = false;
        if (sequencer == null) {
            return;
        }
        sequencer.stop();
        sequencer.setMicrosecondPosition(0);
        sequencer.removeMetaEventListener(this);
        sequencer = null;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean b) {
        loop = b;
    }

    // -----------------------------------------------------------------
    // --------------------------LINSTENER------------------------------
    // -----------------------------------------------------------------
    /**
     * Notify when the sound has finished playing.
     */
    public void meta(MetaMessage msg) {
        if (msg.getType() == MIDI_EOT_MESSAGE) {
            if (loop) {
                playing = true;
                sequencer.setMicrosecondPosition(0);
                sequencer.start();
            } else {
                playing = false;
                sequencer.setMicrosecondPosition(0);
                sequencer.removeMetaEventListener(this);
                sequencer = null;
            }
        }
    }

    // -----------------------------------------------------------------
    // ----------------------------VOLUME-------------------------------
    // -----------------------------------------------------------------
    public void setVolume(float volume) {
        if (sequencer == null || this.volume == volume) {
            this.volume = volume;
            return;
        }
        if (isVolumeSupported() == false) {
            Utility.error("Setting midi volume not supported!",
                    "MidiCore.setVolume(float)");
            return;
        }
        this.volume = volume;

        MidiChannel[] channels = ((Synthesizer) sequencer).getChannels();

        for (int i = 0; i < channels.length; i++) {
            channels[i].controlChange(GAIN_CONTROLLER, (int) (volume * 127));
        }
    }

    public float getVolume() {
        if (sequencer instanceof Synthesizer == false) {
            return volume;
        }

        MidiChannel[] channels = ((Synthesizer) sequencer).getChannels();

        // all channels are supposed to have the same volume (?)
        return ((float) channels[0].getController(GAIN_CONTROLLER)) / 127;
    }

    public boolean isVolumeSupported() {
        return volumeSupported;
    }

    // -----------------------------------------------------------------
    // ------------------------------MANAGMENT--------------------------
    // -----------------------------------------------------------------
    public String getAudioFile() {
        return path;
    }

    public InputStream getAudioStream() {
        return stream;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isAvailable() {
        return available == 1;
    }

    // -----------------------------------------------------------------
    // -------------------------------THREAD-PLAYER---------------------
    // -----------------------------------------------------------------
    private void playSound() {
        if (sequencer != null && sequencer.isOpen()) {
            return;
        }
        openStream();
        try {
            if (sequencer == null) {
                sequencer = MidiSystem.getSequencer();
                if (!sequencer.isOpen()) {
                    sequencer.open();
                }
            }

            Sequence seq = MidiSystem.getSequence(stream);
            sequencer.setSequence(seq);
            sequencer.start();
            sequencer.addMetaEventListener(MidiCore.this);

            playing = true;

            // the volume of newly loaded audio always 1.0f
            float oldVolume = volume;
            if (oldVolume != 1.0f) {
                volume = 1.0f;
                setVolume(oldVolume);
            }
        } catch (Exception e) {
            stream = null;
            playing = false;
            Utility.error("Error playing the midi: '" + path + "'",
                    "MidiCore.play()", e);
        }
    }

    /*private class MidiThread extends Thread {
    
    public final void run() {
    playSound();
    }
    }
     */
    public void finalize() {
        if (sequencer != null) {
            sequencer.stop();
        }
    }
}