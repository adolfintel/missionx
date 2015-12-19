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
package org.easyway.input;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.easyway.gui.base.IContainer;
import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.interfaces.extended.ILayer;
import org.easyway.interfaces.sprites.IClickable;
import org.easyway.interfaces.sprites.IMaskerable;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.lists.BaseList;
import org.easyway.lists.Entry;
import org.easyway.lists.GameList;
import org.easyway.objects.Camera;
import org.easyway.objects.sprites2D.Mask;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.easyway.utils.Utility;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;

/**
 * this class manages the mouse inputs.<br>
 */
public class Mouse {

    public static final boolean useLwjgl = Core.useLwjgl;
    /** buffered coordiantes */
    protected static int bx, by;
    /** buffered button status */
    protected static boolean bleft, bright, bmiddle;
    /** x coordinate */
    protected static int x;
    /** y coordinate */
    protected static int y;
    /** x coordinate */
    protected static int oldx;
    /** y coordinate */
    protected static int oldy;
    protected static boolean oldSomeOne, someOne;
    /** indicates if the left button is down or not */
    protected static boolean left, oldLeft;
    /** indicates if the right button is down or not */
    protected static boolean right, oldRight;
    /** indicates if the middle button is down or not */
    protected static boolean middle, oldMiddle;    // protected static ArrayList<__Assocation> objects = new
    // ArrayList<__Assocation>(
    // 100);
    static EWMouseList objects = new EWMouseList();
    static EWMouseList guiObjects = new EWMouseList();
    static EWMouseList lastGuiObjs = new EWMouseList();
    protected static boolean guiMode = false;
    public static boolean collisionMode = false;

    public static void resetClickableObjects() {
        objects = new EWMouseList();
        guiObjects = new EWMouseList();
        lastGuiObjs = new EWMouseList();
    }

    /** data loop of mouse: updates the x,y coordiantes and the button status */
    public synchronized static void loop() {
        if (!org.lwjgl.input.Mouse.isCreated()) {
            return;
        }
        oldx = x;
        oldy = y;
        // if (useLwjgl) {
        x = org.lwjgl.input.Mouse.getX();
        y = Core.getInstance().getHeight() - org.lwjgl.input.Mouse.getY() - 1;
        // System.out.println("x "+x+" y "+y);
        oldLeft = left;
        oldRight = right;
        oldMiddle = middle;

        left = org.lwjgl.input.Mouse.isButtonDown(0);
        right = org.lwjgl.input.Mouse.isButtonDown(1);
        middle = org.lwjgl.input.Mouse.isButtonDown(2);
        // } else {
        // x = bx;
        // y = by;
        // left = bleft;
        // right = bright;
        // middle = bmiddle;
        // }
        oldSomeOne = someOne;
        someOne = left || right || middle;

        guiMode = false;
        collisionMode = true;
        loopGuiObject();
        if (!guiMode) {
            loopObject();
        }
        collisionMode = false;

    }

    protected static void loopObject() {
        objects.startScan();
        MouseAssociation obj;
        while (objects.next()) {
            obj = objects.getCurrent();
            if (obj == null || obj.object == null) {
                continue;
            }
            if (testCollision(obj.object)) {
                if (!obj.over) {
                    over(obj);
                }
                int nx = getX() - (int) obj.object.getXOnScreen();
                int ny = getY() - (int) obj.object.getYOnScreen();
                obj.object.onOver(nx, ny);
                if (someOne) {
                    obj.object.onDown(nx, ny);
                    obj.down = true;
                    if (oldSomeOne && (oldx != x || oldy != y)) {
                        obj.object.onDrag(x - oldx, y - oldy);
                    }
                }
                if (oldSomeOne && !someOne) {
                    if (obj.down) {
                        obj.down = false;
                        obj.object.onClick(nx, ny);
                    }
                    obj.object.onRelease(nx, ny);
                }
                // obj.object.onClick(nx, ny);
            } else if (obj.over) {
                notOver(obj);
            }
        }
    }

