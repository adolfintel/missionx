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
package org.easyway.shader;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.easyway.utils.Utility;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.Util;

/** ALPHA */
@Deprecated
public class Shaders {

	int program;

	public Shaders(String filename) {
		//ByteBuffer vdata = getProgramCode("v"+filename);
		ByteBuffer fdata = getProgramCode("f"+filename);
		// crea un nuovo "id"

		int vertexShader = ARBShaderObjects
				.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
		int fragmentShader = ARBShaderObjects
				.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		// attracca il codice
		//ARBShaderObjects.glShaderSourceARB(vertexShader, vdata);
		ARBShaderObjects.glShaderSourceARB(fragmentShader, fdata);
		// compila il codice
		ARBShaderObjects.glCompileShaderARB(vertexShader);
		ARBShaderObjects.glCompileShaderARB(fragmentShader);

		// crea il programma
		int programObject = ARBShaderObjects.glCreateProgramObjectARB();
		// attracca il codice
		ARBShaderObjects.glAttachObjectARB(programObject, vertexShader);
		ARBShaderObjects.glAttachObjectARB(programObject, fragmentShader);
		// linka il programma
		ARBShaderObjects.glLinkProgramARB(programObject);
		// controlla la correttezza del codice
		ARBShaderObjects.glValidateProgramARB(programObject);
		// visualizza il log
		printLogInfo(programObject);

		// usa il codice
		// ARBShaderObjects.glUseProgramObjectARB(programObject);
		ARBShaderObjects.glUseProgramObjectARB(0);
		program = programObject;
	}
	
	public void setUniform(String name, int value) {
		ByteBuffer buff = toByteString(name, true);
		int location = ARBShaderObjects
				.glGetUniformLocationARB(program, buff);
		ARBShaderObjects.glUniform1iARB(location, value);
		
	}
	
	public void setUniform(String name, IntBuffer value ) {
		ByteBuffer buff = toByteString(name, true);
		int location = ARBShaderObjects
				.glGetUniformLocationARB(program, buff);
		ARBShaderObjects.glUniform1ARB(location, value);
	}

	public void setUniform(String name, float value) {
		ByteBuffer buff = toByteString(name, true);
		int location = ARBShaderObjects
				.glGetUniformLocationARB(program, buff);
		ARBShaderObjects.glUniform1fARB(location, value);
	}

	public void use() {
		ARBShaderObjects.glUseProgramObjectARB(program);
	}

	public static void reset() {
		ARBShaderObjects.glUseProgramObjectARB(0);
	}

	private static void printLogInfo(int obj) {
		IntBuffer iVal = BufferUtils.createIntBuffer(1);
		ARBShaderObjects.glGetObjectParameterARB(obj,
				ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);

		int length = iVal.get();
		System.out.println("Info log length:" + length);
		if (length > 0) {
			// We have some info we need to output.
			ByteBuffer infoLog = BufferUtils.createByteBuffer(length);
			iVal.flip();
			ARBShaderObjects.glGetInfoLogARB(obj, iVal, infoLog);
			byte[] infoBytes = new byte[length];
			infoLog.get(infoBytes);
			String out = new String(infoBytes);

			System.out.println("Info log:\n" + out);
		}
		Util.checkGLError();
		// LWJGLUtil.checkGLError();
	}

	private static ByteBuffer getProgramCode(String filename) {
		// ClassLoader fileLoader = Shaders.class.getClassLoader();

		InputStream fileInputStream;
		{
			String temp = "./../../..";
			if (filename.charAt(0) != '/')
				temp = temp + '/';
			fileInputStream = Utility.class
					.getResourceAsStream(temp + filename);
		}

		// InputStream fileInputStream =
		// fileLoader.getResourceAsStream(filename);
		byte[] shaderCode = null;

		try {
			if (fileInputStream == null)
				fileInputStream = new FileInputStream(filename);
			DataInputStream dataStream = new DataInputStream(fileInputStream);
			dataStream.readFully(shaderCode = new byte[fileInputStream
					.available()]);
			fileInputStream.close();
			dataStream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		ByteBuffer shaderPro = BufferUtils.createByteBuffer(shaderCode.length);

		shaderPro.put(shaderCode);
		shaderPro.flip();

		return shaderPro;
	}

	private static ByteBuffer toByteString(String str, boolean isNullTerminated) {
		int length = str.length();
		if (isNullTerminated)
			++length;
		ByteBuffer buff = BufferUtils.createByteBuffer(length);
		buff.put(str.getBytes());

		if (isNullTerminated)
			buff.put((byte) 0);

		buff.flip();
		return buff;
	}

}
