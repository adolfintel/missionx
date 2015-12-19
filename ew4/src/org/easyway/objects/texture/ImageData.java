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
package org.easyway.objects.texture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.easyway.utils.ImageUtils;
import org.easyway.utils.Utility;
import org.lwjgl.BufferUtils;

public class ImageData implements Serializable {

    /**
     * generated version id
     */
    private static final long serialVersionUID = -7893392091273534932L;
    /**
     * the data of image
     */
    protected transient ByteBuffer data;
    /**
     * the width of image
     */
    protected int width;
    /**
     * the height of image
     */
    protected int height;
    /**
     * the width hardware of image
     */
    protected int widthHW;
    /**
     * the height hardware of image
     */
    protected int heightHW;
    /**
     * the internal format of image (RGB or RGBA)
     */
    //protected int format;
    protected boolean solid = true;

    public ImageData(TextureID texture, ByteBuffer bb) {
        data = bb;
        width = texture.width;
        widthHW = texture.widthHW;
        height = texture.height;
        heightHW = texture.heightHW;
        //format = texture.format;
        solid = texture.solid;
    }

    public ImageData(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();
        widthHW = ImageUtils.getNextPowerOfTwoHW(width);
        heightHW = ImageUtils.getNextPowerOfTwoHW(height);

        data = ByteBuffer.allocateDirect(4 * widthHW * heightHW);
        data.clear();
        byte imgdata[] = (byte[]) image.getRaster().getDataElements(0, 0,
                width, height, null);

        int x = 0, y = 0;
        if (4 * widthHW * heightHW == imgdata.length) {
            for (int index = 0; index < imgdata.length; ++index) {
                data.put(imgdata[index]) // red
                        .put(imgdata[++index]) // green
                        .put(imgdata[++index]) // blue
                        .put(imgdata[++index]); // alpha
                ++x;
                if (x == width) {
                    data.position(widthHW * 4 * ++y);
                    x = 0;
                }
            }
        } else {
            for (int index = 0; index < imgdata.length; ++index) {
                data.put(imgdata[index]) // red
                        .put(imgdata[++index]) // green
                        .put(imgdata[++index]) // blue
                        .put((byte) 255); // alpha
                ++x;
                if (x == width) {
                    data.position(widthHW * 4 * ++y);
                    x = 0;
                }
            }
        }
        data.rewind();

        // solid = false;
        // format = ImageType.RGBA;
    }

