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

import java.io.Serializable;

import org.easyway.interfaces.base.IDestroyable;
import org.easyway.interfaces.base.IPureLoopable;
import org.easyway.interfaces.base.ITexture;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;

public class Animo implements IPureLoopable, IDestroyable, Serializable {

    /**
     * generated serial version
     */
    private static final long serialVersionUID = 4577168281901778049L;
    /**
     * indicates if the object is destroyed or not
     */
    private boolean destroyed = false;
    /**
     * the animo data
     */
    protected AnimoData data;
    /**
     * how much time is elasped from the last loop
     */
    protected long elaspedTime;
    /**
     * a chaced time
     */
    protected long cacheTime;
    /**
     * current index
     */
    protected int index;
    /**
     * indicates if the Animo is stopped or not
     */
    protected boolean stopped;
    /**
     * indicates if the Animo is already added to the animoList
     */
    protected boolean added;

    /**
     * creates a new instance of Animo
     *
     * @param data
     */
    public Animo(AnimoData data) {
        // this.data = data;
        setData(data);
    }

    /**
     * creates a new instance of Animo
     */
    public Animo() {
        setData(new AnimoData());
    }

    public Animo(Animo animo) {
        setData(animo.data);
        elaspedTime = index = 0;
        cacheTime = data.getFrame(0);
    }

    /**
     * sets the animoData
     *
     * @param data
     */
    public void setData(AnimoData data) {
        this.data = data;
        removeFromList();
        addToList();
    }

    /**
     * returns the animoData
     *
     * @return
     */
    public AnimoData getData() {
        return data;
    }

    public void loop() {
        if (stopped || data.size() <= 1) {
            return;
        }

        elaspedTime += Core.getInstance().getElaspedTime();

        if (elaspedTime >= cacheTime) {
            next();
        }
    }

    /**
     * move to the next image
     *
     */
    public void next() {
        ++index;
        if (index == data.size()) {
            index = 0;
        }

        // if (added) {
        // StaticRef.animoList.remove(this);
        // added = false;
        // }
        cacheTime = data.getFrame(index);
        elaspedTime = 0;
        // if (!stopped)
        // StaticRef.animoList.add(this);
    }

    protected void next(boolean toAdd) {
        ++index;
        if (index == data.size()) {
            index = 0;
        }
        // if (added) {
        // StaticRef.animoList.remove(this);
        // added = false;
        // }
        elaspedTime = 0;
        cacheTime = data.getFrame(index);

        // if (!stopped && toAdd)
        // StaticRef.animoList.add(this);
    }

    /**
     * move to the previous image
     *
     */
    public void prev() {
        // Assembly - optimized
        if (index == 0) {
            index = data.size() - 1;
        } else {
            --index;
        }
        // end assembly - optimized
        cacheTime = data.getFrame(index);
        // if (added) {
        // StaticRef.animoList.remove(this);
        // added = false;
        // }
        // if (!stopped)
        // StaticRef.animoList.add(this);
    }

    /**
     * move to the previous image
     *
     */
    protected void prev(boolean toAdd) {
        // Assembly - optimized
        if (index == 0) {
            index = data.size() - 1;
        } else {
            --index;
        }
        // end assembly - optimized
        cacheTime = data.getFrame(index);
        // if (added) {
        // StaticRef.animoList.remove(this);
        // added = false;
        // }
        // if (!stopped && toAdd)
        // StaticRef.animoList.add(this);
    }

    /**
     * get the current image
     *
     * @return
     */
    public ITexture get() {
        return data.getImage(index);
    }

    /**
     * get the currnt frame rate
     *
     * @return
     */
    public int getFrame() {
        return data.getFrameMilli(index);
    }

    /**
     * returns the elasped time
     *
     * @return the elasped time
     */
    public long getElaspedTime() {
        return elaspedTime;
    }

    /**
     * rewind the animation and then start the animation from the beginning
     *
     */
    public void start() {
        elaspedTime = index = 0;
        cacheTime = data.getFrame(0);
        stopped = false;
    }

    /**
     * resume the animation
     *
     */
    public void resume() {
        stopped = false;
    }

    /**
     * freeze the animation
     *
     */
    public void pause() {
        stopped = true;
    }

    /**
     * freeze the animation<br/>
     * it's the same of pause()
     */
    public void stop() {
        pause();
    }

    /**
     * creates a copy of the current Animo
     *
     * @return a copy
     * @see #NewAnimo(AnimoData)
     */
    public Animo copy() {
        return new Animo(this);
    }

    /**
     * adds a texture to the Animo's data
     *
     * @param image
     *            the image to add
     * @param frame
     *            the frame rate of the current image
     * @see AnimoData.add( Texture, int )
     */
    public void add(ITexture image, long frame) {
        data.add(frame, image); // 0.3.3.1
    }

    /**
     * adds a texture to the animo's data
     *
     * @param image
     *            the image to add
     * @see AnimoData.add(Texture)
     * @see AnimoData.DEFAULT_SPEED
     */
    public void add(ITexture image) {
        data.add(image);
    }

    /**
     * adds a texture collection set to the animo's data
     *
     * @param images the collection of images
     */
    public void add(ITexture[] images) {
        for (ITexture img : images) {
            data.add(img);
        }
    }

    /**
     * adds a subrange of textures taken from a texture array to the animo's data<br/>
     *
     * @param images an array of textuers
     * @param from the first texture ID to add in the animation
     * @param to the last texture ID to add in the animation
     */
    public void add(ITexture[] images, int from, int to) {
        if (from < 0 || from >= images.length) {
            return;
        }
        if (to < 0 || to >= images.length) {
            return;
        }
        if (to < from) {
            // swap
            int t = from;
            from = to;
            to = t;
        }
        for (; from <= to; ++from) {
            data.add(images[from]);
        }
    }

    public void removeFromList() {
        // if (added) {
        // added = false;
        // StaticRef.animoList.remove(this);
        // }
        GameState.getCurrentGEState().getAnimoList().remove(this);
    }

    public void addToList() {
        GameState.getCurrentGEState().getAnimoList().add(this);
        // if (!added) {
        // added = true;
        // StaticRef.animoList.add(this);
        // }
    }

    public void destroy() {
        if (destroyed) {
            return;
        }
        destroyed = true;
        removeFromList();
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * returns the current image index
     *
     * @return
     */
    public int getCurrentIndex() {
        return index;
    }

    /**
     * returns the number of images of animation
     *
     * @return
     */
    public int size() {
        return data.size();
    }

    /**
     * returns if the current frame is the last image of animation
     *
     * @return
     */
    public boolean isTheLast() {
        return index == data.size() - 1;
    }

    /**
     * returns if the current frame is the first image of animation
     *
     * @return
     */
    public boolean isTheFirst() {
        return index == 0;
    }

    /**
     * sets the current image of the animo
     *
     * @param index
     */
    public void setCurrentImage(int index) {
        this.index = index;
        removeFromList();
        addToList();
    }

    /**
     * returns the image that has got the index 'index'
     *
     * @param index
     * @return
     */
    public ITexture get(int index) {
        return data.getImage(index);
    }

    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        //setData(data);a
        addToList();
    }
}
