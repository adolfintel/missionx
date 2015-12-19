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

package org.easyway.utils.loaderdata;

/**
 * A static list of Chunks that are used by 3DS file format.
 * 
 * @author Daniele Paggi
 * 
 */
public class Chunks {
	// headers
	/** 4d4d */
	public static final int MAIN = 0x4D4D;

	/** 3d3d */
	public static final int EDITOR = 0x3D3D;

	// colors?
	/** 010 */
	public static final int COLOR_RGBF = 0x010;

	/** 011 */
	public static final int COLOR_24 = 0x011;

	/** 012 */
	public static final int LIN_COLOR_24 = 0x012;

	/** 030 */
	public static final int SHORT_PERTENTAGE = 0x030;

	/** 013 */
	public static final int COLOR_UNK = 0x013;

	/** 100 */
	public static final int MASTER_SCALE = 0x100;

	// objects
	/** 4000 object and name */
	public static final int OBJ = 0x4000;

	/** 4100 new object */
	public static final int OBJ_MESH = 0x4100;

	/** 4110 vertex list */
	public static final int OBJ_MESH_VERTICELIST = 0x4110;

	/** 4120 faces list */
	public static final int OBJ_MESH_FACELIST = 0x4120;

	/** 4130 materials */
	public static final int OBJ_MESH_FACEMAT = 0x4130;

	/** 4140 coordinates u, v */
	public static final int OBJ_MESH_MAPCOORDLIST = 0x4140;

	/** 4150 */
	public static final int OBJ_MESH_SMOOTHLIST = 0x4150;

	/** 4160 */
	public static final int OBJ_MESH_LOCALCOORDS = 0x4160;

	/** 4600 */
	public static final int OBJ_LIGHT = 0x4600;

	/** 4610 */
	public static final int OBJ_LIGHT_SPOTLIGHT = 0x4610;

	// camera
	/** 4700 camera */
	public static final int OBJ_CAMERA = 0x4700;

	// materiale
	/** AFFF */
	public static final int MAT = 0xAFFF;

	/** A000 */
	public static final int MAT_NAME = 0xA000;

	/** A010 */
	public static final int MAT_AMBIENT = 0xA010;

	/** A020 */
	public static final int MAT_DIFFUSE = 0xa020;

	/** A030 */
	public static final int MAT_SPECULAR = 0xA030;

	/** A040 */
	public static final int MAT_SHININESS = 0xA040;

	/** A050 */
	public static final int MAT_TRANSPARENCY = 0xA050;

	/** A200 */
	public static final int MAT_MAP_TEXTURE1 = 0xA200;

	/** A230 */
	public static final int MAT_MAP_BUMP = 0xa230;

	/** A220 */
	public static final int MAT_MAP_REFLECTION = 0xa220;

	/** A300 */
	public static final int MAT_MAP_FILENAME = 0xa300;

	/** A351 */
	public static final int MAT_MAP_PARAMETERS = 0xa351;

	// animazioni
	/** B000 */
	public static final int KEY_FRAME_DATA = 0xb000;

	/** B00A */
	public static final int KEY_FRAME_HDR = 0xb00a;

	/** B008 */
	public static final int KEY_FRAME_SEG = 0xb008;

	/** B009 */
	public static final int KEY_FRAME_CURTIME = 0xb009;

	/** B002 */
	public static final int OBJECT_NODE_TAG = 0xb002;

	// /** 05 */
	// public static final int M3D_KEY_FRAME_VERSION = 0x5;

}
