package examples.example12;

import org.easyway.input.Keyboard;
import org.easyway.objects.text.Text;
import org.easyway.sounds.MP3;
import org.easyway.sounds.MidiCore;
import org.easyway.sounds.WaveCore;
import org.easyway.system.state.Game;

public class SoundExample extends Game {

    private static final long serialVersionUID = 1L;

    public static void main(String args[]) {
        new SoundExample();
    }

    public SoundExample() {
        super(800, 600, 24, false);
    }
    WaveCore sound;
    MidiCore midi;
    MP3 mp3;
    boolean mp3p;

    @Override
    public void creation() {
        // load the sound
        setTimeOut(0);
        sound = new WaveCore("/examples/example12/gun.wav");
        midi = new MidiCore("/examples/example12/mozart.mid");
        midi.play();

        mp3 = new MP3("/examples/example12/Les choristes.mp3");
        mp3p = false;

        new Text(0, 0, "Press SPACE to play the sound", null);
        new Text(0, 30, "Press RETURN to stop/play the MIDI music", null);
        new Text(0, 60, "Press CTRL to stop/play the MP3 music", null);
    }

    @Override
    public void loop() {
        // play the sound
        if (Keyboard.isKeyPressed(Keyboard.KEY_SPACE)) {
            sound.play();
        }
        if (Keyboard.isKeyPressed(Keyboard.KEY_RETURN)) {
            if (midi.isPlaying()) {
                midi.stop();
            } else {
                midi.play();
            }
        }
        if (Keyboard.isKeyPressed(Keyboard.KEY_LCONTROL) || Keyboard.isKeyPressed(Keyboard.KEY_RCONTROL)) {
            if (mp3p == true) {
                mp3.close();
                mp3p =  false;
            } else {
                mp3.play();
                mp3p = true;
            }
        }

    }
}