    protected static void loopGuiObject() {
        MouseAssociation obj;
        IContainer container;

        for (int i = 0; i < guiObjects.size(); ++i) {
            // start test object
            obj = guiObjects.get(i);
            if (obj == null || obj.object == null) {
                continue;
            }
            if (testCollision(obj.object)) {
                assert obj.object instanceof IContainer;
                container = searchChild((IContainer) obj.object);
                testGuiObj(container.getMouseAssociation());
                break;
            }
            // end test object
        }
        lastGuiObjs.startScan();
        while (lastGuiObjs.next()) {
            obj = lastGuiObjs.getCurrent();
            if (obj == null || obj.object == null) {
                //lastGuiObjs.remove(obj);
                continue;
            }
            if (!testCollision(obj.object)) {
                notOver(obj);
                lastGuiObjs.remove(obj);
            }
        }
    }

    protected static IContainer searchChild(IContainer father) {

        IContainer child;
        for (int i = 0; i < father.getChildren().size(); ++i) {
            child = father.getChildren().get(i);
            if (child == null) {
                Utility.error("null child", new Exception("null child"));
                continue;
            }
            if (testCollision(child)) {
                return searchChild(child);
            }
        }

        return father;
    }

    protected static void testGuiObj(MouseAssociation obj) {
        if (!lastGuiObjs.contains(obj)) {
            lastGuiObjs.add(obj);
        }
        // if (testCollision(obj.object)) {
        if (!obj.over) {
            over(obj);
        }
        int nx = getX() - (int) obj.object.getXOnScreen();
        int ny = getY() - (int) obj.object.getYOnScreen();
        obj.object.onOver(nx, ny);
        if (someOne) {
            obj.object.onDown(nx, ny);
            obj.down = true;
            if (oldSomeOne && (oldx != x || oldy != y)) {
                obj.object.onDrag(x - oldx, y - oldy);
            }
        }
        if (oldSomeOne && !someOne) {
            if (obj.down) {
                obj.down = false;
                obj.object.onClick(nx, ny);
            }
            obj.object.onRelease(nx, ny);
        }
        // obj.object.onClick(nx, ny);
        // } else if (obj.over) {
        // notOver(obj);
        // }
    }

    protected static void loopGuiObject1() {
        guiObjects.startScan();
        MouseAssociation obj;
        while (guiObjects.next()) {
            obj = guiObjects.getCurrent();
            if (obj == null || obj.object == null) {
                continue;
            }
            if (testCollision(obj.object)) {
                guiMode = true;
                if (!obj.over) {
                    over(obj);
                }
                int nx = getX() - (int) obj.object.getXOnScreen();
                int ny = getY() - (int) obj.object.getYOnScreen();
                obj.object.onOver(nx, ny);
                if (someOne) {
                    obj.object.onDown(nx, ny);
                    obj.down = true;
                    if (oldSomeOne && (oldx != x || oldy != y)) {
                        obj.object.onDrag(x - oldx, y - oldy);
                    }
                }
                if (oldSomeOne && !someOne) {
                    if (obj.down) {
                        obj.down = false;
                        obj.object.onClick(nx, ny);
                    }
                    obj.object.onRelease(nx, ny);
                }
                // obj.object.onClick(nx, ny);
            } else if (obj.over) {
                notOver(obj);
            }
        }
    }

    private static void over(MouseAssociation ass) {
        ass.over = true;
        ass.object.onEnter();
    }

    private static void notOver(MouseAssociation ass) {
        ass.over = false;
        ass.down = false;
        ass.object.onExit();
    }

    public synchronized static void setXY(int x, int y) {
        org.lwjgl.input.Mouse.setCursorPosition(x, Core.getInstance().getHeight() - 1 - y);
    }

    public static int getDeltaX() {
        return oldx - x;
    }

    public static int getDeltaY() {
        return oldy - y;
    }

    /**
     * returns the x coordinate of mouse on the Camera<br>
     * <b>the coordinates are relative to the game's Camera system</b>
     * 
     * @return returns the x coordinate of mouse
     */
    public synchronized static int getX() {
        Camera camera = StaticRef.getCamera();
        return (int) ((float) x * camera.getZoom2D() * camera.getWidthFactor());
    }

