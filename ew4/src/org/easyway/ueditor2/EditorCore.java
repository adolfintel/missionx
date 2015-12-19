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
package org.easyway.ueditor2;

import org.easyway.lists.DrawingLayaredList;
import org.easyway.ueditor2.system.ObjectList;
import java.awt.Canvas;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

import org.easyway.debug.DebugManager;
import org.easyway.ueditor2.commands.CommandList;
import org.easyway.ueditor2.effects.Effect;
import org.easyway.ueditor2.effects.EffectList;
import org.easyway.input.PreciseClickableObjectWrapper;
import org.easyway.ueditor2.effects.MoveCamera;
import org.easyway.ueditor2.effects.SelectedEffect;
import org.easyway.input.Keyboard;
import org.easyway.input.Mouse;
import org.easyway.interfaces.extended.ILayer;
import org.easyway.interfaces.sprites.IMaskerable;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.objects.Camera;
import org.easyway.objects.sprites2D.SimpleSprite;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import org.easyway.system.state.OpenGLState;
import org.lwjgl.opengl.GL11;

public class EditorCore extends Core {

    private static final long serialVersionUID = -8805238930427486640L;
    /**
     * indicates if the grid is to draw or not 
     */
    public static boolean drawGrid = true;
    /**
     * indicates if the objects can be moved or not
     */
    public static boolean canMoveObject = true;
    /**
     * indicates if can select an object or not
     */
    public static boolean canSelect = true;
    /**
     * indicates if the camera can be moved or not
     */
    public static boolean canMoveCamera = true;
    /**
     * indicates if the snap to grid is enabled or not
     */
    public static boolean snapToGrid = false;
    /**
     * remembers the layers to show\hide
     */
    public static boolean showLayer[];
    protected static EditorCore thisInstance;
    /**
     * info. for the grid
     */
    private static int widthGrid = 100;
    /**
     * info for the grid
     */
    private static int heightGrid = 100;
    private static StartUEditor2 frame;
    private static ObjectList objectList;

    static {
        showLayer = new boolean[NUMBER_OF_LAYERS];
        for (int i = 0; i < NUMBER_OF_LAYERS; ++i) {
            showLayer[i] = true;
        }
    }

    public EditorCore(int width, int height, int bpp, Canvas container, StartUEditor2 frame) {
        super(width, height, bpp, container);
        thisInstance = this;
        EditorCore.frame = frame;
    }

    public static StartUEditor2 getMainFrame() {
        return frame;
    }

    public static ObjectList getObjectList() {
        return objectList;
    }

    @Override
    public void creation() {
        DebugManager.debug = true;
        //getCamera().setClearColor(0.3f, 0.3f, 0.3f, 0);
        //Texture.getTexture("images/red.bmp").setUseAlphaChannel(false);
        new Sprite().setImage("images/apple.png");
        objectList = new ObjectList();
        //frame.getBeanEditor().setBean(spr);
    }

    @Override
    public void coreRender() {
        OpenGLState.enableRendering();
        StaticRef.getCamera().getObjectsOnScreen().removeAll();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glLoadIdentity();
        final DrawingLayaredList[] layers = GameState.getCurrentGEState().getLayers();
        synchronized (layers) {
            // StaticRef.drawingList.render();
            for (int i = 0; i < layers.length; ++i) {
                if (layers[i] != null && showLayer[i]) {
                    layers[i].render();
                }
            }
        }
        render();
    }

    public void render() {
        //super.coreRender();
        GameState.getCurrentGEState().getMainGuiFather().render();
        drawGrid();
        EffectList.getSingelton().render();
        CommandList.render();
        loop();
        OpenGLState.disableRendering();
    }

