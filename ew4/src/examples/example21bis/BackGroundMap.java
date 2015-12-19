/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example21bis;

import java.util.ArrayList;
import org.easyway.interfaces.extended.IFinalLoopable;
import org.easyway.objects.BaseObject;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.tiles.Tile;
import org.easyway.tiles.TileMapLayer;

/**
 *
 * @author RemalKoil
 */
public class BackGroundMap extends TileMapLayer implements IFinalLoopable {

    ArrayList<Sprite> toDestroy = new ArrayList<Sprite>(100);

    public BackGroundMap(int width, int height, int tileWidth, int tileHeight, int idLayer, String name) {
        super(width, height, tileWidth, tileHeight, idLayer, name);
    }

    public BackGroundMap(int width, int height, int tileWidth, int tileHeight, String name) {
        super(width, height, tileWidth, tileHeight, name);
    }

    public void drawOnBackground(Sprite spr) {
        Tile tiles[][] = MainKillThemAllBis.backgroundMap.getTilesUnderObject(spr);
        // we don't want to add the copy object to the game engine lists
        BaseObject.autoAddToLists = false;

        for (int x = 0; x < tiles.length; ++x) {
            for (int y = 0; y < tiles[x].length; ++y) {
                BackgroundTile bgTile = (BackgroundTile) tiles[x][y];
                if (bgTile != null) {
                    bgTile.drawOnBackground(new Sprite(spr));
                }
            }
        }
        BaseObject.autoAddToLists = true;
        toDestroy.add(spr);
    }

    @Override
    public void finalLoop() {
        for (Sprite spr : toDestroy) {
            spr.destroy();
        }
        toDestroy.clear();
    }
}
