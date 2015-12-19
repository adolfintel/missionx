package examples.example11;

import org.easyway.collisions.BruteForceCollision;
import org.easyway.objects.texture.Texture;
import org.easyway.system.state.Game;
import org.easyway.utils.Timer.SyncTimer;

public class BruteForceCollisionExample extends Game {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new BruteForceCollisionExample();
    }

    public BruteForceCollisionExample() {
        super(800, 600, 24, false);
    }
    Texture image;

    @Override
    public void creation() {
        BruteForceCollision.createGroup("GroupA");
        image = Texture.getTexture("images/marwy/mattone.png");
        // from EW4 the following line is not more necessary:
        // image.createMask();
        // start making boxes
        new MyTimer();
    }

    @Override
    public void loop() {
    }

    // a special thread that call the method onTick with a regular "step time"
    class MyTimer extends SyncTimer {

        public MyTimer() {
            // the method onTick is called each 1000ms (1second)
            super(1000);
            // start this "sync-thread"
            start();
        }

        @Override
        public void onTick() {
            new FallingBox((float) Math.random() * (800 - 64), image);
        }
    }
}
