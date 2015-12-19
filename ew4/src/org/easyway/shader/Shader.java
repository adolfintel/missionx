/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.shader;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.objects.texture.ImageData;
import org.easyway.system.StaticRef;
import org.easyway.utils.Utility;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Util;
import org.lwjgl.util.vector.Matrix3f;
import static org.lwjgl.opengl.GL20.*;

/**
 *
 * @author Daniele Paggi
 */
public class Shader<T> {

    static int bindedProgramID = 0;
    private static Shader defaultShader;

    public static Shader getDefaultShader() {
        if (!StaticRef.use_shaders) {
            return null;
        }
        
        if (defaultShader == null) {
            System.out.println("loading default shader..");
            // defaultShader = new Shader<IPlain2D>("/org/easyway/shader/Default.vs", "/org/easyway/shader/Default.fs");
            defaultShader = new Shader<IPlain2D>("/shaders/Default.vs", "/shaders/Default.fs");
        }
        return defaultShader;
    }
    int programID;
    int fragmentID = 0;
    int vertexID = 0;
    int width = 0;
    int height = 0;

    public Shader(String vertexShader, String fragmentShader) {
        ByteBuffer programSource;

        System.out.println("----");
        System.out.println("- SHADING INFO for: "+vertexShader+" , "+fragmentShader);
        if (vertexShader != null) {
            programSource = getProgramCode(vertexShader);
            vertexID = glCreateShader(GL_VERTEX_SHADER);
            glShaderSource(vertexID, programSource);
            glCompileShader(vertexID);
            System.out.println("vertxid "+vertexID);
        }

        if (fragmentShader != null) {
            programSource = getProgramCode(fragmentShader);
            fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
            glShaderSource(fragmentID, programSource);
            glCompileShader(fragmentID);
            System.out.println("fragid "+fragmentID);
        }

        programID = glCreateProgram();
        System.out.println("progid "+programID);
        if (fragmentShader != null) {
            glAttachShader(programID, fragmentID);
        }
        if (vertexShader != null) {
            glAttachShader(programID, vertexID);
        }
        glLinkProgram(programID);
        glValidateProgram(programID);
        printLogInfo(programID);
    }

    public void setFrameBufferSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private static void printLogInfo(int obj) {
        IntBuffer iVal = BufferUtils.createIntBuffer(1);
        glGetProgram(obj, GL_INFO_LOG_LENGTH, iVal);

        int length = iVal.get();
        System.out.println("Info log length:" + length);
        if (length > 0) {
            // We have some info we need to output.
            ByteBuffer infoLog = BufferUtils.createByteBuffer(length);
            iVal.flip();
            glGetProgramInfoLog(obj, iVal, infoLog);
            // ARBShaderObjects.glGetInfoLogARB(obj, iVal, infoLog);
            byte[] infoBytes = new byte[length];
            infoLog.get(infoBytes);
            String out = new String(infoBytes);

            System.out.println("Info log:\n" + out);
        }
        Util.checkGLError();
        // LWJGLUtil.checkGLError();
    }


    /*
    public void addShader(int id) {
    glAttachShader(programID, id);
    }

    public void addFragmentShader(String filename) {
    if (filename != null) {
    ByteBuffer programSource = getProgramCode(filename);
    int fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fragmentID, programSource);
    glCompileShader(fragmentID);
    glAttachShader(programID, fragmentID);
    }
    }
     */
    public int getUniformLocation(String name) {
        int lastBinded = bindedProgramID;
        if (bindedProgramID != programID) {
            bind();
        }
        ByteBuffer buffer = toByteString(name, true);
        int location = glGetUniformLocation(programID, buffer);
        if (bindedProgramID != lastBinded) {
            glUseProgram(lastBinded);
            bindedProgramID = lastBinded;
        }
        return location;
    }

    public void setUniform(String name, float x) {
        setUniform(getUniformLocation(name), x);
    }

    protected void setUniform(int location, float x) {
        int lastBinded = bindedProgramID;
        if (bindedProgramID != programID) {
            bind();
        }
        glUniform1f(location, x);

        if (bindedProgramID != lastBinded) {
            glUseProgram(lastBinded);
            bindedProgramID = lastBinded;
        }
    }

    public void setUniform(String name, float x, float y) {
        setUniform(getUniformLocation(name), x, y);
    }

    protected void setUniform(int location, float x, float y) {
        int lastBinded = bindedProgramID;
        if (bindedProgramID != programID) {
            bind();
        }
        glUniform2f(location, x, y);

        if (bindedProgramID != lastBinded) {
            glUseProgram(lastBinded);
            bindedProgramID = lastBinded;
        }
    }

    public void setUniform(String name, float x, float y, float z) {
        setUniform(getUniformLocation(name), x, y, z);
    }