    /**
     * returns the y coordinate of mouse on the Camera<br>
     * <b>the coordinates are relative to the game's Camera system</b>
     * 
     * @return returns the y coordinate of mouse
     */
    public synchronized static int getY() {
        Camera camera = StaticRef.getCamera();
        return (int) ((float) y * camera.getZoom2D() * camera.getHeightFactor());
    }

    /**
     * returns the x coordinate of mouse in the world<br>
     * <b>the coordinates are relative to the world's coordinate system</b>
     * 
     * @return returns the x coordinate of mouse in the world
     */
    public synchronized static float getXinWorld() {
        Camera camera = StaticRef.getCamera();
        return ((float) x * camera.getZoom2D() * camera.getWidthFactor() + camera.getX());
    }

    /**
     * returns the y coordinate of mouse in the world<br>
     * <b>the coordinates are relative to the world's coordinate system</b>
     * 
     * @return returns the y coordinate of mouse in the world
     */
    public synchronized static float getYinWorld() {
        Camera camera = StaticRef.getCamera();
        return ((float) y * camera.getZoom2D() * camera.getHeightFactor() + camera.getY());
    }

    /**
     * 
     * @return returns if the left button is down or not
     */
    public static boolean isLeftDown() {
        return left;
    }

    /**
     * 
     * @return returns if the right button is down or not
     */
    public static boolean isRightDown() {
        return right;
    }

    /**
     * 
     * @return returns if the middle button is down or not
     */
    public static boolean isMiddleDown() {
        return middle;
    }

    /**
     * 
     * @return returns if the left button is up or not
     */
    public static boolean isLeftUp() {
        return !left;
    }

    /**
     * 
     * @return returns if the right button is up or not
     */
    public static boolean isRightUp() {
        return !right;
    }

    /**
     * 
     * @return returns if the middle button is up or not
     */
    public static boolean isMiddleUp() {
        return !middle;
    }

    /**
     * 
     * @returns if the left button is released or not
     */
    public static boolean isLeftReleased() {
        return oldLeft && !left;
    }

    /**
     * 
     * @returns if the middle button is released or not
     */
    public static boolean isMiddleReleased() {
        return oldMiddle && !middle;
    }

    /**
     * 
     * @returns if the left button is released or not
     */
    public static boolean isRightReleased() {
        return oldRight && !right;
    }

    /**
     * 
     * @returns if the left button is pressed or not
     */
    public static boolean isLeftPressed() {
        return !oldLeft && left;
    }

    /**
     * 
     * @returns if the middle button is pressed or not
     */
    public static boolean isMiddlePressed() {
        return !oldMiddle && middle;
    }

    /**
     * returns the state of the Weel's mouse
     * 
     * @returns the state of the Weel's mouse
     */
    public static boolean isRightPressed() {
        return !oldRight && right;
    }

    public static int getWheelState() {
        if (org.lwjgl.input.Mouse.isCreated()) {
            return org.lwjgl.input.Mouse.getDWheel();
        }
        Utility.error(
                "getWheelState() called while the game engine's mouse isn't crated yet",
                "Mouse.getWheelState");
        return 0;
    }

    /**
     * only with LWJGL
     * 
     * @return returns if the indicates button number is down or not
     */
    public static boolean isButtonDown(int number) {

        if (org.lwjgl.input.Mouse.isCreated()) {
            return org.lwjgl.input.Mouse.isButtonDown(number);
        }
        Utility.error(
                "isButtonDown(int) called while the game engine's mouse isn't crated yet",
                "Mouse.isButtonDown(int)");
        return false;
    }

    public static void startDrag(IPlain2D obj) {
        Dragger.startDrag(obj, Dragger.LEFT);
    }

    public static boolean isOver(IPlain2D obj) {
        if (testRectangleCollision(obj)) {
            if (obj instanceof IMaskerable && obj instanceof ILayer) {
                if (testCollision(new PreciseClickableObjectWrapper(obj))) {
                    return true;
                }

            }
            return true;
        }
        return false;
    }        // TODO: SPEED UP THE CODE (removing the brute force searching)