    public ImageData(String path) {
        InputStream is = null;
        {
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            int index;
            while ((index = path.indexOf("\\")) != -1) {
                path = path.substring(0, index) + '/' + path.substring(index + 1);
            }
            URL url = Thread.currentThread().getContextClassLoader().getResource(path);
            if (url != null) {
                try {
                    is = url.openStream();
                } catch (IOException ex) {
                    Logger.getLogger(ImageData.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Utility.error("ImageData(String,boolean)", "Image " + path + " was not found!");
                width = height = -1;
                data = null;
                throw new TextureNotFoundException(path);
            }
        }
        assert is != null;
        BufferedImage image = null;
        try {
            image = ImageIO.read(is);
        } catch (IOException ex) {
            Logger.getLogger(ImageData.class.getName()).log(Level.SEVERE, null, ex);
        }

        width = image.getWidth();

        height = image.getHeight();
        widthHW = ImageUtils.getNextPowerOfTwoHW(width);
        heightHW = ImageUtils.getNextPowerOfTwoHW(height);
        // System.out.println("width: "+width+" wh: "+widthHW);
        // System.out.println("height: "+height+" hh: "+heightHW);

        data = ByteBuffer.allocateDirect(4 * widthHW * heightHW);
        data.clear();
        data.rewind();
        int imgdata[];
        /*byte imgdata[] = (byte[]) image.getRaster().getPixels(0, 0,//.getDataElements(0, 0,
        width, height, (int[])null);*/
        imgdata = image.getRaster().getPixels(0, 0, width, height, (int[]) null);

        int x = 0, y = 0;
        solid = !image.getColorModel().hasAlpha();
        // System.out.println("is using alpha: "+!solid+" size: "+imgdata.length+ "  S: "+4 * widthHW * heightHW );
        if (4 * width * height == imgdata.length) {
            for (int index = 0; index < imgdata.length; ++index) {
                data.put((byte) imgdata[index]) // red
                        .put((byte) imgdata[++index]) // green
                        .put((byte) imgdata[++index]) // blue
                        .put((byte) imgdata[++index]); // alpha
                ++x;
                if (x == width) {
                    data.position(widthHW * 4 * ++y);
                    x = 0;
                }
            }
        } else {
            if (imgdata.length % 3 == 0 && solid) { // RGB!
                for (int index = 0; index < imgdata.length; ++index) {
                    data.put((byte) imgdata[index]) // red
                            .put((byte) imgdata[++index]) // green
                            .put((byte) imgdata[++index]) // blue
                            .put((byte) 255); // alpha
                    ++x;
                    if (x == width) {
                        data.position(widthHW * 4 * ++y);
                        x = 0;
                    }
                }
            } else {
                BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics g = bimage.createGraphics();
                g.drawImage(image, 0, 0, null);
                imgdata = image.getRaster().getPixels(0, 0, width, height, (int[]) null);
                for (int index = 0; index < imgdata.length; ++index) {
                    data.put((byte) imgdata[index]) // red
                            .put((byte) imgdata[++index]) // green
                            .put((byte) imgdata[++index]) // blue
                            .put((byte) imgdata[++index]); // alpha
                    ++x;
                    if (x == width) {
                        data.position(widthHW * 4 * ++y);
                        x = 0;
                    }
                }
                /*System.out.println("IMG WIDTH: "+image.getWidth());
                System.out.println("IMG HEIGHT: "+image.getHeight());
                System.out.println("IMG SIZE: "+imgdata.length);
                throw new RuntimeException("Error loading the image: Plase don't use compressed images!");*/
            }
        }
        data.rewind();

        // solid = false;
        // format = ImageType.RGBA;
    }

    /**
     * crates a new instance of ImageData loading the image specified. If
     * useAlpha is setted as true the image will use the RGBA format else will
     * use the RGB format
     *
     * @param path
     *            the image path
     * @param useAlpha
     *            indicates if the image will use the internal format RGBA or
     *            RGB
     */
    /*public ImageData(String path, boolean useAlpha, int old) {

    // numberOfByetes = 3 or 4
    // format = RGB or RGBA
    int numberOfBytes = ((format = (useAlpha ? ImageType.RGBA
    : ImageType.RGB)) == ImageType.RGB) ? 3 : 4;

    // convert the PATH as InputStream
    InputStream is = null;
    {
    if (path.startsWith("/")) {
    path = path.substring(1);
    }
    int index;
    // path = path.replaceAll("\\", "/");
    while ((index = path.indexOf("\\")) != -1) {
    path = path.substring(0, index) + '/' + path.substring(index + 1);
    }
    //try {
    URL url = Thread.currentThread().getContextClassLoader().getResource(path);
    if (url != null) {
    try {
    is = url.openStream();
    } catch (IOException ex) {
    Logger.getLogger(ImageData.class.getName()).log(Level.SEVERE, null, ex);
    }
    } else {
    Utility.error("ImageData(String,boolean)", "Image " + path + " was not found!");
    width = height = -1;
    data = null;
    throw new TextureNotFoundException(path);
    }
    }
    assert is != null;
    int type = ImageUtils.getType(path);
    // load the data from the file
    IntBuffer scratch = BufferUtils.createIntBuffer(1);

    // create image in DevIL and bind it
    IL.ilGenImages(scratch);
    IL.ilBindImage(scratch.get(0));

    try {
    IL.ilLoadFromStream(is, type);
    is.close();
    } catch (Exception e) {
    Utility.error("Error to load Image", "ImageData(String,boolean)", e);
    }

    // convert data as byte
    IL.ilConvertImage(format, IL.IL_BYTE);

    // need to flip the image?
    if (type == IL.IL_BMP) {
    ILU.iluFlipImage();
    // IL.ilOriginFunc(IL.IL_ORIGIN_LOWER_LEFT);
    // IL.ilEnable(IL.IL_ORIGIN_SET);
    }

    // align the data
    width = IL.ilGetInteger(IL.IL_IMAGE_WIDTH);
    height = IL.ilGetInteger(IL.IL_IMAGE_HEIGHT);
    widthHW = ImageUtils.getNextPowerOfTwoHW(width);
    heightHW = ImageUtils.getNextPowerOfTwoHW(height);
    if (Runtime.getRuntime().freeMemory() <= widthHW * heightHW * numberOfBytes) {
    Runtime.getRuntime().gc();
    if (Runtime.getRuntime().freeMemory() <= widthHW * heightHW * numberOfBytes) {

    Utility.error(
    "<image: " + path + "> the memory is ending.. trying to free memory\n for extra information write on the forum",
    "ImageData(String,boolean)");
    System.out.println("PRE-FREE SPACE: " + (Runtime.getRuntime().freeMemory()) + " -- max: " + (Runtime.getRuntime().maxMemory()) + " -- tot: " + (Runtime.getRuntime().totalMemory()));
    TextureID.FREE_MEMORY = true;
    // TextureID.USE_MIPMAP = true;
    System.setProperty("-Xmx",
    (Runtime.getRuntime().maxMemory() * 2) / 1048576 + "m");
    ImageUtils.freeSpace();

    System.out.println("POST-FREE SPACE: " + (Runtime.getRuntime().freeMemory()) + " -- max: " + (Runtime.getRuntime().maxMemory()) + " -- tot: " + (Runtime.getRuntime().totalMemory()));
    }
    }

    if (widthHW != width || heightHW != height) {
    // Fix for Opengl1
    data = BufferUtils.createByteBuffer(widthHW * heightHW * numberOfBytes);
    IL.ilCopyPixels(0, 0, 0, widthHW, heightHW, 1,
    format == ImageType.RGB ? IL.IL_RGB : IL.IL_RGBA, IL.IL_BYTE,
    data);
    } else {
    // data = IL.ilGetData();
    data = BufferUtils.createByteBuffer(widthHW * heightHW * numberOfBytes);
    data.rewind();
    IL.ilCopyPixels(0, 0, 0, widthHW, heightHW, 1,
    format == ImageType.RGB ? IL.IL_RGB : IL.IL_RGBA, IL.IL_BYTE,
    data);
    }

    // ??
    IL.ilDeleteImages(scratch);
    scratch = null;

    }*/
    /**
     * creates a new empty imageData
     *
     * @param width
     *            the width of imageData
     * @param height
     *            the height of imageData
     * @param useAlpha
     *            indicates the internal format of image: RGBA or RGB
     */
    public ImageData(int width, int height) {
        this.width = width;
        this.height = height;
        this.widthHW = ImageUtils.getNextPowerOfTwoHW(width);
        this.heightHW = ImageUtils.getNextPowerOfTwoHW(height);
        data = BufferUtils.createByteBuffer(4 * widthHW * heightHW);
    }

    /**
     * make the image transparent
     *
     * @param red
     *            the red value (0-255)
     * @param green
     *            the green value (0-255)
     * @param blue
     *            the blue value (0-255)
     */
    public void makeTransp(byte red, byte green, byte blue) {
        makeTransp(red, green, blue, (byte) 0);
    }

    /**
     * make the image transparent
     *
     * @param red
     *            the red value (0-255)
     * @param green
     *            the green value (0-255)
     * @param blue
     *            the blue value (0-255)
     * @param alpha
     *              the alpha value that will be setted (0-255)
     */
    public void makeTransp(byte red, byte green, byte blue, byte alpha) {
        solid = false;
        if (data == null) {
            Utility.error("the imageData is destroyed",
                    "ImageData.makeTransp(int,int,int)");
            return;
        }
        data.rewind();
        int r, g, b;
        int position;
        int step = widthHW * 4; // lenght of a line
        for (int y = 0; y < heightHW; ++y) {
            position = data.position();

            for (int x = 0; x < widthHW; ++x) {
                //data.mark();
                r = data.get(); // red
                g = data.get(); // green
                b = data.get(); // blue
                //data.mark();
                if (r == red && g == green && b == blue) { // test
                    //data.reset();
                    data.put(alpha);
                    //data.put((byte) 0).put((byte) 0).put((byte) 0).put((byte) 0); // transparent
                } else {
                    data.get(); // ignore alpha
                }
                if (x == width) { // out image?
                    data.position(position + step);
                    break; // x = widthHW
                }
            }
            if (y == height) {
                break; // y = heightHW // end
            }
        }
        data.rewind();
    }

    public void setData(BufferedImage image) {
        if (image != null) {
            setData(image.getData());
        }
    }

    public void setData(Raster rasterData) {
        if (rasterData == null) {
            Utility.error("rasterData is null", "ImageData.setData(Raster)");
            new Exception().printStackTrace();
            return;
        }
        width = rasterData.getWidth();
        height = rasterData.getHeight();
        if (width > widthHW || height > heightHW) {
            Utility.error(
                    "width>widthHW or height>heightHW: consult the forum :) -image NOT edited-",
                    "ImageData.setData(Raster)");
            new Exception().printStackTrace();
            return;
        }
        int pixelData[] = rasterData.getPixels(0, 0, width, height,
                (int[]) null);
        data.rewind();
        int counter = 0;
        int position = data.position();
        int step = widthHW * 4;
        for (int i = 0; i < pixelData.length; ++i) {
            ++counter;
            if (counter > width) { // test X
                data.position(position + step);
                position = data.position();
                counter = 1;
            }
            data.put((byte) pixelData[i]);
            data.put((byte) pixelData[++i]);
            data.put((byte) pixelData[++i]);
            // ++i;
            // data.put((byte)255);
            data.put((byte) pixelData[++i]);
        }
        data.rewind();
        solid = false;
    }

    public ByteBuffer getData() {
        data.rewind();
        return data;
    }

    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        s.defaultWriteObject();
        data.rewind();
        for (int i = 0; i < widthHW * heightHW; ++i) {
            s.writeByte(data.get());
            s.writeByte(data.get());
            s.writeByte(data.get());
            s.writeByte(data.get());
        }

    }

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        //int numberOfBytes = format == ImageType.RGB ? 3 : 4;
        data = BufferUtils.createByteBuffer(4 * widthHW * heightHW);
        data.rewind();
        for (int i = 0; i < widthHW * heightHW; ++i) {
            data.put(s.readByte()).put(s.readByte()).put(s.readByte()).put(s.readByte());
        }
        data.rewind();
    }
}