    protected void setUniform(int location, float x, float y, float z) {
        int lastBinded = bindedProgramID;
        if (bindedProgramID != programID) {
            bind();
        }
        glUniform3f(location, x, y, z);

        if (bindedProgramID != lastBinded) {
            glUseProgram(lastBinded);
            bindedProgramID = lastBinded;
        }
    }

    public void setUniform(String name, boolean value) {
        setUniform(getUniformLocation(name), value);
    }

    protected void setUniform(int location, boolean value) {
        int lastBinded = bindedProgramID;
        if (bindedProgramID != programID) {
            bind();
        }

        glUniform1i(location, value ? 1 : 0);

        if (bindedProgramID != lastBinded) {
            glUseProgram(lastBinded);
            bindedProgramID = lastBinded;
        }
    }

    public void setUniform(String name, float x, float y, float z, float w) {
        setUniform(getUniformLocation(name), x, y, z, w);
    }

    protected void setUniform(int location, float x, float y, float z, float w) {
        int lastBinded = bindedProgramID;
        if (bindedProgramID != programID) {
            bind();
        }
        glUniform4f(location, x, y, z, w);

        if (bindedProgramID != lastBinded) {
            glUseProgram(lastBinded);
            bindedProgramID = lastBinded;
        }
    }

    public void setUniform(String name, ITexture image, int unit) {
        setUniform(getUniformLocation(name), image, unit);
    }

    protected void setUniform(int location, ITexture image, int unit) {
        int lastBinded = bindedProgramID;
        if (bindedProgramID != programID) {
            bind();
        }
        image.bind(unit);
        glUniform1i(location, unit);
        if (bindedProgramID != lastBinded) {
            glUseProgram(lastBinded);
            bindedProgramID = lastBinded;
        }
    }

    public void setUniform(String name, Matrix3f matrix) {
        setUniform(getUniformLocation(name), matrix);
    }

    protected void setUniform(int location, Matrix3f matrix) {
        int lastBinded = bindedProgramID;
        if (bindedProgramID != programID) {
            bind();
        }

        FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
        buffer.rewind();
        buffer.put(matrix.m00).put(matrix.m01).put(matrix.m02).
                put(matrix.m10).put(matrix.m11).put(matrix.m12).
                put(matrix.m20).put(matrix.m21).put(matrix.m22);
        glUniformMatrix3(location, false, buffer);

        if (bindedProgramID != lastBinded) {
            glUseProgram(lastBinded);
            bindedProgramID = lastBinded;
        }
    }

    public boolean useVertexShader() {
        return vertexID != 0;
    }

    public boolean useFragmentShader() {
        return fragmentID != 0;
    }

    public int getVertexShader() {
        return vertexID;
    }

    public int getFragmentShader() {
        return fragmentID;
    }

    public void bind() {
        if (bindedProgramID != programID) {
            glUseProgram(programID);
            bindedProgramID = programID;
        }
    }

    public static void unBind() {
        if (bindedProgramID != 0) {
            glUseProgram(0);
            bindedProgramID = 0;
        }
    }

    public void update(T target) {
    }

    private static ByteBuffer getProgramCode(String filename) {
        InputStream fileInputStream = null;
        {
            if (filename.startsWith("/")) {
                filename = filename.substring(1);
            }
            int index;
            while ((index = filename.indexOf("\\")) != -1) {
                filename = filename.substring(0, index) + '/' + filename.substring(index + 1);
            }
            URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
            if (url != null) {
                try {
                    fileInputStream = url.openStream();
                } catch (IOException ex) {
                    Logger.getLogger(ImageData.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Utility.error("Shader.getProgramCode(String)", "Shader " + filename + " was not found!");
                throw new ShaderNotFoundException(filename);
            }
        }
        assert fileInputStream != null;

        /*InputStream fileInputStream;
        {
        String temp = "./../../..";
        if (filename.charAt(0) != '/') {
        temp = temp + '/';
        }
        fileInputStream = Utility.class.getResourceAsStream(temp + filename);
        }*/

        byte[] shaderCode = null;

        try {
            if (fileInputStream == null) {
                fileInputStream = new FileInputStream(filename);
            }
            DataInputStream dataStream = new DataInputStream(fileInputStream);
            dataStream.readFully(shaderCode = new byte[fileInputStream.available()]);
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
        if (isNullTerminated) {
            ++length;
        }
        ByteBuffer buff = BufferUtils.createByteBuffer(length);
        buff.put(str.getBytes());

        if (isNullTerminated) {
            buff.put((byte) 0);
        }

        buff.flip();
        return buff;
    }

    @Override
    protected void finalize() throws Throwable {
        if (fragmentID != 0) {
            glDeleteShader(fragmentID);
            fragmentID = 0;
        }
        if (vertexID != 0) {
            glDeleteShader(vertexID);
            vertexID = 0;
        }
        glDeleteProgram(programID);
        programID = 0;
        super.finalize();
    }
}
