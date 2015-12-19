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
package org.easyway.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PushbackInputStream;
import java.util.Vector;

import org.easyway.interfaces.base.ITexture;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

/**
 * 
 * General Utility; <br>
 * used to load textures and prints easyway messages at viedo.
 * 
 * @author Daniele Paggi
 */
public final class Utility {

	// ----------------------------------------------------------------------
	// -------------------------ERRORS AND MESSAGES--------------------------
	// ----------------------------------------------------------------------
	/** indicates if show messages or not */
	private static boolean messagesEnabled = true;

	/** indicats how messages shows and how not */
	private static Vector<String> messIgnoreList = new Vector<String>();

	/** the last error that is occurred */
	private static String lastError;

	/** shows an error on screen */
	public static void error(String error, String type, Exception e) {
		if (!messagesEnabled)
			return;
		if (messIgnoreList.contains(type) && error != null && type != null)
			return;
		if (error == null || error.length() == 0) {
			error = "EXCEPTION!";
		} else if (type != null) {
			if (type.charAt(0) == '$') {
				Sys.alert(error, type.substring(1));
			} else if (type.charAt(0) == '&') {

			}
		}
		lastError = error;
		System.out.println(" - CORE MESSAGE: " + error + " (" + type + ")");
		if (e != null)
			e.printStackTrace();
	}

	/** shows an error on screen */
	public static void error(String type, Exception e) {
		error(null, type, e);
	}

	/** shows an error/message on screen */
	public static void error(String message, String type) {
		error(message, type, null);
	}

	/** sets if shows or not the errors/messages */
	public static void showCoreMessages(boolean value) {
		messagesEnabled = value;
	}

	/** ignores an error message */
	public static void ignoreError(String error) {
		if (!messIgnoreList.contains(error))
			messIgnoreList.add(error);
		else
			error("OPTIMIZATION ignoreError(String) in Utility : "
					+ "error alreay ignored", "Utility.ignoreError(String)");
	}

	/** allows an error message */
	public static void allowsError(String error) {
		if (messIgnoreList.contains(error))
			messIgnoreList.remove(error);
		else
			error("allowsError(String) in Utility : error isn't ignored",
					"Utility.allowsError");
	}

	/** allows all errors */
	public static final void allowsAllError() {
		messIgnoreList.removeAllElements();
		messagesEnabled = true;
	}

	/** returns the last error that is occurred */
	public static String getLastError() {
		return lastError;
	}

	/**
	 * reads a line from an inputstream
	 * 
	 * @param dis
	 *            DataInputStream
	 * @return line readed
	 */
	public static final String readLine(InputStream in) throws IOException {
		char lineBuffer[];
		char buf[];

		buf = lineBuffer = new char[128];

		int room = buf.length;
		int offset = 0;
		int c;

		loop: while (true) {
			switch (c = in.read()) {
			case -1:
			case '\n':
				break loop;

			case '\r':
				int c2 = in.read();
				if ((c2 != '\n') && (c2 != -1)) {
					if (!(in instanceof PushbackInputStream)) {
						in = new PushbackInputStream(in);
					}
					((PushbackInputStream) in).unread(c2);
				}
				break loop;

			default:
				if (--room < 0) {
					buf = new char[offset + 128];
					room = buf.length - offset - 1;
					System.arraycopy(lineBuffer, 0, buf, 0, offset);
					lineBuffer = buf;
				}
				buf[offset++] = (char) c;
				break;
			}
		}
		if ((c == -1) && (offset == 0)) {
			return null;
		}
		return String.copyValueOf(buf, 0, offset);
	}

	/** draws a quad on screen */
	public static void drawQuad(float x, float y, float width, float height,
			ITexture image) {
		image.bind();
		GL11.glColor3f(1, 1, 1);
		GL11.glBegin(GL11.GL_QUADS); // Draw A Quad
		GL11.glTexCoord2f(image.getXStart(), image.getYStart());
		GL11.glVertex2f(x, y);
		GL11.glTexCoord2f(image.getXEnd(), image.getYStart());
		GL11.glVertex2f(x + width, y);
		GL11.glTexCoord2f(image.getXEnd(), image.getYEnd());
		GL11.glVertex2f(x + width, y + height);
		GL11.glTexCoord2f(image.getXStart(), image.getYEnd());
		GL11.glVertex2f(x, y + height);
		GL11.glEnd();
	}

	/**
	 * sets the Standard Blending function.<br>
	 * for "experts" it's: <i>glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);</i>
	 */
	public static void setStandardBlendingMode() {
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void drawRect(float x, float y, float width, float height,
			float red, float green, float blue) {
		OpenGLState.disableAlphaTest();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(red, green, blue);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x + width, y);
		GL11.glVertex2f(x + width, y + height);
		GL11.glVertex2f(x, y + height);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		OpenGLState.enableAlphaTest();
	}

	public static ObjectInputStream getLocalFile(String path) {

		InputStream is = null;
		{
			if (path.startsWith("/"))
				path = path.substring(1);
			int index;
			// path = path.replaceAll("\\", "/");
			while ((index = path.indexOf("\\")) != -1)
				path = path.substring(0, index) + '/'
						+ path.substring(index + 1);
			try {
				is = Thread.currentThread().getContextClassLoader()
						.getResource(path).openStream();
			} catch (Exception e) {
				Utility.error("File " + path + " was not found!", e);
			}
		}

		ObjectInputStream in; // stream for objects to the file
		try {
			in = new ObjectInputStream(is);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return in;
	}

}
