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

import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.objects.sprites2D.SpriteColl;

public class TileIterator {
	/**
	 * current index in the list
	 */
	private int indexScan = -1;

	/** lists of all tiles */
	Tile tiles[];

	/** coordinates of all tiles */
	float x[];

	/** coordinates of all tiles */
	float y[];

	/**
	 * reference to the TileManager
	 */
	TileMapLayer tileManager;

	/**
	 * creates a new instance of TileManager
	 * 
	 * @param tileManager
	 *            the TileManager to iterate
	 * @param plain
	 *            the collision plain
	 */
	public TileIterator(TileMapLayer tiledMap, IPlain2D plain) {
		this.tileManager = tiledMap;

		int xStartPos = ((int) (plain.getX() - tiledMap.getX()))
				/ tiledMap.getTileWidth();

		if (xStartPos < 0)
			xStartPos = 0;

		else if (xStartPos >= tiledMap.getNumX())
			return;

		int xEndPos = ((int) (plain.getX() + plain.getWidth() - tiledMap
				.getX()))
				/ tiledMap.getTileWidth();
		if (xEndPos >= tiledMap.getNumX())
			xEndPos = tiledMap.getNumX() - 1;
		else if (xStartPos < 0)
			return;

		int yStartPos = ((int) (plain.getY() - tiledMap.getY()))
				/ tiledMap.getTileHeight();
		if (yStartPos < 0)
			yStartPos = 0;
		else if (yStartPos >= tiledMap.getNumY())
			return;

		int yEndPos = ((int) (plain.getY() + plain.getHeight() - tiledMap
				.getY()))
				/ tiledMap.getTileHeight();
		if (yEndPos >= tiledMap.getNumY())
			yEndPos = tiledMap.getNumY() - 1;
		else if (yStartPos < 0)
			return;
		// System.out.println("XSTART: " + xStartPos + " XEND " + xEndPos);
		// System.out.println("YSTART: " + yStartPos + " YEND " + yEndPos);
		{
			int tot = (xEndPos - xStartPos + 1) * (yEndPos - yStartPos + 1);
			assert tot > 0 : "Errore nel TileIterator";
			tiles = new Tile[tot];
			x = new float[tot];
			y = new float[tot];
		}

		int xindex, yindex;
		Tile tempTile;
		indexScan = -1;
		for (yindex = yStartPos; yindex <= yEndPos; ++yindex) {
			for (xindex = xStartPos; xindex <= xEndPos; ++xindex) {
				if ((tempTile = tiledMap.grid[xindex][yindex]) == null) {
					continue;
				}
				if (tempTile.image == null)
					continue;
				++indexScan;
				tiles[indexScan] = tempTile;
				this.x[indexScan] = xindex * tiledMap.getTileWidth()
						+ tiledMap.getX();
				this.y[indexScan] = yindex * tiledMap.getTileHeight()
						+ tiledMap.getY();
			}
		}
		// System.out.println("INDEX = >>>> "+ indexScan);
		++indexScan;
	}

	/**
	 * has anymore elements to iterate?
	 * 
	 * @return returns true if has still elements
	 */
	public boolean hasElement() {
		--indexScan;
		if (indexScan >= 0)
			return true;
		return false;
	}

	/**
	 * get the current element
	 * 
	 * @return the current element
	 */
	public SpriteColl getCurrent() {
		TileSprite sp = new TileSprite(x[indexScan], y[indexScan], tileManager,
				tiles[indexScan]);
		return sp;
	}

}