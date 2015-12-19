/* EasyWay Game Engine
 * Copyright (C) 2007 Daniele Paggi.
 *  
 * Written by: 2007 Daniele Paggi<dshnt@hotmail.com>
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

package org.easyway.input;

import java.util.Vector;

public class Keyboard {

	public static int NUMBER_OF_KEYS = 300;

	/**
	 * indicates if we are using the LWJGL input method or the KeyEvent input
	 * method
	 */
	protected static boolean useLwjgl = true;// Core.useLwjgl;

	/**
	 * a buffer that records that keys are released
	 */
	protected static int bufferReleased[];
	
	protected static int bufferRIndex;
	
	protected static Vector<Integer> bufferDownKeys;

	/**
	 * indicates the state of keys
	 */
	protected static int keystate[];

	/**
	 * indicates if a key is down or not
	 */
	public synchronized static boolean isKeyDown(int key) {
		return keystate[key] == 2;
	}

	/**
	 * indicates if a key is up or not
	 */
	public synchronized static boolean isKeyUp(int key) {
		return keystate[key] == 0;
	}

	/**
	 * indicates if a key is released or not
	 */
	public synchronized static boolean isKeyReleased(int key) {
		return keystate[key] == 3;
	}

	/**
	 * indicates if a key is released or not
	 */
	public synchronized static boolean isKeyPressed(int key) {
		return keystate[key] == 1;
	}
	
	public static Vector<Integer> getDownKeys() {
		return bufferDownKeys;
	}
	
	/**
	 * returns the name of the key specified
	 * @param key the ID of the key (for example: Keyboard.KEY_A)
	 * @return the name of key
	 */
	public static String getKeyName(int key) {
		return org.lwjgl.input.Keyboard.getKeyName(key);
	}

	/**
	 * indexs<br>
	 * we implement these index as field to speed up the code:<br>
	 * we'll not need to allocate at any loop the space for the index variables.
	 */
	private static int i;

	/**
	 * updates the buffering keyboards
	 * 
	 */
	public synchronized static void loop() {
		for (i = 0; i < bufferRIndex; ++i) {
			// assert keystate[bufferReleased[i]] == 2;
			if (++keystate[bufferReleased[i]] == 4) {
				keystate[bufferReleased[i]] = 0;
			}
		}
		bufferRIndex = 0;
	}

	/**
	 * initialize the Keyboard
	 * 
	 */
	public static void create() {
		bufferReleased = new int[NUMBER_OF_KEYS];
		keystate = new int[NUMBER_OF_KEYS];
		bufferDownKeys = new Vector<Integer>(NUMBER_OF_KEYS/4);
		bufferRIndex = 0;
	}

	/**
	 * called when a new Keyboard event is occurred
	 * 
	 * @param key
	 */
	// public synchronized static void sendEvent(int key) {
	// buffer[bufferIndex] = key;
	// ++bufferIndex;
	// }
	/**
	 * called when a new Keyboard event is occurred
	 * 
	 * @param key
	 */
	public static void sendEvent(int key, boolean state) {
		if (state) { // down
			if (keystate[key] == 0) {
				bufferReleased[bufferRIndex] = key;
				bufferRIndex++;
				keystate[key] = 1;
				bufferDownKeys.add(key);
			} else {
				keystate[key] = 2;
			}
		} else {
			if (keystate[key] == 2) {
				bufferReleased[bufferRIndex] = key;
				bufferRIndex++;
				keystate[key] = 3;
				bufferDownKeys.remove((Integer)key);
			} else {
				keystate[key] = 0;
			}
		}
	}

	// /**
	// * sets if we're using the Lwjgl input method or the KeyEvent input method
	// *
	// * @param value
	// */
	// public static void setUseLwjgl(boolean value) {
	// useLwjgl = value;
	// }

	// -----------------------------------------------------------------------------
	// -----------------------------------------------------------------------------
	// -----------------------------------------------------------------------------

	/**
	 * The special keycode meaning that only the translated character is valid.
	 */
	public static final int KEY_NONE = 0x00;

	public static final int KEY_ESCAPE = 0x01;

	public static final int KEY_1 = 0x02;

	public static final int KEY_2 = 0x03;

	public static final int KEY_3 = 0x04;

	public static final int KEY_4 = 0x05;

	public static final int KEY_5 = 0x06;

	public static final int KEY_6 = 0x07;

	public static final int KEY_7 = 0x08;

	public static final int KEY_8 = 0x09;

	public static final int KEY_9 = 0x0A;

	public static final int KEY_0 = 0x0B;

	public static final int KEY_MINUS = 0x0C; /* - on main keyboard */

	public static final int KEY_EQUALS = 0x0D;

	public static final int KEY_BACK = 0x0E; /* backspace */

	public static final int KEY_TAB = 0x0F;

	public static final int KEY_Q = 0x10;

	public static final int KEY_W = 0x11;

	public static final int KEY_E = 0x12;

	public static final int KEY_R = 0x13;

	public static final int KEY_T = 0x14;

	public static final int KEY_Y = 0x15;

	public static final int KEY_U = 0x16;

	public static final int KEY_I = 0x17;

	public static final int KEY_O = 0x18;

	public static final int KEY_P = 0x19;

	public static final int KEY_LBRACKET = 0x1A;

	public static final int KEY_RBRACKET = 0x1B;

	public static final int KEY_RETURN = 0x1C; /* Enter on main keyboard */

	public static final int KEY_LCONTROL = 0x1D;

	public static final int KEY_A = 0x1E;

	public static final int KEY_S = 0x1F;

	public static final int KEY_D = 0x20;

	public static final int KEY_F = 0x21;

	public static final int KEY_G = 0x22;

	public static final int KEY_H = 0x23;

	public static final int KEY_J = 0x24;

	public static final int KEY_K = 0x25;

	public static final int KEY_L = 0x26;

	public static final int KEY_SEMICOLON = 0x27;

	public static final int KEY_APOSTROPHE = 0x28;

	public static final int KEY_GRAVE = 0x29; /* accent grave */

	public static final int KEY_LSHIFT = 0x2A;

	public static final int KEY_BACKSLASH = 0x2B;

	public static final int KEY_Z = 0x2C;

	public static final int KEY_X = 0x2D;

	public static final int KEY_C = 0x2E;

	public static final int KEY_V = 0x2F;

	public static final int KEY_B = 0x30;

	public static final int KEY_N = 0x31;

	public static final int KEY_M = 0x32;

	public static final int KEY_COMMA = 0x33;

	public static final int KEY_PERIOD = 0x34; /* . on main keyboard */

	public static final int KEY_SLASH = 0x35; /* / on main keyboard */

	public static final int KEY_RSHIFT = 0x36;

	public static final int KEY_MULTIPLY = 0x37; /* * on numeric keypad */

	public static final int KEY_LMENU = 0x38; /* left Alt */

	public static final int KEY_SPACE = 0x39;

	public static final int KEY_CAPITAL = 0x3A;

	public static final int KEY_F1 = 0x3B;

	public static final int KEY_F2 = 0x3C;

	public static final int KEY_F3 = 0x3D;

	public static final int KEY_F4 = 0x3E;

	public static final int KEY_F5 = 0x3F;

	public static final int KEY_F6 = 0x40;

	public static final int KEY_F7 = 0x41;

	public static final int KEY_F8 = 0x42;

	public static final int KEY_F9 = 0x43;

	public static final int KEY_F10 = 0x44;

	public static final int KEY_NUMLOCK = 0x45;

	public static final int KEY_SCROLL = 0x46; /* Scroll Lock */

	public static final int KEY_NUMPAD7 = 0x47;

	public static final int KEY_NUMPAD8 = 0x48;

	public static final int KEY_NUMPAD9 = 0x49;

	public static final int KEY_SUBTRACT = 0x4A; /* - on numeric keypad */

	public static final int KEY_NUMPAD4 = 0x4B;

	public static final int KEY_NUMPAD5 = 0x4C;

	public static final int KEY_NUMPAD6 = 0x4D;

	public static final int KEY_ADD = 0x4E; /* + on numeric keypad */

	public static final int KEY_NUMPAD1 = 0x4F;

	public static final int KEY_NUMPAD2 = 0x50;

	public static final int KEY_NUMPAD3 = 0x51;

	public static final int KEY_NUMPAD0 = 0x52;

	public static final int KEY_DECIMAL = 0x53; /* . on numeric keypad */

	public static final int KEY_F11 = 0x57;

	public static final int KEY_F12 = 0x58;

	public static final int KEY_F13 = 0x64; /* (NEC PC98) */

	public static final int KEY_F14 = 0x65; /* (NEC PC98) */

	public static final int KEY_F15 = 0x66; /* (NEC PC98) */

	public static final int KEY_KANA = 0x70; /* (Japanese keyboard) */

	public static final int KEY_CONVERT = 0x79; /* (Japanese keyboard) */

	public static final int KEY_NOCONVERT = 0x7B; /* (Japanese keyboard) */

	public static final int KEY_YEN = 0x7D; /* (Japanese keyboard) */

	public static final int KEY_NUMPADEQUALS = 0x8D; /*
														 * = on numeric keypad
														 * (NEC PC98)
														 */

	public static final int KEY_CIRCUMFLEX = 0x90; /* (Japanese keyboard) */

	public static final int KEY_AT = 0x91; /* (NEC PC98) */

	public static final int KEY_COLON = 0x92; /* (NEC PC98) */

	public static final int KEY_UNDERLINE = 0x93; /* (NEC PC98) */

	public static final int KEY_KANJI = 0x94; /* (Japanese keyboard) */

	public static final int KEY_STOP = 0x95; /* (NEC PC98) */

	public static final int KEY_AX = 0x96; /* (Japan AX) */

	public static final int KEY_UNLABELED = 0x97; /* (J3100) */

	public static final int KEY_NUMPADENTER = 0x9C; /* Enter on numeric keypad */

	public static final int KEY_RCONTROL = 0x9D;

	public static final int KEY_NUMPADCOMMA = 0xB3; /*
													 * , on numeric keypad (NEC
													 * PC98)
													 */

	public static final int KEY_DIVIDE = 0xB5; /* / on numeric keypad */

	public static final int KEY_SYSRQ = 0xB7;

	public static final int KEY_RMENU = 0xB8; /* right Alt */

	public static final int KEY_PAUSE = 0xC5; /* Pause */

	public static final int KEY_HOME = 0xC7; /* Home on arrow keypad */

	public static final int KEY_UP = 0xC8; /* UpArrow on arrow keypad */

	public static final int KEY_PRIOR = 0xC9; /* PgUp on arrow keypad */

	public static final int KEY_LEFT = 0xCB; /* LeftArrow on arrow keypad */

	public static final int KEY_RIGHT = 0xCD; /* RightArrow on arrow keypad */

	public static final int KEY_END = 0xCF; /* End on arrow keypad */

	public static final int KEY_DOWN = 0xD0; /* DownArrow on arrow keypad */

	public static final int KEY_NEXT = 0xD1; /* PgDn on arrow keypad */

	public static final int KEY_INSERT = 0xD2; /* Insert on arrow keypad */

	public static final int KEY_DELETE = 0xD3; /* Delete on arrow keypad */

	public static final int KEY_LWIN = 0xDB; /* Left Windows key */

	public static final int KEY_RWIN = 0xDC; /* Right Windows key */

	public static final int KEY_APPS = 0xDD; /* AppMenu key */

	public static final int KEY_POWER = 0xDE;

	public static final int KEY_SLEEP = 0xDF;
	/*
	 * public static final int KEY_0 = useLwjgl ? org.lwjgl.input.Keyboard.KEY_0 :
	 * java.awt.event.KeyEvent.VK_0;
	 * 
	 * public static final int KEY_1 = useLwjgl ? org.lwjgl.input.Keyboard.KEY_1 :
	 * java.awt.event.KeyEvent.VK_1;
	 * 
	 * public static final int KEY_2 = useLwjgl ? org.lwjgl.input.Keyboard.KEY_2 :
	 * java.awt.event.KeyEvent.VK_2;
	 * 
	 * public static final int KEY_3 = useLwjgl ? org.lwjgl.input.Keyboard.KEY_3 :
	 * java.awt.event.KeyEvent.VK_3;
	 * 
	 * public static final int KEY_4 = useLwjgl ? org.lwjgl.input.Keyboard.KEY_4 :
	 * java.awt.event.KeyEvent.VK_4;
	 * 
	 * public static final int KEY_5 = useLwjgl ? org.lwjgl.input.Keyboard.KEY_5 :
	 * java.awt.event.KeyEvent.VK_5;
	 * 
	 * public static final int KEY_6 = useLwjgl ? org.lwjgl.input.Keyboard.KEY_6 :
	 * java.awt.event.KeyEvent.VK_6;
	 * 
	 * public static final int KEY_7 = useLwjgl ? org.lwjgl.input.Keyboard.KEY_7 :
	 * java.awt.event.KeyEvent.VK_7;
	 * 
	 * public static final int KEY_8 = useLwjgl ? org.lwjgl.input.Keyboard.KEY_8 :
	 * java.awt.event.KeyEvent.VK_8;
	 * 
	 * public static final int KEY_9 = useLwjgl ? org.lwjgl.input.Keyboard.KEY_9 :
	 * java.awt.event.KeyEvent.VK_9;
	 *  // ok ok ?? public static final int KEY_APOSTROPHE = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_APOSTROPHE :
	 * java.awt.event.KeyEvent.VK_QUOTE;
	 * 
	 * public static final int KEY_TAB = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_TAB : java.awt.event.KeyEvent.VK_TAB;
	 * 
	 * public static final int KEY_Q = useLwjgl ? org.lwjgl.input.Keyboard.KEY_Q :
	 * java.awt.event.KeyEvent.VK_Q;
	 * 
	 * public static final int KEY_W = useLwjgl ? org.lwjgl.input.Keyboard.KEY_W :
	 * java.awt.event.KeyEvent.VK_W;
	 * 
	 * public static final int KEY_E = useLwjgl ? org.lwjgl.input.Keyboard.KEY_E :
	 * java.awt.event.KeyEvent.VK_E;
	 * 
	 * public static final int KEY_R = useLwjgl ? org.lwjgl.input.Keyboard.KEY_R :
	 * java.awt.event.KeyEvent.VK_R;
	 * 
	 * public static final int KEY_T = useLwjgl ? org.lwjgl.input.Keyboard.KEY_T :
	 * java.awt.event.KeyEvent.VK_T;
	 * 
	 * public static final int KEY_Y = useLwjgl ? org.lwjgl.input.Keyboard.KEY_Y :
	 * java.awt.event.KeyEvent.VK_Y;
	 * 
	 * public static final int KEY_U = useLwjgl ? org.lwjgl.input.Keyboard.KEY_U :
	 * java.awt.event.KeyEvent.VK_U;
	 * 
	 * public static final int KEY_I = useLwjgl ? org.lwjgl.input.Keyboard.KEY_I :
	 * java.awt.event.KeyEvent.VK_I;
	 * 
	 * public static final int KEY_O = useLwjgl ? org.lwjgl.input.Keyboard.KEY_O :
	 * java.awt.event.KeyEvent.VK_O;
	 * 
	 * public static final int KEY_P = useLwjgl ? org.lwjgl.input.Keyboard.KEY_P :
	 * java.awt.event.KeyEvent.VK_P;
	 *  // ok ?ok? ok <we should test> public static final int kEY_CAPSLOCK =
	 * useLwjgl ? org.lwjgl.input.Keyboard.KEY_CAPITAL :
	 * java.awt.event.KeyEvent.VK_CAPS_LOCK;
	 * 
	 * public static final int KEY_A = useLwjgl ? org.lwjgl.input.Keyboard.KEY_A :
	 * java.awt.event.KeyEvent.VK_A;
	 * 
	 * public static final int KEY_S = useLwjgl ? org.lwjgl.input.Keyboard.KEY_S :
	 * java.awt.event.KeyEvent.VK_S;
	 * 
	 * public static final int KEY_D = useLwjgl ? org.lwjgl.input.Keyboard.KEY_D :
	 * java.awt.event.KeyEvent.VK_D;
	 * 
	 * public static final int KEY_F = useLwjgl ? org.lwjgl.input.Keyboard.KEY_F :
	 * java.awt.event.KeyEvent.VK_F;
	 * 
	 * public static final int KEY_G = useLwjgl ? org.lwjgl.input.Keyboard.KEY_G :
	 * java.awt.event.KeyEvent.VK_G;
	 * 
	 * public static final int KEY_H = useLwjgl ? org.lwjgl.input.Keyboard.KEY_H :
	 * java.awt.event.KeyEvent.VK_H;
	 * 
	 * public static final int KEY_J = useLwjgl ? org.lwjgl.input.Keyboard.KEY_J :
	 * java.awt.event.KeyEvent.VK_J;
	 * 
	 * public static final int KEY_K = useLwjgl ? org.lwjgl.input.Keyboard.KEY_K :
	 * java.awt.event.KeyEvent.VK_K;
	 *  // with KeyEvent we should solve the Right and Left Shift o.o public
	 * static final int KEY_LSHIFT = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_LSHIFT : java.awt.event.KeyEvent.VK_SHIFT;
	 * 
	 * public static final int KEY_RSHIFT = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_RSHIFT : java.awt.event.KeyEvent.VK_SHIFT;
	 * 
	 * public static final int KEY_Z = useLwjgl ? org.lwjgl.input.Keyboard.KEY_Z :
	 * java.awt.event.KeyEvent.VK_Z;
	 * 
	 * public static final int KEY_X = useLwjgl ? org.lwjgl.input.Keyboard.KEY_X :
	 * java.awt.event.KeyEvent.VK_X;
	 * 
	 * public static final int KEY_C = useLwjgl ? org.lwjgl.input.Keyboard.KEY_C :
	 * java.awt.event.KeyEvent.VK_C;
	 * 
	 * public static final int KEY_V = useLwjgl ? org.lwjgl.input.Keyboard.KEY_V :
	 * java.awt.event.KeyEvent.VK_V;
	 * 
	 * public static final int KEY_B = useLwjgl ? org.lwjgl.input.Keyboard.KEY_B :
	 * java.awt.event.KeyEvent.VK_B;
	 * 
	 * public static final int KEY_N = useLwjgl ? org.lwjgl.input.Keyboard.KEY_N :
	 * java.awt.event.KeyEvent.VK_N;
	 * 
	 * public static final int KEY_M = useLwjgl ? org.lwjgl.input.Keyboard.KEY_M :
	 * java.awt.event.KeyEvent.VK_M;
	 * 
	 * public static final int KEY_BACK = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_BACK :
	 * java.awt.event.KeyEvent.VK_BACK_SPACE;
	 * 
	 * public static final int KEY_ESCAPE = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_ESCAPE : java.awt.event.KeyEvent.VK_ESCAPE;
	 * 
	 * public static final int KEY_F1 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_F1 : java.awt.event.KeyEvent.VK_F1;
	 * 
	 * public static final int KEY_F2 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_F2 : java.awt.event.KeyEvent.VK_F2;
	 * 
	 * public static final int KEY_F3 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_F3 : java.awt.event.KeyEvent.VK_F3;
	 * 
	 * public static final int KEY_F4 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_F4 : java.awt.event.KeyEvent.VK_F4;
	 * 
	 * public static final int KEY_F5 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_F5 : java.awt.event.KeyEvent.VK_F5;
	 * 
	 * public static final int KEY_F6 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_F6 : java.awt.event.KeyEvent.VK_F6;
	 * 
	 * public static final int KEY_F7 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_F7 : java.awt.event.KeyEvent.VK_F7;
	 * 
	 * public static final int KEY_F8 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_F8 : java.awt.event.KeyEvent.VK_F8;
	 * 
	 * public static final int KEY_F9 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_F9 : java.awt.event.KeyEvent.VK_F9;
	 * 
	 * public static final int KEY_F10 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_F10 : java.awt.event.KeyEvent.VK_F10;
	 * 
	 * public static final int KEY_F11 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_F11 : java.awt.event.KeyEvent.VK_F11;
	 * 
	 * public static final int KEY_F12 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_F12 : java.awt.event.KeyEvent.VK_F12;
	 * 
	 * public static final int KEY_F13 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_F13 : java.awt.event.KeyEvent.VK_F13;
	 * 
	 * public static final int KEY_F14 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_F14 : java.awt.event.KeyEvent.VK_F14;
	 * 
	 * public static final int KEY_F15 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_F15 : java.awt.event.KeyEvent.VK_F15;
	 *  // with KeyEvent we should solve the Right and Left Ctrl o.o public
	 * static final int KEY_LCTRL = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_LCONTROL :
	 * java.awt.event.KeyEvent.VK_CONTROL;
	 * 
	 * public static final int KEY_RCTRL = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_RCONTROL :
	 * java.awt.event.KeyEvent.VK_CONTROL;
	 *  // with KeyEvent we should solve the Right and Left WinLogo o.o public
	 * static final int KEY_LWIN = useLwjgl ? org.lwjgl.input.Keyboard.KEY_LWIN :
	 * java.awt.event.KeyEvent.VK_WINDOWS;
	 * 
	 * public static final int KEY_RWIN = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_RWIN : java.awt.event.KeyEvent.VK_WINDOWS;
	 *  // with KeyEvent we should solve the Right and Left Alt o.o public
	 * static final int kEY_LALT = useLwjgl ? org.lwjgl.input.Keyboard.KEY_LMENU :
	 * java.awt.event.KeyEvent.VK_ALT;
	 * 
	 * public static final int kEY_RALT = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_RMENU : java.awt.event.KeyEvent.VK_ALT;
	 * 
	 * public static final int KEY_SPACE = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_SPACE : java.awt.event.KeyEvent.VK_SPACE;
	 *  // public static final int KEY_ALTGR = useLwjgl ? //
	 * org.lwjgl.input.Keyboard.KEY_: java.awt.event.KeyEvent.VK_SPACE; //
	 * public static final int KEY_MENU = useLwjgl ? //
	 * org.lwjgl.input.Keyboard.KEY_RMENU: java.awt.event.KeyEvent.VK_R;
	 * 
	 * public static final int KEY_UP = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_UP : java.awt.event.KeyEvent.VK_UP;
	 * 
	 * public static final int KEY_RIGHT = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_RIGHT : java.awt.event.KeyEvent.VK_RIGHT;
	 * 
	 * public static final int KEY_DOWN = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_DOWN : java.awt.event.KeyEvent.VK_DOWN;
	 * 
	 * public static final int KEY_LEFT = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_LEFT : java.awt.event.KeyEvent.VK_LEFT;
	 * 
	 * public static final int KEY_MINUS = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_MINUS : java.awt.event.KeyEvent.VK_MINUS;
	 * 
	 * public static final int KEY_ADD = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_ADD : java.awt.event.KeyEvent.VK_ADD;
	 * 
	 * public static final int KEY_MULTIPLY = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_MULTIPLY :
	 * java.awt.event.KeyEvent.VK_MULTIPLY;
	 * 
	 * public static final int KEY_DIVIDE = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_DIVIDE : java.awt.event.KeyEvent.VK_DIVIDE;
	 * 
	 * public static final int KEY_DELETE = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_DELETE : java.awt.event.KeyEvent.VK_DELETE;
	 * 
	 * public static final int KEY_DECIMAL = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_DECIMAL :
	 * java.awt.event.KeyEvent.VK_DECIMAL;
	 * 
	 * public static final int KEY_EQUALS = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_EQUALS : java.awt.event.KeyEvent.VK_EQUALS;
	 * 
	 * public static final int KEY_OPEN_BARCKET = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_LBRACKET :
	 * java.awt.event.KeyEvent.VK_OPEN_BRACKET;
	 * 
	 * public static final int KEY_CLOSE_BARCKET = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_RBRACKET :
	 * java.awt.event.KeyEvent.VK_CLOSE_BRACKET;
	 * 
	 * public static final int KEY_SEMICOLON = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_SEMICOLON :
	 * java.awt.event.KeyEvent.VK_SEMICOLON;
	 * 
	 * public static final int KEY_GRAVE = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_GRAVE :
	 * java.awt.event.KeyEvent.VK_DEAD_GRAVE;
	 * 
	 * public static final int KEY_BACKSLASH = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_BACKSLASH :
	 * java.awt.event.KeyEvent.VK_BACK_SLASH;
	 * 
	 * public static final int KEY_COMMA = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_COMMA : java.awt.event.KeyEvent.VK_COMMA;
	 * 
	 * public static final int KEY_PERIOD = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_PERIOD : java.awt.event.KeyEvent.VK_PERIOD;
	 * 
	 * public static final int KEY_SLASH = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_SLASH : java.awt.event.KeyEvent.VK_SLASH;
	 * 
	 * public static final int KEY_NUMLOCK = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_NUMLOCK :
	 * java.awt.event.KeyEvent.VK_NUM_LOCK;
	 * 
	 * public static final int KEY_SCROLL = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_SCROLL :
	 * java.awt.event.KeyEvent.VK_SCROLL_LOCK;
	 * 
	 * public static final int KEY_NUMPAD0 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_NUMPAD0 :
	 * java.awt.event.KeyEvent.VK_NUMPAD0;
	 * 
	 * public static final int KEY_NUMPAD1 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_NUMPAD1 :
	 * java.awt.event.KeyEvent.VK_NUMPAD1;
	 * 
	 * public static final int KEY_NUMPAD2 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_NUMPAD2 :
	 * java.awt.event.KeyEvent.VK_NUMPAD2;
	 * 
	 * public static final int KEY_NUMPAD3 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_NUMPAD3 :
	 * java.awt.event.KeyEvent.VK_NUMPAD3;
	 * 
	 * public static final int KEY_NUMPAD4 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_NUMPAD4 :
	 * java.awt.event.KeyEvent.VK_NUMPAD4;
	 * 
	 * public static final int KEY_NUMPAD5 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_NUMPAD5 :
	 * java.awt.event.KeyEvent.VK_NUMPAD5;
	 * 
	 * public static final int KEY_NUMPAD6 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_NUMPAD6 :
	 * java.awt.event.KeyEvent.VK_NUMPAD6;
	 * 
	 * public static final int KEY_NUMPAD7 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_NUMPAD7 :
	 * java.awt.event.KeyEvent.VK_NUMPAD7;
	 * 
	 * public static final int KEY_NUMPAD8 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_NUMPAD8 :
	 * java.awt.event.KeyEvent.VK_NUMPAD8;
	 * 
	 * public static final int KEY_NUMPAD9 = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_NUMPAD9 :
	 * java.awt.event.KeyEvent.VK_NUMPAD9;
	 * 
	 * public static final int KEY_KANA = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_KANA : java.awt.event.KeyEvent.VK_KANA;
	 * 
	 * public static final int KEY_CONVERT = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_CONVERT :
	 * java.awt.event.KeyEvent.VK_CONVERT;
	 * 
	 * public static final int KEY_NOCONVERT = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_NOCONVERT :
	 * java.awt.event.KeyEvent.VK_NONCONVERT;
	 * 
	 * public static final int KEY_CIRCUMFLEX = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_CIRCUMFLEX :
	 * java.awt.event.KeyEvent.VK_CIRCUMFLEX;
	 * 
	 * public static final int KEY_COLON = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_COLON : java.awt.event.KeyEvent.VK_COLON;
	 * 
	 * public static final int KEY_UNDERSCORE = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_UNDERLINE :
	 * java.awt.event.KeyEvent.VK_UNDERSCORE;
	 * 
	 * public static final int KEY_KANJI = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_KANJI : java.awt.event.KeyEvent.VK_KANJI;
	 * 
	 * public static final int KEY_STOP = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_STOP : java.awt.event.KeyEvent.VK_STOP;
	 * 
	 * public static final int KEY_PAUSE = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_PAUSE : java.awt.event.KeyEvent.VK_PAUSE;
	 * 
	 * public static final int KEY_HOME = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_HOME : java.awt.event.KeyEvent.VK_HOME;
	 * 
	 * public static final int KEY_PAGEUP = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_PRIOR : java.awt.event.KeyEvent.VK_PAGE_UP;
	 * 
	 * public static final int KEY_PAGEDOWN = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_NEXT : java.awt.event.KeyEvent.VK_PAGE_DOWN;
	 * 
	 * public static final int KEY_INSERT = useLwjgl ?
	 * org.lwjgl.input.Keyboard.KEY_INSERT : java.awt.event.KeyEvent.VK_INSERT;
	 */
	// public static final int KEY_PAGEUP = useLwjgl ?
	// org.lwjgl.input.Keyboard.KEY_LWIN
	// : java.awt.event.KeyEvent.VK_META;
}