    /**
     * returns a list of IPlain2D that are under the cursor<br>
     * caution: this is slow.. we should speed up the code (removing the brute
     * force testing)
     * 
     * @return a list of IPlain2D that are under the cursor
     */
    @Deprecated
    public static BaseList getOver() {
        // loop();
        GameList<IDrawing> bl = new GameList<IDrawing>();
        IPlain2D p;
        //IDrawing b;
        for (int i = 0; i < GameState.getCurrentGEState().getLayers().length; ++i) {
            /*StaticRef.layers[i].startScan();
            while (StaticRef.layers[i].next()) {
            b = StaticRef.layers[i].getCurrent();*/
            for (IDrawing b : GameState.getCurrentGEState().getLayers()[i]) {
                if (b instanceof IPlain2D) {
                    p = (IPlain2D) b;
                    if (testRectangleCollision(p)) {
                        if (p instanceof IMaskerable && p instanceof ILayer) {
                            if (testCollision((PreciseClickableObjectWrapper) p)) {
                                bl.add(b);
                            }

                        } else {
                            bl.add(b);
                        }
                    }
                }
            }
        }
        if (bl.size() > 0) {
            return bl;
        }
        return null;
    }

    public static ArrayList<IPlain2D> getOver(GameList<IPlain2D> list) {
        // loop();
        ArrayList<IPlain2D> bl = new ArrayList<IPlain2D>(list.size() / 2);
        IPlain2D p;
        for (int i = 0; i < list.size(); ++i) {
            p = list.get(i);
            if (testRectangleCollision(p)) {
                // System.out.println("found");
                bl.add(p);
            }
        }

        if (bl.size() > 0) {
            return bl;
        }
        return null;
    }

    public static ArrayList<IPlain2D> getOver(ArrayList<IPlain2D> list) {
        // loop();
        ArrayList<IPlain2D> bl = new ArrayList<IPlain2D>(list.size() / 2);
        IPlain2D p;
        for (int i = 0; i < list.size(); ++i) {
            p = list.get(i);
            if (testRectangleCollision(p)) {
                // System.out.println("found");
                bl.add(p);
            }
        }

        if (bl.size() > 0) {
            return bl;
        }
        return null;
    }

    /**
     * tests a collision with a IPlain2D and returns if this is under the
     * cursor.
     * 
     * @return returns if the IPlain2D is under the cursor
     */
    public synchronized static boolean testRectangleCollision(IPlain2D p) {
        final int xcam = getX();
        final int ycam = getY();
        if (xcam >= p.getXOnScreen() && ycam >= p.getYOnScreen() && xcam <= (p.getWidth() - 1) + p.getXOnScreen() && ycam <= (p.getHeight() - 1) + p.getYOnScreen()) {
            return true;
        }
        return false;
    }

    /**
     * tests a collision with a ISpriteColl and returns if this has got a
     * precise pixel collision with the cursor.
     * 
     * @returns returns true if the ISpriteColl has got a precise pixel
     *          collision with the mouse
     */
    public synchronized static <T extends IMaskerable & IPlain2D> boolean testCollision(
            T p) {
        Mask mask;
        if ((mask = p.getMask()) == null) {
            return testRectangleCollision((IPlain2D) p);
        }
        if (!testRectangleCollision((IPlain2D) p)) {
            return false;
        }
        if (mask.full) {
            return true;
        }
        int nx = getX() - (int) p.getXOnScreen();
        int ny = getY() - (int) p.getYOnScreen();
        if (mask.width != (float) p.getWidth() / StaticRef.getCamera().getZoom2D() || mask.height != (float) p.getHeight() / StaticRef.getCamera().getZoom2D()) {
            final float factorX = ((float) p.getWidth() / StaticRef.getCamera().getZoom2D()) / mask.width;
            final float factorY = ((float) p.getHeight() / StaticRef.getCamera().getZoom2D()) / mask.height;
            if (nx / factorX >= mask.width || ny / factorY >= mask.height) {
                return false;
            }
            return mask.mask[(int) (nx / factorX)][(int) (ny / factorY)];
        }
        //System.out.println("A");
        return mask.mask[nx][ny];
    }

    /**
     * adds an IClickable object to the mouse; note
     * 
     * @param plain
     *            the object
     */
    public synchronized static void add(IClickable plain) {
        MouseAssociation ass = new MouseAssociation(plain);
        objects.add(ass);
        //ass.object.getUsedLists().add(objects);
        Entry entry = objects.getEntry(ass);
        ass.object.getEntries().add(entry);
    }

