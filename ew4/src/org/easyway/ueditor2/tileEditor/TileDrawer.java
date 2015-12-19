/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.ueditor2.tileEditor;

import org.easyway.ueditor2.effects.Effect;
import org.easyway.input.Mouse;
import org.easyway.tiles.Tile;
import org.easyway.tiles.TileMapLayer;

/**
 *
 * @author Daniele
 */
public class TileDrawer extends Effect {

    TileMapLayer tiledLayer;
    TilePalettePanel tilesPalette;

    public TileDrawer(TileMapLayer tiledLayer, TilePalettePanel tilesPalette) {
        this.tiledLayer = tiledLayer;
        this.tilesPalette = tilesPalette;
    }

    @Override
    public void loop() {
        if (tiledLayer == null || tiledLayer.isDestroyed()) {
            destroy();
            return;
        }
        if (Mouse.isLeftDown()) {
            int tilex = tiledLayer.getCoordX(Mouse.getXinWorld());
            int tiley = tiledLayer.getCoordY(Mouse.getYinWorld());
            if (tilex < 0 || tiley < 0 || tilex >= tiledLayer.getNumX() ||
                    tiley >= tiledLayer.getNumY()) {
                return;
            }
            Tile selectedTile = tilesPalette.getSelectedTile();
            tiledLayer.setTile(tilex, tiley, selectedTile);
        }
    }
}
