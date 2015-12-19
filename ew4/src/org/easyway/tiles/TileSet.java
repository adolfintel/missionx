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
import java.io.Serializable;
import java.util.ArrayList;

import org.easyway.system.Core;
import org.easyway.system.state.GameState;
import org.easyway.utils.Utility;

/**
 * 
 * @author Daniele Paggi
 * 
 */
public class TileSet implements Serializable {

    /**
     * generated version id
     */
    private static final long serialVersionUID = 790915426908315894L;
    /** list of all the tileSet loaded */
    protected static ArrayList<TileSet> tileSetList = new ArrayList<TileSet>(5);
    /**
     * the list of tiles
     */
    protected volatile ArrayList<Tile> tileList;
    /**
     * the file name
     */
    protected volatile String name;

    public TileSet(int numberTiles, String name) {
        tileList = new ArrayList<Tile>(numberTiles);
        this.name = name;
        tileSetList.add(this);
    }

    public TileSet(String name) {
        tileList = new ArrayList<Tile>(255);
        this.name = name;
        tileSetList.add(this);
    }

    public TileSet(ArrayList<Tile> tiles, String name) {
        tileList = tiles;
        this.name = name;
        tileSetList.add(this);
    }

    // ------------------
    /**
     * returns all the tile in the memory.
     * @return a list filled with all the tileset in the memory.
     */
    public static ArrayList<TileSet> getAllTileSet() {
        return tileSetList;
    }

    /**
     * returns the number of tiles contained in the tileset
     * @return the number of tiles contained in the tileset
     */
    public int getSize() {
        return tileList.size();
    }

    /**
     * return the tileList
     *
     * @return the tileList
     */
    public ArrayList<Tile> getTileList() {
        return tileList;
    }

    /**
     * changes the tileList
     *
     * @param tileList
     *            the new tileList to set
     */
    public void setTileList(ArrayList<Tile> tileList) {
        this.tileList = tileList;
    }

    /**
     * returns the tile number 'index'
     *
     * @param index
     *            the index of the tile to get
     * @return the tile or null if the index is outofbounds
     */
    public Tile get(int index) {
        if (index < 0 || index >= tileList.size()) {
            return null;
        }
        return tileList.get(index);
    }

    public int getIndex(Tile tile) {
        return tileList.indexOf(tile);
    }

    public void add(Tile tile) {
        tileList.add(tile);
    }

    public void add(Tile tile, int index) {
        tileList.add(index, tile);
    }

    public void remove(Tile tile) {
        if (tileList.contains(tile)) {
            tileList.remove(tile);
        }
    }

    /*private void readObject(java.io.ObjectInputStream s)
    throws java.io.IOException, ClassNotFoundException {
    s.defaultReadObject();
    for (int i = 0; i < tileList.size(); ++i) {
    tileList.get(i).id = i;
    }
    }*/
    public String getName() {
        return name;
    }

    public void setName(String pathFilename) {
        this.name = pathFilename;
    }

    public void destroy() {
        if (tileSetList.contains(this)) {
            tileSetList.remove(this);
        }
        for (TileMapLayer tm : GameState.getCurrentGEState().getCoreCollision().tileGroups) {
            if (tm.getTileSet() == this) {
                tm.setTileSet(null);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (tileSetList.contains(this)) {
            tileSetList.remove(this);
        }
        super.finalize();
    }

    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {
        //in.defaultReadObject();
        name = (String) in.readObject();
        readFromFile(Core.getSavingOperationPath() + name + "_tileSet");
        if (!tileSetList.contains(this)) {
            tileSetList.add(this);
        }
        System.out.println("TileSetName: " + name);
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws java.io.IOException, ClassNotFoundException {
        /*s.defaultWriteObject();*/
        out.writeObject(name);
        System.out.println("TileSetName: " + name);
        writeOnFile(Core.getSavingOperationPath() + name + "_tileSet");
        //new RuntimeException().printStackTrace();
    }

    public void readFromFile(String file) throws ClassNotFoundException {
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
            int version = in.readInt();

            //int size = in.readInt();
            //TileSet tileSet = new TileSet(size, file);
            tileList = (ArrayList<Tile>) in.readObject();//new ArrayList<Tile>(size);

            /*Tile tile;
            for (int i = 0; i < size; ++i) {
            System.out.println("loading: " + i);
            tile = (Tile) in.readObject();//Tile.readFromFile(in);
            tile.id = i;
            add(tile);
            }*/

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //new RuntimeException().printStackTrace();
    }

    public void writeOnFile(String filename) {
        //name = filename;
        if (tileList == null || tileList.size() == 0) {
            Utility.error("tileSet is empty; What I should Write??",
                    new Exception("tileSet is Empty"));
            return;
        }

        try {
            FileOutputStream fout = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fout);

            // the version-id
            out.writeInt(1);
            out.writeObject(tileList);

            /*out.writeInt(tileList.size());
            for (int i = 0; i < tileList.size(); ++i) {
            //tileList.get(i).writeOnFile(out);
            out.writeObject(tileList.get(i));
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
