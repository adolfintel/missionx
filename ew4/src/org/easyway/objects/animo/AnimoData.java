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
package org.easyway.objects.animo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.easyway.interfaces.base.ITexture;
import org.easyway.objects.texture.Texture;
import org.easyway.utils.Utility;

public class AnimoData implements Serializable {

    private static final long serialVersionUID = 5946315432609621973L;
    protected ArrayList<ITexture> images = new ArrayList<ITexture>(10);
    protected ArrayList<Long> frames = new ArrayList<Long>(10);
    /**
     * the default frame rate that the AnimoData will use<br>
     * note: this value MUST be in nanoseconds;<br>
     * 200 ms = 200 * 100000000L ns
     */
    protected static long DEFAULT_SPEED = 100000;

    /**
     * adds a new image in the animoData
     *
     * @param image
     */
    public void add(ITexture image) {
        images.add(image);
        frames.add(DEFAULT_SPEED);
    }

    /**
     * adds a new image in the animoData
     *
     * @param speed
     *            the speed rate in ms of image
     * @param image
     *            the image to add in the animo
     */
    public void add(long speed, ITexture image) {
        images.add(image);
        frames.add(speed * 1000000L);
    }

    /**
     * removes an image from the animoData
     *
     * @param image
     */
    public void remove(ITexture image) {
        int i;
        frames.remove(i = images.indexOf(image));
        images.remove(i);
    }

    /**
     * removes an image from the animoData
     *
     * @param index
     */
    public void remove(int index) {
        frames.remove(index);
        images.remove(index);
    }

    /**
     * adds an image at the position specified
     *
     * @param image
     * @param index
     */
    public void add(ITexture image, int index) {
        images.add(index, image);
        frames.add(index, DEFAULT_SPEED);
    }

    public void add(long speed, ITexture image, int index) {
        images.add(index, image);
        frames.add(index, speed * 1000000L);
    }

    /**
     * returns the number of images contained in the animoData
     *
     * @return
     */
    public int size() {
        return images.size();
    }

    /**
     * gets an image from the animoData
     *
     * @param index
     * @return
     */
    public ITexture getImage(int index) {
        if (images.size() == 0) {
            return null;
        }
        return images.get(index);
    }

    /**
     * sets the frame-rate of the image specified
     *
     * @param index
     * @return the frame in NANO-seconds
     */
    public void setFrame(int index, long time) {
        if (index < frames.size()) {
            frames.set(index, time);
        } else {
            Utility.error("out of range in AnimoData.setFrame",
                    "AnimoData.setFrame");
        }
    }

    /**
     * sets the frame-rate of the image specified
     *
     * @param index
     * @return the frame in MILLI-seconds
     */
    public void setFrameMilli(int index, int time) {
        if (index < frames.size()) {
            frames.set(index, time * 1000000L);
        } else {
            Utility.error("out of range in AnimoData.setFrame",
                    "AnimoData.setFrame");
        }
    }

    /**
     * gets the frame-rate of the image specified
     *
     * @param index
     * @return the frame in NANO-seconds
     */
    public long getFrame(int index) {
        if (frames.size() == 0) {
            return Long.MAX_VALUE;
        }
        return frames.get(index);
    }

    /**
     * gets the frame-rate of the image specified
     *
     * @param image
     * @return the frame in NANO-seconds
     */
    public long getFrame(ITexture image) {
        return frames.get(images.indexOf(image));
    }

    /**
     * returns the frame-rate of the image specified
     *
     * @param index
     * @return returns the frame in MILLI-seconds
     */
    public int getFrameMilli(int index) {
        return (int) (frames.get(index) / 1000000L);
    }

    /**
     * gets the frame-rate of the image specified
     *
     * @param image
     * @return the frame in NANO-seconds
     */
    public int getFrameMilli(ITexture image) {
        return (int) (frames.get(images.indexOf(image)) / 1000000L);
    }

    /**
     * sets the DEFAULT_SPEED value
     *
     * @param speed
     *            speed in milliseconds
     */
    public static void setDefaultSpeed(int speed) {
        DEFAULT_SPEED = speed * 1000000L;
    }

    /**
     * returns the DEFAULT_SPEED in milliseconds
     *
     * @return the DEFAULT_SPEED in milliseconds
     */
    public static final int getDefaultSpeed() {
        return (int) (DEFAULT_SPEED / 1000000L);
    }

    public static AnimoData readFromFile(ObjectInputStream in) {

        // ObjectInputStream in = Utility.getLocalFile(file);
        if (in == null) {
            return null;
        }

        try {
            // read the version (at this time it's only 1)
			/* int version = */            in.readInt();

            int size = in.readInt();
            AnimoData animo = new AnimoData();

            Texture texture;
            long time;
            for (int i = 0; i < size; ++i) {
                //texture = Texture.readFromFile(in);// (Texture)in.readObject();
                texture = (Texture) in.readObject();
                time = in.readLong();

                animo.add(time, texture);
            }

            // in.close();
            return animo;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AnimoData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeOnFile(ObjectOutputStream out) {
        if (images.size() != frames.size()) {
            Utility.error("found a bug in the AnimoData class",
                    new Exception("images.size()!=frames.size()"));
            return;
        }

        try {
            // FileOutputStream fout = new FileOutputStream(filename);
            // ObjectOutputStream out = new ObjectOutputStream(fout);

            // the version-id
            out.writeInt(1);

            out.writeInt(images.size());
            for (int i = 0; i < images.size(); ++i) {
                //((Texture) images.get(i)).writeOnFile(out);
                out.writeObject(images.get(i));
                // I'll write the speed in ms instead of ns
                out.writeLong(frames.get(i) / 1000000L);
            }
            // out.close();
            // fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
