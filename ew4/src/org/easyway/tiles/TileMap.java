package org.easyway.tiles;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

// import java.util.HashMap;
import org.easyway.system.Core;
import org.easyway.utils.Utility;

public class TileMap implements Serializable {

    public static ArrayList<TileMap> tileMapList = new ArrayList<TileMap>(5);
    private static final long serialVersionUID = -920462280853911498L;
    //protected static final HashMap<String, TileMap> tileMapList = new HashMap<String, TileMap>();

    public static TileMap getTileMap(String name) {
        //return tileMapList.get(name);

        for (TileMap tm : tileMapList) {
            if (tm.name.equals(name)) {
                return tm;
            }
        }
        return null;

    }
    volatile ArrayList<TileMapLayer> tileLayers;
    /**
     * the number of tiles in width and height
     */
    int width, height;
    /**
     * the size of the tiles
     */
    int tileWidth, tileHeight;
    /**
     * the name of the TileMap
     */
    public String name;

    public TileMap(int width, int height, int tileWidth, int tileHeight,
            String name, int nLayers) {
        tileLayers = new ArrayList<TileMapLayer>(nLayers);
        this.width = width;
        this.height = height;
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
        this.name = name;
        tileMapList.add(this);
        //tileMapList.put(name, this);
    }

    public void removeTileManager(TileMapLayer tileManager) {
        tileLayers.remove(tileManager);
    }

    public void removeTileManager(int index) {
        tileLayers.remove(index);
    }

    public void addTileLayer(TileMapLayer tileManager) {
        /*if (tileManager.getNumX() == width && tileManager.getNumY() == height && tileManager.getTileWidth() == tileWidth && tileManager.getTileHeight() == tileHeight) {
        tileLayers.add(tileManager);

        } else {
        Utility.error(
        "the TileManager that you're adding to the TileMap isn't correct",
        "TileMap.addTileManager()", new Exception(
        "TileManager isn't correct"));
        }
         */
        if (!tileLayers.contains(tileManager)) {
            tileLayers.add(tileManager);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (tileMapList.contains(this)) {
            tileMapList.remove(this);
        }
        this.name = name;
        tileMapList.add(this);
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public TileMapLayer getTileManager(int index) {
        return tileLayers.get(index);
    }

    public ArrayList<TileMapLayer> getTileManagers() {
        return tileLayers;
    }

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        /*s.defaultReadObject();
        if (!tileMapList.contains(this)) {
        tileMapList.add(this);
        }*/
        name = (String) s.readObject();
        readFromFile(Core.getSavingOperationPath() + name + "_tileMap");
        System.out.println("n.TileLayers: "+getTileManagers().size());
    }

    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.writeObject(name);
        writeOnFile(Core.getSavingOperationPath() + name + "_tileMap");
    }

    public void readFromFile(String file) {
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(new FileInputStream(file));
        } catch (IOException ex) {
            in = Utility.getLocalFile(file);
        }
        if (in == null) {
            return;
        }

        try {
            // read the version (at this time it's only 1)
			/* int version = */ in.readInt();

            width = in.readInt();
            height = in.readInt();
            tileWidth = in.readInt();
            tileHeight = in.readInt();
            name = (String) in.readObject();

            int size = in.readInt();
            /*TileMap tileMap = new TileMap(width, height, tileWidth, tileHeight,
                    name, size);*/

            tileLayers = new ArrayList<TileMapLayer>(size);
            TileMapLayer tileMapLayer;
            for (int i = 0; i < size; ++i) {
                //String str = (String) in.readObject();
                //tileMapLayer = new TileMapLayer( str, true, 0, null);
                //tileMapLayer = (TileMapLayer) in.readObject();
                String txt = (String)in.readObject();
                tileMapLayer = TileMapLayer.getTileMapLayer(txt);
                addTileLayer(tileMapLayer);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return;
    }

    public void writeOnFile(String fileName) {
        if (tileLayers == null || tileLayers.size() == 0) {
            Utility.error("tileMap is empty; What I should Write??",
                    new Exception("tileMap is Empty"));
            return;
        }
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeInt(1); // version
            // fileds
            out.writeInt(width);
            out.writeInt(height);
            out.writeInt(tileWidth);
            out.writeInt(tileHeight);
            out.writeObject(name);
            // tileLayers:
            // surely tileLayers exists and is not empty
            out.writeInt(tileLayers.size());

            for (int i = 0; i < tileLayers.size(); ++i) {
                out.writeObject(tileLayers.get(i).getName());
                // out.writeObject(tileLayers.get(i));
                //tileLayers.get(i).writeOnFile(tileLayers.get(i).getName() + "_layer");
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void finalize() throws Throwable {
        if (tileMapList.contains(this)) {
            tileMapList.remove(this);
        }
        super.finalize();
    }

    @Override
    public String toString() {
        //return super.toString();
        return getName();
    }
}
