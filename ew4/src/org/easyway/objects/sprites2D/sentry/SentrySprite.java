package org.easyway.objects.sprites2D.sentry;

import java.io.Serializable;
import java.util.ArrayList;

import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.objects.animo.Animo;
import org.easyway.objects.sprites2D.SpriteColl;
import org.easyway.tiles.TileMapLayer;
import org.easyway.tiles.TileSprite;

public class SentrySprite extends SpriteColl implements Serializable {

    private static final long serialVersionUID = -5139860575042410163L;
    public ArrayList<Sentry> sentryList;

    public SentrySprite(boolean autoAdd, float x, float y, ITexture img, Animo animo, int layer, int idLayer) {
        super(autoAdd, x, y, img, animo, layer, idLayer);
    }

    public SentrySprite(boolean toAdd, int layer, int idLayer) {
        super(toAdd, layer, idLayer);
    }

    public SentrySprite(boolean toAdd, int layer) {
        super(toAdd, layer);
    }

    public SentrySprite(int layer, int idLayer) {
        super(layer, idLayer);
    }

    public SentrySprite(int layer) {
        super(layer);
    }

    public SentrySprite(ITexture image) {
        super(image);
    }

    public SentrySprite(boolean toAdd, float x, float y, Animo animo) {
        super(toAdd, x, y, animo);
    }

    public SentrySprite(float x, float y, Animo animo) {
        super(x, y, animo);
    }

    public SentrySprite(boolean toAdd, float x, float y, ITexture image) {
        super(toAdd, x, y, image);
    }

    public SentrySprite(boolean toAdd, float x, float y, ITexture image, int layer) {
        super(toAdd, x, y, image, layer);
    }

    public SentrySprite(float x, float y, ITexture image, int layer) {
        super(x, y, image, layer);
    }

    public SentrySprite(float x, float y, ITexture image) {
        super(x, y, image);
    }

    public SentrySprite(boolean toAdd, ITexture image) {
        super(toAdd, image);
    }

    public SentrySprite(boolean toAdd, float x, float y) {
        super(toAdd, x, y);
    }

    public SentrySprite(float x, float y) {
        super(x, y);
    }

    public SentrySprite(boolean toAdd) {
        super(toAdd);
    }

    public SentrySprite() {
    }

    public SentrySprite(SentrySprite obj) {
        super(obj);
        for (Sentry sentry : obj.getSentryList()) {
            addSentry((Sentry) sentry.clone());
        }
    }

    // --------------------------------------------------
    /** TO OVERRIDE */
    public void sentryCollision(Sentry sentry) {
    }

    public void addSentry(Sentry sentry) {
        getSentryList().add(sentry);
    }

    public Sentry addSentry(float x, float y, int width, int height, String name) {
        Sentry t;
        getSentryList().add(t = new Sentry(this, name, x, y, width, height));
        return t;
    }

    public void removeSentry(Sentry sentry) {
        getSentryList().remove(sentry);
    }

    public void removeSentry(String name) {
        for (Sentry s : getSentryList()) {
            if (s.getName().equals(name)) {
                s.destroy();
            }
        }
    }

    /**
     * this method is called when all collisions are tested
     */
    protected void finalLoop() {
    }

    // ---------------------------------------------------
    @Override
    public void setXY(float x, float y) {
        /*float stepx = (x - getX());
        float stepy = (y - getY());*/

        super.setXY(x, y);
        for (Sentry s : getSentryList()) {
            //s.setXY(s.getX() + stepx, s.getY() + stepy);
            s.update();
        }
    }


    @Override
    public void setSize(int width, int height) {
        float scalex = (float) width / (float) this.getWidth();
        float scaley = (float) height / (float) this.getHeight();
        for (Sentry s : sentryList) {
            s.setLocalXY(s.getLocalX() * scalex, s.getLocalY() * scaley);
            s.setSize((int) (s.width * scalex), (int) (s.height * scaley));
        }
        super.setSize(width, height);
    }

    protected ArrayList<Sentry> getSentryList() {
        return sentryList == null ? sentryList = new ArrayList<Sentry>(5) : sentryList;
    }

    /**
     * auto move the sprite in relation with a collision of a tile
     * @param stepx amount to move on x
     * @param stepy amount to move on y
     * @param source the sentry that will be tested in the collision
     * @param tm the TiledMap
     * @param tileName the tile name 
     */
    public void autoAlign(float stepx, float stepy, Sentry source, TileMapLayer tm, String tileName) {
        ArrayList<ISpriteColl> coll = tm.testCollision(source);
        boolean found = false;
        do {
            coll = tm.testCollision(source);
            if (coll == null) {
                return;
            }
            TileSprite tSprite;
            found = false;
            for (ISpriteColl spr : coll) {
                tSprite = (TileSprite) spr;
                if (tSprite.getTileType().equals(tileName)) {
                    found = true;
                    break;
                }
            }
            move(stepx, stepy);
        } while (found);

    }

    @Override
    public SentrySprite clone() {
        return new SentrySprite(this);
    }

    @Override
    protected void disactivate() {
        super.disactivate();

        for (Sentry sentry : getSentryList()) {
            sentry.destroy();
        }
    }
}
