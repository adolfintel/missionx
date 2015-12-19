package examples.example18;

import java.util.ArrayList;
import org.easyway.collisions.GroupCollision;
import org.easyway.input.Keyboard;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.objects.sprites2D.sentry.EventCaller;
import org.easyway.objects.sprites2D.sentry.Sentry;
import org.easyway.objects.sprites2D.sentry.SentrySprite;
import org.easyway.system.StaticRef;
import org.easyway.tiles.TileMapLayer;
import static examples.example18.Images.*;

/**
 *
 * @author Daniele Paggi
 */
public class Player extends SentrySprite implements ILoopable {

    EventCaller evCallerV, evCallerH;
    String tiledMapCollisions;

    public Player() {
        // super(0,0,BASKET_BALL.getImage());
        setImage(BASKET_BALL.getImage());
        setXY(200, 200);
        setSize(64, 64);

        Sentry left = addSentry(0, getHeight() / 4, getWidth() / 16, getHeight() / 2, "Left");
        Sentry right = addSentry(getWidth() / 16 * 15, getHeight() / 4, getWidth() / 16, getHeight() / 2, "Right");
        Sentry bottom = addSentry(getWidth() / 2 - getWidth() / 3 / 2, getHeight(), getWidth() / 3, getHeight() / 16, "Bottom");
        Sentry top = addSentry(getWidth() / 4, 0, getWidth() / 2, getHeight() / 16, "Top");

        GroupCollision gc = GroupCollision.getGroup("Wall");
        gc.addToSource(left);
        gc.addToSource(right);
        gc.addToSource(bottom);
        gc.addToSource(top);
        gc.addToSource(this);
        evCallerV = new EventCaller(this, "pressed", 2);
        evCallerH = new EventCaller(this, "pressed", 2);
        StaticRef.getCamera().centerOn(this);
    }

    public void pressed() {
        System.out.println("object pressed!");
    }

    public String getTiledMapCollisions() {
        return tiledMapCollisions;
    }

    public void setTiledMapCollisions(String tiledMapCollisions) {
        if (tiledMapCollisions != null && tiledMapCollisions.equals(this.tiledMapCollisions)) {
            return;
        }
        this.tiledMapCollisions = tiledMapCollisions;
        if (tiledMapCollisions != null && tiledMapCollisions.length() > 0) {
            TileMapLayer map = TileMapLayer.getTileMapLayer(tiledMapCollisions);
            if (map != null) {
                map.add(this);
                for (Sentry s : getSentryList()) {
                    map.add(s);
                }
            }
        }
    }

    @Override
    public void onCollision() {
        ArrayList<ISpriteColl> list = getCollisionList();
        for (ISpriteColl spr : list) {
            // do nothing
        }
    }

    @Override
    public void sentryCollision(Sentry sentry) {
        if (isDestroyed()) {
            return;
        }

        if (sentry.getName().equals("Bottom")) {
            for (ISpriteColl obj : sentry.getCollisionList()) {
                while (testCollision(obj)) {
                    move(0, -1);
                }
            }
            evCallerV.signal(1);
        } else if (sentry.getName().equals("Top")) {
            for (ISpriteColl obj : sentry.getCollisionList()) {
                while (testCollision(obj)) {
                    move(0, 1);
                }
            }
            evCallerV.signal(2);
        } else if (sentry.getName().equals("Left")) {
            for (ISpriteColl obj : sentry.getCollisionList()) {
                while (testCollision(obj)) {
                    move(1, 0);
                }
            }
            evCallerH.signal("left");
        } else if (sentry.getName().equals("Right")) {
            for (ISpriteColl obj : sentry.getCollisionList()) {
                while (testCollision(obj)) {
                    move(-1, 0);
                }
            }
            evCallerH.signal("right");
        }

    }

    @Override
    public void destroy() {
        if (isDestroyed()) {
            return;
        }
        // this code is ok!!
        evCallerV.destroy();
        // should speed up the Garbage Collector
        evCallerV = null;
        super.destroy();
    }

    @Override
    protected void finalize() throws Throwable {
        // SBAGLIATO!!
        // se 'evCaller' non è ancora stato distrutto allora e' raggiungibile
        // da qualche thread del game engine e siccome evCaller contiene un
        // reference a questa istanza di Player allora anche questa istanza e'
        // raggiungibile dallo stesso thread che e' raggiungibile evCaller.
        // Essendo, dunque, questa istanza raggiunbile da un thread non verra'
        // MAI eseguito questo metodo!
        //
        if (evCallerV != null) {
            evCallerV.destroy(); // ERRORE!
        }
        super.finalize();
    }

    public void loop() {
        move(0, 2);

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            move(-2, 0);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            move(2, 0);
        }
    }
}
