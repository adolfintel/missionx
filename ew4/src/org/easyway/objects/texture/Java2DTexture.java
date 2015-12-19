/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.objects.texture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.utils.ImageUtils;

/**
 *
 * @author Daniele
 */
public class Java2DTexture extends Texture {

    private static final Color blackAlphaColor = new Color(0, true);
    protected static Java2DTexture j2d;

    public static Java2DTexture getDefault() {
        return j2d == null
                ? j2d = new Java2DTexture(Core.getInstance().getWidth(),
                Core.getInstance().getHeight())
                : j2d;
    }
    protected BufferedImage imageData;
    Graphics2D g2d;
    // protected Texture texture;
    // protected ByteBuffer directBuffer;

    public Java2DTexture(int width, int height) {
        super(ImageUtils.getNextPowerOfTwoHW(width), ImageUtils.getNextPowerOfTwoHW(height));
        StaticRef.textures.remove(this);
        setUseAlphaChannel(true);
        setRegion(0, 0, width, height);
        imageData = new BufferedImage(getWidthHW(), getHeightHW(), BufferedImage.TYPE_4BYTE_ABGR);
        g2d = imageData.createGraphics();
        //directBuffer = ByteBuffer.allocateDirect(4 * imageData.getWidth() * imageData.getHeight());
    }

    public Graphics2D getGraphics() {
        return g2d;
    }

    public void clear() {
        clear(blackAlphaColor);
    }

    public void clear(Color color) {
        g2d.setBackground(color);
        g2d.clearRect(0, 0, getWidth(), getHeight());
    }

    public void update() {
        ByteBuffer directBuffer = null;
        if (dataid.data != null && dataid.data.data != null) {
            directBuffer = dataid.data.data;
        } else {
            directBuffer = ByteBuffer.allocateDirect(getWidthHW() * getHeightHW() * 4);
        }
        // ByteBuffer directBuffer = ByteBuffer.allocateDirect(getWidthHW()*getHeightHW()*4);

        directBuffer.rewind();
        directBuffer.clear();

        byte[] arrayData = (byte[]) imageData.getRaster().getDataElements(0, 0, imageData.getWidth(), imageData.getHeight(), null);
        directBuffer.put(arrayData);
        directBuffer.rewind();
        getTextureId().setData(directBuffer);
    }
}
