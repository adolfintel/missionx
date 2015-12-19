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
package org.easyway.tiles;

import org.easyway.objects.BaseObject;
import org.easyway.objects.sprites2D.SpriteColl;

public class TileSprite extends SpriteColl {

    private static final long serialVersionUID = -6319084634082751830L;
    /**
     * reference to tile manager
     */
    public TileMapLayer tm;
    /**
     * reference to the tile
     */
    public Tile tile;

    /**
     * creates a new instance
     * 
     * @param x
     *            position
     * @param y
     *            position
     * @param tm
     *            reference to tile manager
     * @param tile
     *            the tile
     */
    public TileSprite(float x, float y, TileMapLayer tm, Tile tile) {
        super(false, x, y, tile.getImage());
        isQuadTreeUsable = false;
        // super(false, x, y, tile.image);
        this.tm = tm;
        this.type = "$_TILESPRITE";
        setSize(tm.getTileWidth(), tm.getTileHeight());
        this.tile = tile;
        //autoAddToLists = oldAutoAddToList;
        // collisionable = true;
    }

    @Override
    public void activate() {
        //super.activate();
    }

    @Override
    protected void removeQuadsUpdate() {
        //super.removeQuadsUpdate();
    }

    @Override
    protected void autoActivate(boolean toAdd) {
        //super.autoActivate(toAdd);
    }

    @Override
    public void setIdLayer(int id) {
        //super.setIdLayer(id);
    }

    @Override
    public void setLayer(int layer) {
        //super.setLayer(layer);
    }

    @Override
    public boolean isQuadTreeUsable() {
        return false;
    }

    /**
     * changes the current tile image
     * 
     * @param tile
     */
    public void setTile(Tile tile) {
        if (tile == null) {
            destroy();
            return;
        }
        tm.setTile((int) (getX() - tm.getX()) / tm.getTileWidth(), (int) (getY() - tm.getY()) / tm.getTileHeight(), tile);
        this.tile = tile;
    }

    /**
     * returns the adiacents Tiles<br>
     * the contents of the matrix will be:<br>
     * tile tile tile<br>
     * tile THIS tile<br>
     * tile tile tile<br>
     * <br>
     * note that you can get a null-tile
     * 
     * @return
     */
    public TileSprite[][] getAdiacents() {
        Tile tempTile;
        TileSprite ts[][] = new TileSprite[3][3];
        int nx, ny;
        for (int ix = -1; ix < 2; ++ix) {
            for (int iy = -1; iy < 2; ++iy) {
                nx = (int) (getX() + (ix * tm.getTileWidth()) - tm.getX()) / tm.getTileWidth();
                ny = (int) (getY() + (iy * tm.getTileHeight()) - tm.getY()) / tm.getTileHeight();
                // check bounds
                if (nx < 0 || nx >= tm.getNumX() || ny < 0 || ny >= tm.getNumY()) {
                    ts[ix + 1][iy + 1] = null;
                    continue;
                }
                // code speeded up:
                // because I've did the check in the previous lines of code
                // I can write grid[nx][ny] instead of getTile(nx.ny)
                tempTile = tm.grid[nx][ny];// tm.getTile(nx, ny);
                if (tempTile == null) {
                    ts[ix + 1][iy + 1] = null;
                } else {
                    boolean oldState = BaseObject.autoAddToLists;
                    BaseObject.autoAddToLists = false;
                    ts[ix + 1][iy + 1] = new TileSprite(getX() + (ix * tm.getTileWidth()), getY() + (iy * tm.getTileHeight()), tm, tile);
                    BaseObject.autoAddToLists = oldState;
                }
            }
        }
        return ts;
    }

    /**
     * 
     */
    @Override
    public void onCollision() {
    }

    @Override
    public void onDestroy() {
        tm.setTile((int) (getX() - tm.getX()) / tm.getTileWidth(), (int) (getY() - tm.getY()) / tm.getTileHeight(), null);
    }

    @Override
    public void finalize() {
        kill();
    }

    public String getTileType() {
        return tile.getType();
    }

    public String getTileName() {
        return tile.getName();
    }

    public Tile getTile() {
        return tile;
    }

    public TileMapLayer getTileMapLayer() {
        return tm;
    }
}
