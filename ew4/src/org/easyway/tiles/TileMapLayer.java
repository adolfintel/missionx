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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.easyway.collisions.CollisionUtils;
import org.easyway.collisions.ICollisionMethod;
import org.easyway.collisions.methods.HardWarePixelMethod;
import org.easyway.collisions.MgcList;
import org.easyway.collisions.SingleCollisionList;
import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayerID;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.lists.GameList;
import org.easyway.objects.Camera;
import org.easyway.objects.Plain2D;
import org.easyway.objects.sprites2D.Mask;
import org.easyway.objects.sprites2D.StaticFullyMask;
import org.easyway.shader.Shader;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.easyway.utils.Utility;

/**
 * implements the tile engine<br>
 * example:<br>
 * ...<br>
 * TileManager tm = new TileManger( 1000, 1000, 64, 64, true, 3 );<br>
 * Texture image = new Texture("path.png");<br>
 * Tile t = new Tile(image);<br>
 * tm.setTile( 0, 0, t );<br>
 * tm.setTile( 1, 0, t );<br>
 * ...<br>
 * <br>
 */
public class TileMapLayer extends Plain2D implements IDrawing, ISpriteColl,
        ILayerID {

    private static final long serialVersionUID = -7576756624211903331L;
    /** depth of TileManager */
    private int layer = 0;
    /**
     * the drawing sheet
     */
    private int idLayer = -1;
    /** number of tiles minus one */
    transient private int numX, numY;
    /** sizes of tiles */
    transient private int tileWidth, tileHeight;
    /** an array that record the used tiles */
    transient public Tile grid[][];
    // public boolean isPrecise = true;
    /**
     * indicates if the object is added or not to the auto-collisionLists.
     */
    protected boolean collAdded = false;
    // protected float x, y;
    /** records that objects has gone a collision with a tile of tile manager */
    transient protected ArrayList<ISpriteColl> collisions;
    /**
     * the tileMap mask
     */
    protected StaticFullyMask mask;
    protected TiledCollisionList collisionList;
    /**
     * indicates if the tilemanager is to show at screen or not
     */
    protected boolean visible = true;
    /**
     * the tileSet that the TileManager use
     */
    transient protected TileSet tileSet;
    /** the used shader in the rendering process */
    private Shader shader = Shader.getDefaultShader();
    private ICollisionMethod collisionMethod = HardWarePixelMethod.getDefaultInstance();

    /**
     * creates a new instance of tileMap
     *
     * @param width
     *            width of tile manager: number of tile across the X axis
     * @param height
     *            height of tile manager: number of tile across the Y axis
     * @param tileWidth
     *            the size of tile used by the tile manager
     * @param tileHeight
     *            the size of tile used by the tile manager
     * @param name
     *            the name of the TiledMap
     */
    public TileMapLayer(int width, int height, int tileWidth, int tileHeight,
            String name) {
        this(width, height, tileWidth, tileHeight, 2, name);
    }

    /**
     * creates a new instance of tileMap
     *
     * @param width
     *            width of tile manager: number of tile across the X axis
     * @param height
     *            height of tile manager: number of tile across the Y axis
     * @param tileWidth
     *            the size of tile used by the tile manager
     * @param tileHeight
     *            the size of tile used by the tile manager
     * @param autoAdd
     *            auto adds the TileManager to the engin list (for example the
     *            drawing list)
     * @param idlayer
     *            the id of the drawing sheet
     * @param name
     *            the name of the TiledMap
     */
    public TileMapLayer(int width, int height, int tileWidth, int tileHeight, int idLayer, String name) {
        super();
        isQuadTreeUsable = false;
        // StaticRef.layers[idlayer].add(this);
        setIdLayer(idLayer);
        type = name;
        this.name = name;
        this.numX = width;// - 1;
        this.numY = height;// - 1;
        setSize(width * tileWidth, height * tileHeight);
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        grid = new Tile[width][height];
        collisions = new ArrayList<ISpriteColl>(100);
        mask = new StaticFullyMask((numX - 1) * tileWidth, (numY - 1) * tileHeight);
        collisionList = new TiledCollisionList(this);
        GameState.getCurrentGEState().getCoreCollision().tileGroups.add(this);
    }

    /**
     * sets a tile on the coordinates x,y
     *
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param tile
     *            the tile to sets
     */
    public boolean setTile(int x, int y, Tile tile) {
        if (x < 0 || x >= numX) {
            Utility.error("x out of bounds",
                    "TileManager.setTile(int,int,Tile)");
            return false;
        }
        if (y < 0 || y >= numY) {
            Utility.error("y out of bounds",
                    "TileManager.setTile(int,int,Tile)");
            return false;
        }
        grid[x][y] = tile;
        return true;
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    @Override
    public void render() {
        if (!visible) {
            return;
        }
        int startx = Math.max(0, (int) (Camera.getCurrentCamera().x - getX()) / tileWidth);

        if (startx >= numX) {
            return;
        }
        int endx = Math.min(numX - 1, startx + Camera.getCurrentCamera().getWidth() / tileWidth + 1);
        if (endx < 0) {
            return;
        }
        if (startx > endx) {
            return;
        }

        int starty = Math.max(0, (int) (Camera.getCurrentCamera().y - getY()) / tileHeight);
        if (starty >= numY) {
            return;
        }

        int endy = Math.min(numY - 1, starty + Camera.getCurrentCamera().getHeight() / tileHeight + 1);
        if (endy < 0) {
            return;
        }
        if (starty > endy) {
            return;
        }

        int sx = startx, sy;

        float coordstartx = startx * tileWidth + getX() - Camera.getCurrentCamera().x;//Math.max( -StaticRef.getCamera().x % tileWidth, x - StaticRef.getCamera().x);
        float coordstarty = starty * tileHeight + getY() - Camera.getCurrentCamera().y;//Math.max( -StaticRef.getCamera().y % tileHeight, y - StaticRef.getCamera().y);
        float coordendx = (endx - startx) * tileWidth + coordstartx;
        float coordendy = (endy - starty) * tileHeight + coordstarty;

        float cx, cy;
        Tile temp;
        if (shader != null) {
            shader.bind();
            shader.update(this);
        }
        // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        for (sy = starty, sx = startx, cy = coordstarty; cy <= coordendy; cy += tileHeight, ++sy) {
            cx = coordstartx;
            sx = startx;
            if ((temp = grid[sx][sy]) != null && temp.image != null) {
                temp.render(cx, cy, tileWidth, tileHeight);
            }
            for (cx += tileWidth, ++sx; cx <= coordendx; cx += tileWidth, ++sx) {
                if ((temp = grid[sx][sy]) != null && temp.image != null) {
                    temp.render(cx, cy, tileWidth, tileHeight);
                }
            }

        }

    }

    /**
     * sets all the tiles to the selected tile
     *
     * @param tile
     *            the selected tile
     */
    public void setAllTileTo(Tile tile) {
        for (int i = 0; i < numX; ++i) {
            for (int j = 0; j < numY; ++j) {
                grid[i][j] = tile;
            }
        }
    }

    public int getLayer() {
        return layer;
    }

    /**
     * returns the tile at position numX, numY
     *
     * @param numX
     *            x position of the tile on the TileManager
     * @param numY
     *            x position of the tile on the TileManager
     */
    public Tile getTile(int x, int y) {
        if (x < numX && x >= 0) {
            if (y >= 0 && y < numY) {
                return grid[x][y];
            }
            Utility.error("y out of bounds", "TileManager.getTile(int,int)");
            return null;
        }
        Utility.error("x out of bounds", "TileManager.getTile(int,int)");
        return null;
    }

    /**
     * returns the number of tiles that the tile manager can manages across the
     * Y axis
     */
    public int getNumY() {
        return numY;
    }

    /**
     * returns the height of tiles managed
     */
    public int getTileHeight() {
        return tileHeight;
    }

    /**
     * returns the width of tiles managed
     */
    public int getTileWidth() {
        return tileWidth;
    }

    /**
     * returns the number of tiles that the tile manager can manages across the
     * X axis
     */
    public int getNumX() {
        return numX;
    }

    /**
     * change the depth of tile manager
     */
    public void setLayer(int layer) {
        this.layer = layer;
        readdToDrawingLists();
    }

    /**
     * change the Height of tile managed
     *
     * @param tileHeight
     *            the new height to use
     */
    public void setTileHeight(int tileHeight) {
        if (tileHeight <= 0) {
            return;
        }
        this.tileHeight = tileHeight;
        setHeight(tileHeight * (numY - 1));
        mask = new StaticFullyMask((numX - 1) * tileWidth, (numY - 1) * tileHeight);
    }

    /**
     * change the Width of tile managed
     *
     * @param tileWidth
     *            the new width to use
     */
    public void setTileWidth(int tileWidth) {
        if (tileWidth <= 0) {
            return;
        }
        this.tileWidth = tileWidth;
        setWidth(tileWidth * (numX - 1));
        mask = new StaticFullyMask((numX - 1) * tileWidth, (numY - 1) * tileHeight);
    }

    @Override
    public ICollisionMethod getCollisionMethod() {
        return collisionMethod;
    }

    public void setCollisionMethod(ICollisionMethod collMethod) {
        this.collisionMethod = collMethod;
    }

    @Override
    public void onCollision() {
        ISpriteColl sprite;
        // ArrayList<ISpriteColl> tempList;
        // int index;
        SingleCollisionList coll;
        TileIterator ti;

        Iterator<ISpriteColl> it = collisions.iterator();

        while (it.hasNext()) {
            /*boolean oldstate = BaseObject.autoAddToLists;
            BaseObject.autoAddToLists = false;*/
            sprite = it.next();// collisions.get(i);// (ISpriteColl)
            // collisions.getCurrent();
            assert sprite != null : "errore di inserimento?";

            if ((sprite.getCollisionList()) == null) {
                continue;
            }
            coll = new SingleCollisionList(false, sprite);
            // coll.isPrecise = this.isPrecise;
            ti = new TileIterator(this, sprite);
            while (ti.hasElement()) {
                if (ti.getCurrent() != null) {
                    coll.add(ti.getCurrent());
                }
            }
            //BaseObject.autoAddToLists = oldstate;
            if (coll.size() > 0) // 0.1.9
            {
                coll.loop();
            }
        }

        collisions.clear();
    }

    /**
     * checks if the sprite has or not a collision with a tile of tile manager
     *
     * @param sprite
     *            the sprite to check
     */
    public ArrayList<ISpriteColl> testCollision(ISpriteColl sprite) {
        // ArrayList<ISpriteColl> coll = sprite.getCollisionList();
        // if (coll == null) {
        // return null;
        // }
        ArrayList<ISpriteColl> coll = new ArrayList<ISpriteColl>(10);
        if (!CollisionUtils.rectangleHit(this, sprite)) {
            return null;
        }
        TileIterator ti = new TileIterator(this, sprite);
        while (ti.hasElement()) {
            if (ti.getCurrent() != null) {
                if (CollisionUtils.rectangleHit(ti.getCurrent(), sprite)) {
                    if (Core.getGLIntVersion() >= 15) {
                        if (CollisionUtils.trueHardWareHit(ti.getCurrent(), sprite)) {
                            coll.add(ti.getCurrent());
                        }
                    } else {
                        if (CollisionUtils.trueHit(ti.getCurrent(), sprite)) {
                            coll.add(ti.getCurrent());
                        }
                    }
                }
            }
        }
        return coll;
    }

    @Override
    public ArrayList<ISpriteColl> getCollisionList() {
        return collisions;
    }

    public Mask getMask() {
        assert mask != null;
        return mask;
        // return new StaticFullyMask((numX - 1) * tileWidth, (numY - 1)
        // * tileHeight);
    }

    @Override
    public float getRotation() {
        return 0;
    }

// -----------------------------------------------------------------
// ---------------------------- IDLAYER-----------------------------
// -----------------------------------------------------------------
    @Override
    public int getIdLayer() {
        return idLayer;
    }

    @Override
    public void setIdLayer(int id) {
        if (idLayer != -1) {
            GameState.getCurrentGEState().getLayers()[idLayer].remove(this);
        }

        if (id < 0) {
            id = 0;
        } else if (id > GameState.getCurrentGEState().getLayers().length) {
            id = GameState.getCurrentGEState().getLayers().length;
        }

        idLayer = id;
        GameState.getCurrentGEState().getLayers()[idLayer].add(this);
    }

    @Override
    public boolean isAddedToCollisionList() {
        return collAdded;
    }

    @Override
    public void setAddedToCollisionList(boolean value) {
        collAdded = value;
    }

    /**
     * adds a sprite to the testing-collision list
     *
     * @param sprite
     */
    public void add(ISpriteColl sprite) {
        collisionList.add(sprite);
    }

    /**
     * removes a sprite from the testing-collision list
     *
     * @param sprite
     */
    public void remove(ISpriteColl sprite) {
        collisionList.remove(sprite);
    }

    /**
     * returns the testing-collision list
     *
     * @return
     */
    public TiledCollisionList getTestingCollisionList() {
        return collisionList;
    }

    public void setTestingCollisionList(GameList<ISpriteColl> list) {
        collisionList.setList(list);
    }

    /**
     * clears the testing-collision list.<br>
     * note: O( n ); with 'n' = list.size()
     */
    public void clearTestingCollisionList() {
        collisionList.removeAll();
    }

    public static TileMapLayer getTileMapLayer(
            String name) {
        return GameState.getCurrentGEState().getCoreCollision().searchTileMap(name);
    }

    public int getCoordX(float worldX) {
        return (int) (worldX - getX()) / tileWidth;
    }

    public int getCoordY(float worldY) {
        return (int) (worldY - getY()) / tileHeight;
    }

    @Override
    public String toString() {
        return getType().toString();
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void destroy() {
        if (isDestroyed()) {
            return;
        }

        for (TileMap tileMap : TileMap.tileMapList) {
            if (tileMap.getTileManagers().contains(this)) {
                tileMap.getTileManagers().remove(this);
            }

        }
        super.destroy();
    }

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        // String str = (String) s.readObject();
        System.out.println("TileLayer: " + name + " instance: " + this.hashCode());
        readFromFile(Core.getSavingOperationPath() + name + "_layer");
        if (collisions == null) {
            collisions = new ArrayList<ISpriteColl>(100);
        }
    }

    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultWriteObject();
        // s.writeObject(name);
        System.out.println("TLayer: " + name);
        writeOnFile(Core.getSavingOperationPath() + name + "_layer");
    }

    public void readFromFile(String file) {
        try {
            ObjectInputStream in;
            try {
                in = new ObjectInputStream(new FileInputStream(file));
            } catch (IOException ex) {
                in = Utility.getLocalFile(file);
            }
            if (in == null) {
                return;
            }
            // read the version (at this time it's only 1)
            int version = in.readInt();

            // fields:
            numX = in.readInt();
            numY = in.readInt();
            grid = new Tile[numX][numY];

            //System.out.println("USE QUAD: "+quad);
            // quad = false;
            //tileWidth = in.readInt();
            setTileWidth(in.readInt());
            //tileHeight = in.readInt();
            setTileHeight(in.readInt());
            setType((String) in.readObject());
            setName((String) in.readObject());

            //String tileSetName = (String) in.readObject();
            tileSet = (TileSet) in.readObject();
            // ~tileSet = TileSet.readFromFile(tileSetName);// -------TODO
            Tile tile;

            for (int y = 0; y
                    < numY; ++y) {
                for (int x = 0; x
                        < numX; ++x) {
                    //tile = grid[x][y];
                    int tileid = in.readInt();
                    tile = tileSet.get(tileid);
                    grid[x][y] = tile;
                }

            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void writeOnFile(String filename) {
        try {
            System.out.println("writing on: " + filename);
            //new RuntimeException().printStackTrace();
            FileOutputStream fout = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fout);

            // the -1 indicates that is a new version
            //out.writeInt(-1);
            // the VERSION-id

            // fields:
            out.writeInt(1);
            out.writeInt(numX);
            out.writeInt(numY);
            out.writeInt(tileWidth);
            out.writeInt(tileHeight);
            out.writeObject(getType().toString()); // name
            out.writeObject(getName());

            // TODO: write TileSet

            //tileSet.writeOnFile(filename+"_tileset");
            out.writeObject(tileSet);
            // grid:
            Tile tile;

            for (int y = 0; y < numY; ++y) {
                for (int x = 0; x < numX; ++x) {
                    tile = grid[x][y];

                    if (tile != null) {
                        // the ID of the tile
                        out.writeInt(tileSet.getIndex(tile));
                    } else { // tile == null
                        // write -1 for a null tile
                        out.writeInt(-1);
                    }

                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public TileSet getTileSet() {
        return tileSet;
    }

    public Tile[][] getTilesUnderObject(IPlain2D obj) {
        IPlain2D outObj = CollisionUtils.getCircleRectangle(obj);
        int initialx = getCoordX(outObj.getX());
        int initialy = getCoordY(outObj.getY());
        int finalx = getCoordX(outObj.getX() + outObj.getWidth());
        int finaly = getCoordX(outObj.getY() + outObj.getHeight());

        Tile[][] tiles = new Tile[finalx - initialx + 1][finaly - initialy + 1];
        for (int y = initialy; y <= finaly; ++y) {
            for (int x = initialx; x <= finalx; ++x) {
                if (x < numX && x >= 0 && y >= 0 && y < numY) {
                    tiles[x - initialx][y - initialy] = getTile(x, y);
                } else {
                    tiles[x - initialx][y - initialy] = null;
                }
            }
        }
        return tiles;
    }

    public void setTileSet(TileSet newTileSet) {
        if (tileSet != null && newTileSet != null
                && newTileSet != tileSet) {

            Tile tile;
            Tile newTile;

            int index;
            for (int y = 0; y < numY; ++y) {
                for (int x = 0; x < numX; ++x) {
                    if ((tile = grid[x][y]) != null) {
                        grid[x][y] = (index = tileSet.getIndex(tile)) == -1 ? tile : (newTile = newTileSet.get(index)) == null ? tile : newTile;
                    }

                }
            }

        }

        this.tileSet = newTileSet;
    }

    @Override
    public ITexture getImage() {
        return null;
    }

    @Override
    public ArrayList getQuadEntries() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MgcList getMgcs() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList getUsedInQuadNodes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRotation(float newRotation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isQuadTreeUsable() {
        return false;
    }

    @Override
    public void renderAt(float x, float y, float z, int unit) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getCollisionData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Class getCollisionDataType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}// end class