    public void loop() {
        if (Mouse.isGuiMode()) {
            return;
        }

        pickupObject();
        moveObject();
        moveCamera();
        try {
            EffectList.getSingelton().loop();
            CommandList.loop();
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(getMainFrame(), "EXCEPTION:\n" + e.getMessage(), "EXCEPTION", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    protected void drawGrid() {
        if (!drawGrid || !canMoveCamera) {
            return;
        }
        int sx = widthGrid;
        int sy = heightGrid;
        if (sx < 10 || sy < 10) {
            return;
        }
        final int width = StaticRef.getCamera().getWidth() + sx * 2;
        final int height = StaticRef.getCamera().getHeight() + sy * 2;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(1);
        // GL11.glColor3f((float) Math.random() * 0.2f,
        // (float) Math.random() * 0.2f, (float) Math.random() * 0.2f);
        OpenGLState.disableBlending();
        GL11.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
        GL11.glBegin(GL11.GL_LINES);
        {
            for (int x = -(int) StaticRef.getCamera().x % sx; x < width; x += sx) {
                GL11.glVertex2f(x, 0);
                GL11.glVertex2f(x, height);
            }
            for (int y = -(int) StaticRef.getCamera().y % sy; y < height; y += sy) {
                GL11.glVertex2f(0, y);
                GL11.glVertex2f(width, y);
            }

            if (StaticRef.getCamera().x <= 0 && StaticRef.getCamera().x + StaticRef.getCamera().getWidth() > 0) {
                GL11.glColor3f(1.0f, 0.5f, 0.5f);
                GL11.glVertex2f((int) -StaticRef.getCamera().x, 0);
                GL11.glVertex2f((int) -StaticRef.getCamera().x, height);
            }
            if (StaticRef.getCamera().y <= 0 && StaticRef.getCamera().y + StaticRef.getCamera().getHeight() > 0) {
                GL11.glColor3f(1.0f, 0.5f, 0.5f);
                GL11.glVertex2f(0, (int) -StaticRef.getCamera().y);
                GL11.glVertex2f(width, (int) -StaticRef.getCamera().y);
            }
        }
        GL11.glEnd();
        /*
         * GL11.glLineWidth(0.1f); sx /= 2; sy /= 2;
         * GL11.glBegin(GL11.GL_LINES); for (int x = -(int)
         * StaticRef.getCamera().x % sx; x < width; x += sx) {
         * GL11.glVertex2f(x, 0); GL11.glVertex2f(x, height); } for (int y =
         * -(int) StaticRef.getCamera().y % sy; y < height; y += sy) {
         * GL11.glVertex2f(0, y); GL11.glVertex2f(width, y); } GL11.glEnd();
         */
        OpenGLState.enableBlending();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
    // ------------------------------
    // ------------------------------
    protected float startx, starty;
    protected transient MoveCamera moveCameraSprite;

    public void moveCamera() {
        if (!canMoveCamera) {
            return;
        }
        Camera camera = StaticRef.getCamera();
        float fac = camera.getZoom2D();
        if (!Mouse.isRightDown()) { // right up, pressed, released
            startx = Mouse.getXinWorld();
            starty = Mouse.getYinWorld();
            if (moveCameraSprite != null) {
                moveCameraSprite.destroy();
                moveCameraSprite = null;
            }
        } else {// right down
            if (moveCameraSprite == null) {
                moveCameraSprite = new MoveCamera(Mouse.getXinWorld(), Mouse.getYinWorld());
            }
            camera.move(startx - Mouse.getXinWorld(), starty - Mouse.getYinWorld());
        }
        if (Keyboard.isKeyReleased(Keyboard.KEY_PRIOR)) {
            camera.setZoom2D(fac + 0.03f);
        } else if (Keyboard.isKeyReleased(Keyboard.KEY_NEXT)) {
            camera.setZoom2D(fac - 0.03f);
        }
    }    // ------------------------------
    // ------------------------------

    public static EditorCore getInstance() {
        return thisInstance;
    }
    //protected IPlain2D attracked;
    protected Object objSelected;
    protected IPlain2D selectedPlain;
    protected float incx, incy;

    public void pickupObject() {
        if (!canSelect) {
            return;
        }


        ArrayList<IPlain2D> list;
        if (Mouse.isLeftPressed() && canSelect) { // select the object
            list = Mouse.getOver(StaticRef.getCamera().getObjectsOnScreen());
            if (list == null) {
                setSelected(null);
                return;
            }
            //System.out.println("SIZE LIST:" + list.size());
            if (list.size() == 1) {
                setSelected(list.get(0));
                //selected = list.get(0);

                // attracked = list.get(0);
                // incx = attracked.getX() - Mouse.getXinWorld();
                // incy = attracked.getY() - Mouse.getYinWorld();
            } else if (list.size() > 1) {

                // complex
                PreciseClickableObjectWrapper clickobj = new PreciseClickableObjectWrapper(null);
                IPlain2D less = null;
                int lastlayer = Integer.MIN_VALUE;

                for (int i = 0; i < list.size(); ++i) {
                    // System.out.println("trying");
                    if (list.get(i) instanceof Effect) {
                        continue;
                    }
                    if (list.get(i) instanceof ILayer) {

                        if (list.get(i) instanceof IMaskerable) {
                            clickobj.obj = list.get(i);
                            if (Mouse.testCollision(clickobj)) {
                                if (lastlayer <= clickobj.getLayer()) {
                                    // System.out.println("less finded");
                                    lastlayer = clickobj.getLayer();
                                    less = list.get(i);
                                }
                            }
                        } else {// not maskerable
                            if (lastlayer <= ((ILayer) list.get(i)).getLayer()) {
                                // System.out.println("less finded");
                                lastlayer = ((ILayer) list.get(i)).getLayer();
                                less = list.get(i);
                            }
                        }

                    }
                }// end for (search clickables objs)

                if (less != null) {
                    setSelected(less);
                }
                //new SelectInstance(list, less);
            }
        }
    }

    public void moveObject() {
        if (!canMoveObject || !canSelect) {
            return;
        }
        if ((selectedPlain != null)) {
            //setSelected(attracked);
            if (Mouse.isMiddlePressed() && Mouse.isOver(selectedPlain)) {
                incx = selectedPlain.getX() - Mouse.getXinWorld();
                incy = selectedPlain.getY() - Mouse.getYinWorld();
            }
            if (Mouse.isMiddleDown()) { // move the object
                selectedPlain.setXY(Mouse.getXinWorld() + incx, Mouse.getYinWorld() + incy);
                return;
            }
            /*else {
            attracked = null;
            }*/
        }


    }    // --------------------

    public Object cloneObject(Object obj) throws Exception {

        Constructor constr = obj.getClass().getConstructor(obj.getClass());
        Object clObj = constr.newInstance(obj);
        return clObj;

    }
    SelectedEffect selectedEffect;

    public void setSelected(Object selected) {



        if (selected == null) {
            frame.getBeanEditor().setBean(null);
            if (selectedEffect != null) {
                selectedEffect.destroy();
                selectedEffect = null;
            }
            selectedPlain = null;
            objSelected = null;
            return;
        }
        if (objSelected == selected) {
            return;
        }
        if (selected instanceof Effect) {
            return;
        }
        objSelected = selected;
        frame.getBeanEditor().setBean(objSelected);

        if (selectedEffect != null) {
            selectedEffect.destroy();
        }

        if (selected instanceof IPlain2D) {
            selectedPlain = (IPlain2D) selected;
            selectedEffect = new SelectedEffect(selectedPlain);
        } else {

            selectedPlain = null;
        }
    }

    public Object getSelected() {
        return objSelected;
    }

    public static void disableInteraction() {
        EditorCore.setCanMoveCamera(false);
        EditorCore.setCanMoveObject(false);
        EditorCore.setCanSelect(false);
    }

    public static void enableInteraction() {
        EditorCore.setCanMoveCamera(true);
        EditorCore.setCanMoveObject(true);
        EditorCore.setCanSelect(true);
    }

    // --------------------
    public static void setCanMoveCamera(boolean value) {
        // TODO
        //UEditor.getNavigator().setEnabled(value);
        canMoveCamera = value;
    }

    public static void setCanMoveObject(boolean value) {
        canMoveObject = value;
    }

    public static boolean isCanSelect() {
        return canSelect;
    }

    public static void setCanSelect(boolean canSelect) {
        EditorCore.canSelect = canSelect;
    }

    public static int getHeightGrid() {
        return heightGrid;
    }

    public static void setHeightGrid(int heightGrid) {
        if (heightGrid <= 0) {
            return;
        }
        EditorCore.heightGrid = heightGrid;
    }

    public static int getWidthGrid() {
        return widthGrid;
    }

    public static void setWidthGrid(int widthGrid) {
        if (widthGrid <= 0) {
            return;
        }
        EditorCore.widthGrid = widthGrid;
    }
    // @Method("Start Game")//
    // public static void startGame() {
    // DebugManager.debug = false;
    // }
}