    /**
     * removes a Gui IClickable object from the mouse
     * 
     * @param plain
     *            the object
     */
    public synchronized static void remove(IClickable plain) {
        objects.remove(plain);
    }

    /**
     * adds a Gui IClickable object to the mouse; note
     * 
     * @param plain
     *            the object
     */
    public synchronized static void addGuiObject(IContainer plain) {
        MouseAssociation ass = new MouseAssociation(plain);
        guiObjects.add(ass);
        //ass.object.getUsedLists().add(guiObjects);
        Entry entry = guiObjects.getEntry(ass);
        ass.object.getEntries().add(entry);
    }

    /**
     * removes an IClickable object from the mouse
     * 
     * @param plain
     *            the object
     */
    public synchronized static void removeGuiObject(IClickable plain) {
        guiObjects.remove(plain);
    }

    /**
     * hide the mouse
     * 
     */
    public synchronized static void hide() {
        if (Core.getInstance().getContainer() == null) {
            org.lwjgl.input.Mouse.setGrabbed(true);
        } else {
            Dimension d;
            Toolkit tk;
            if ((d = (tk = Toolkit.getDefaultToolkit()).getBestCursorSize(1, 1)).width == 0 || d.height == 0) {
                d.width = d.height = 1;
            }
            BufferedImage Bimage = new BufferedImage(d.width, d.height, 2);
            java.awt.Cursor cursor = tk.createCustomCursor(Bimage, new Point(0,
                    0), "");
            Core.getInstance().getContainer().setCursor(cursor);
        }
    }

    /**
     * shows the mouse
     * 
     */
    public synchronized static void show() {
        if (Core.getInstance().getContainer() == null) {
            org.lwjgl.input.Mouse.setGrabbed(false);
        } else {
            Core.getInstance().getContainer().setCursor(
                    java.awt.Cursor.getDefaultCursor());
        }
    }

    public static MouseState getMouseState() {
        return new MouseState(objects, guiObjects, lastGuiObjs);
    }

    public static void setMouseState(MouseState mouseState) {
        objects = mouseState.objects;
        guiObjects = mouseState.guiObjects;
        lastGuiObjs = mouseState.lastGuiObjs;
    }

    /**
     * sets a texture image to as the native cursor. note that the texture will
     * be converted to ARGB format from the RGBA original format. I advise you
     * to make a copy of this texture if you should be ute the same texture with
     * sprites or other stuff.
     * 
     * @param image
     *            the image to use as cursor image
     */
    public synchronized static void setCursor(ITexture image) {
        if (!useLwjgl) {
            Utility.error(
                    "this function don't works in the Swing/Awt+OpenGL combination!",
                    "Mouse.setCursor(ITexture)");
            return;
        }
        if (image.getWidth() > 32 || image.getHeight() > 32) {
            Utility.error("The image of cursor should be a 32x32 image",
                    "EWMouse.setCursor(Texture)");
        }

        // ---------- to ARGB format --------
        byte r, g, b, a;
        ByteBuffer bb = image.getData();
        for (int i = image.getHeightHW() * image.getHeightHW() - 1; i >= 0; --i) {
            bb.mark();
            r = bb.get(); // red
            g = bb.get(); // green
            b = bb.get(); // blue
            a = bb.get(); // alpha
            bb.reset();
            bb.put(a); // alpha
            bb.put(r); // red
            bb.put(g); // green
            bb.put(b); // blue
        }
        image.setData(bb);
        // --------- end to ARGB format ------
        try {
            Cursor cursor = new Cursor(image.getWidth(), image.getHeight(), 0,
                    image.getHeight() - 1, 1, image.getData().asIntBuffer(),
                    null);
            org.lwjgl.input.Mouse.setNativeCursor(cursor);
        } catch (LWJGLException e) {
            Utility.error("Error on setCursor(Texture) in EWMouse", e);
        }
    }

    public static boolean isGuiMode() {
        return guiMode;
    }

    public static void setGuiMode(boolean guiMode) {
        Mouse.guiMode = guiMode;
    }
}
