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
package org.easyway.collisions;

import java.awt.Point;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;

import org.easyway.collisions.quad.QuadTree;
import org.easyway.interfaces.base.ITexture;
import org.easyway.interfaces.sprites.ICollisionable;
import org.easyway.interfaces.sprites.IMaskerable;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.objects.Camera;
import org.easyway.objects.Plain2D;
import org.easyway.objects.sprites2D.Mask;
import org.easyway.objects.sprites2D.StaticFullyMask;
import org.easyway.objects.texture.Texture;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.tiles.TileSprite;
import org.easyway.utils.Utility;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;

/**
 * Collisions permette di identificare l'eventualit� che 2 sprite vadano in
 * collisione
 */
public class CollisionUtils {

    static IntBuffer queryBuffer = null;
    static int queryID = -1;
    //static TextureFBO collisionTexture;

    public static void init() {
        if (Core.getGLIntVersion() >= 15) {
            queryBuffer = BufferUtils.createIntBuffer(1);
            GL15.glGenQueries(queryBuffer);
            queryID = queryBuffer.get();
        } else {
            Texture.autoCreateMask = true;
        }
    }

    public static boolean contains(IPlain2D containerSpr, IPlain2D spr2) {

        double x1 = containerSpr.getX();
        double x2 = spr2.getX();
        double y1 = containerSpr.getY();
        double y2 = spr2.getY();

        return x2 >= x1 && x2 + spr2.getWidth() <= x1 + containerSpr.getWidth()
                && y2 >= y1 && y2 + spr2.getHeight() <= y1 + containerSpr.getHeight();
    }

    /**
     * controlla se si � verificata una collisione rettangolare tra gli sprite
     * spr1 e spr2
     */
    public static boolean rectangleHit(IPlain2D spr1, IPlain2D spr2) {
        if (spr1 == null || spr2 == null) {
            Utility.error("Null Sprite!", "Collisions.trueHit(IPlain,IPlain)");
            return false;
        }
        double x1 = spr1.getX();
        double x2 = spr2.getX();
        double y1 = spr1.getY();
        double y2 = spr2.getY();

        return -x1 + x2 + spr2.getWidth() >= 0
                && x1 + spr1.getWidth() - x2 >= 0
                && -y1 + y2 + spr2.getHeight() >= 0
                && y1 + spr1.getHeight() - y2 >= 0;
    }

    public static IPlain2D getRectangleHit(IPlain2D spr1, IPlain2D spr2) {
        float x1 = spr1.getX();
        float x2 = spr2.getX();
        float y1 = spr1.getY();
        float y2 = spr2.getY();
        float xs = x1 > x2 ? x1 : x2;
        float ys = y1 > y2 ? y1 : y2;
        return new Plain2D(xs, ys,
                (int) (Math.min(x1 + spr1.getWidth(), x2 + spr2.getWidth()) - xs),
                (int) (Math.min(y1 + spr1.getHeight(), y2 + spr2.getHeight()) - ys));
    }

    public static boolean circleHit(IPlain2D spr1, IPlain2D spr2) {
        if (spr1 == null || spr2 == null) {
            throw new NullPointerException();
        }
        // final variables for a little speed up of the code..
        final float H1 = spr1.getHeight();
        final float H2 = spr2.getHeight();
        final float W1 = spr1.getWidth();
        final float W2 = spr2.getWidth();
        // r1 stand for r^2 of sprite1
        float r1 = (H1 * H1 + W1 * W1) / 4;
        // r2 stand for r^2 of sprite2
        float r2 = (H2 * H2 + W2 * W2) / 4;
        // dsquared is (x1 + w1/2 - x2 - w2/2)^2 + (y1 + h1/2 - y2 - h2/2)^2
        // dsquared is the squared distance between the two sprites
        float dsquared = spr1.getX() + W1 / 2 - spr2.getX() - W2 / 2;
        dsquared *= dsquared;
        float d2 = spr1.getY() + H1 / 2 - spr2.getY() - H2 / 2;
        dsquared += d2 * d2;

        // cause: d = r_sprite1 + r_sprite2
        // --> d^2 = (r_sprite1 + r_sprite2)^2
        // --> d^2 = R1 + R2 + 2sqrt(R1*R2)
        // where:
        // R1 = r_sprite1^2
        // R2 = r_sprite2^2
        if (dsquared < r1 + r2 + 2 * Math.sqrt(r1 * r2)) {
            return true;
        }
        return false;
    }

    /**
     * returns the rectangle that contains the circle circonscritte on the plain
     * @param plain
     * @return a rectangle
     */
    public static IPlain2D getCircleRectangle(IPlain2D plain) {
        // final variables for a little speed up of the code..
        final float H1 = plain.getHeight();
        final float W1 = plain.getWidth();
        // r1 stand for r^2 of sprite1
        float R2 = (float) Math.sqrt(H1 * H1 + W1 * W1);
        float R = R2 / 2.0f;
        float newX = plain.getX() + W1 / 2 - R;
        float newY = plain.getY() + H1 / 2 - R;
        return new Plain2D(newX, newY, (int) (R2), (int) (R2));
    }

    public static void hitted(ISpriteColl src, ISpriteColl dest) {
        if (!src.isAddedToCollisionList()) {
            StaticRef.collisionableLoopList.add((ICollisionable) src);
            src.setAddedToCollisionList(true);
        }
        if (!dest.isAddedToCollisionList()) {
            StaticRef.collisionableLoopList.add((ICollisionable) dest);
            dest.setAddedToCollisionList(true);
        }

        ArrayList<ISpriteColl> collList = src.getCollisionList();
        if (!QuadTree.isUsingQuadTree() || !collList.contains(dest)) { // fix for quadNode
            collList.add(dest);
            collList = dest.getCollisionList();
            assert collList.contains(src) == false;
            collList.add(src);
        }
    }
    static IntBuffer outBuffer = BufferUtils.createIntBuffer(1);
    static float stepZ = 1f;
    static float lastStepZ = 100f;
    static float initialStepZ = 100f;

    public static boolean trueHardWareHit(IHardwareCollisionable home, IHardwareCollisionable dest) {
        return trueHardWareHitCount(home, dest) > 0;
    }

    public static int trueHardWareHitCount(IHardwareCollisionable home, IHardwareCollisionable dest) {
        if (Core.getGLIntVersion() < 15) {
            return trueHit((ISpriteColl)home,(ISpriteColl)dest)?1:0; //Todo: should cound pixels!!!!

        }
        /*if (collisionTexture != null) {
        collisionTexture.startDrawing();
        }*/

        boolean initial = Camera.getCurrentCamera().isCollectingObjectsOnScreen();
        float minx = Math.min(home.getX(), dest.getX());
        float miny = Math.min(home.getY(), dest.getY());
        Camera.getCurrentCamera().setCollectingObjectsOnScreen(false);

        //IPlain2D plain = getRectangleHit(getCircleRectangle((IPlain2D) home), getCircleRectangle((IPlain2D) dest));
        //OpenGLState.enableScissor((int) (plain.getX() - minx), (int) (plain.getY() - miny), plain.getWidth(), plain.getHeight());

        GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        //++stencilData; // little speed up?
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 1);

        //((IRender)home).render();
        home.renderAt(home.getX() - minx, home.getY() - miny, 0, GL13.GL_TEXTURE0);

        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 1);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
        GL15.glBeginQuery(GL15.GL_SAMPLES_PASSED, queryID);
        //((IRender)dest).render();
        dest.renderAt(dest.getX() - minx, dest.getY() - miny, 0, GL13.GL_TEXTURE0);

        GL15.glEndQuery(GL15.GL_SAMPLES_PASSED);
        GL15.glGetQueryObject(queryID, GL15.GL_QUERY_RESULT, (IntBuffer) outBuffer.rewind());

        //OpenGLState.disableScissor();
        Camera.getCurrentCamera().setCollectingObjectsOnScreen(initial);


        /*if (collisionTexture != null) {
        collisionTexture.endDrawing();
        }*/
        return outBuffer.get();
    }

    public static boolean trueHit(ISpriteColl home, ISpriteColl dest) {
        Mask hmask, dmask;
        if ((hmask = (Mask) home.getCollisionData()) == null) {
            hmask = new StaticFullyMask(home.getWidth(), home.getHeight());
            /*Utility.error("Null Masks! - " + home.toString() + " AUTO-FIXING",
                    "Collisions.trueHit(IPlain,IPlain)");*/
        }
        if ((dmask = (Mask) dest.getCollisionData()) == null) {
            dmask = new StaticFullyMask(dest.getWidth(), dest.getHeight());
            /*Utility.error("Null Masks! - " + dest.toString() + " AUTO-FIXING",
                    "Collisions.trueHit(IPlain,IPlain,Mask,Mask)");*/
        }
        return trueHit(home, dest, hmask, dmask);
    }

    /**
     * check a collision between two sprites
     */
    public static boolean trueHit(IPlain2D home, IPlain2D dest, Mask mhome,
            Mask mdest) {
        if (home == null || dest == null) {
            Utility.error("Null Plain!",
                    "Collisions.trueHit(IPlain,IPlain,Mask,Mask)");
            return false;
        }
        if (mhome == null || mdest == null) {
            Utility.error("Null Masks! - " + home.toString() + " " + dest.toString(),
                    "Collisions.trueHit(IPlain,IPlain,Mask,Mask)");
            return false;
        }

        if (mhome.full && mdest.full) {
            return true;
        }

        /** resized */
        if (home.getWidth() != mhome.width || home.getHeight() != mhome.height
                || dest.getWidth() != mdest.width || dest.getHeight() != mdest.height) {
            return trueHitResized(home, dest, mhome, mdest);
        }

        // coordinate
        final double x = home.getX();
        final double y = home.getY();
        final double x1 = dest.getX();
        final double y1 = dest.getY();
        // partenza e arrivo sezione in comune
        final int xstart = (int) Math.max(x, x1);
        final int ystart = (int) Math.max(y, y1);
        final int xend = (int) Math.min(x + mhome.width, x1 + mdest.width);
        final int yend = (int) Math.min(y + mhome.height, y1 + mdest.height);
        final int Toty = Math.abs(yend - ystart);
        final int Totx = Math.abs(xend - xstart);
        // partenza zona comune relativa
        final int xstarth = Math.abs(xstart - (int) x);
        final int ystarth = Math.abs(ystart - (int) y);
        final int xstartd = Math.abs(xstart - (int) x1);
        final int ystartd = Math.abs(ystart - (int) y1);
        // variabili di ciclo
        int X, Y;
        int ny, ny1, nx, nx1;
        if (!(mhome.full || mdest.full)) {
            for (Y = 0; Y < Toty; Y++) {
                ny = ystarth + Y;
                ny1 = ystartd + Y;
                for (X = 0; X < Totx; X++) {
                    nx = xstarth + X;
                    nx1 = xstartd + X;
                    if (mhome.mask[nx][ny] && mdest.mask[nx1][ny1]) {
                        return true; // collision!
                    }
                }
            }
        } else {
            Mask mask = mhome.full ? mdest : mhome;
            final int ys = mhome.full ? ystartd : ystarth;
            final int xs = mhome.full ? xstartd : xstarth;
            for (Y = 0; Y < Toty; Y++) {
                ny = ys + Y;
                for (X = 0; X < Totx; X++) {
                    nx = xs + X;
                    if (mask.mask[nx][ny]) {
                        return true; // collision!
                    }
                }
            }
        }
        return false; // no collision!
    }

    /**
     * check a collision between two sprites and return the first collision point
     */
    public static Point getSoftwarePointHT(IPlain2D home, IPlain2D dest, Mask mhome, Mask mdest) {
        if (home == null || dest == null) {
            Utility.error("Null Plain!",
                    "Collisions.trueHit(IPlain,IPlain,Mask,Mask)");
            return null;
        }
        if (mhome == null || mdest == null) {
            Utility.error("Null Masks! - " + home.toString() + " " + dest.toString(),
                    "Collisions.trueHit(IPlain,IPlain,Mask,Mask)");
            return null;
        }

        if (mhome.full && mdest.full) {
            return new Point((int) Math.max(home.getX(), dest.getX()),
                    (int) Math.max(home.getY(), dest.getY()));
        }
        // coordinate
        final int x = (int) home.getX();
        final int y = (int) home.getY();
        final int x1 = (int) dest.getX();
        final int y1 = (int) dest.getY();
        // partenza e arrivo sezione in comune
        final int xstart = (int) Math.max(x, x1);
        final int ystart = (int) Math.max(y, y1);
        final int xend = (int) Math.min(x + mhome.width, x1 + mdest.width);
        final int yend = (int) Math.min(y + mhome.height, y1 + mdest.height);
        final int Toty = Math.abs(yend - ystart);
        final int Totx = Math.abs(xend - xstart);
        // partenza zona comune relativa
        final int xstarth = Math.abs(xstart - (int) x);
        final int ystarth = Math.abs(ystart - (int) y);
        final int xstartd = Math.abs(xstart - (int) x1);
        final int ystartd = Math.abs(ystart - (int) y1);
        // variabili di ciclo
        int X, Y;
        int ny, ny1, nx, nx1;
        if (!(mhome.full || mdest.full)) {
            for (Y = 0; Y < Toty; ++Y) {
                ny = ystarth + Y;
                ny1 = ystartd + Y;
                for (X = 0; X < Totx; ++X) {
                    nx = xstarth + X;
                    nx1 = xstartd + X;
                    if (mhome.mask[nx][ny] && mdest.mask[nx1][ny1]) {
                        return new Point(xstart + X, ystart + Y); // collision!
                    }
                }
            }
        } else {
            Mask mask = mhome.full ? mdest : mhome;
            final int ys = mhome.full ? ystartd : ystarth;
            final int xs = mhome.full ? xstartd : xstarth;
            for (Y = 0; Y < Toty; ++Y) {
                ny = ys + Y;
                for (X = 0; X < Totx; ++X) {
                    nx = xs + X;
                    if (mask.mask[nx][ny]) {
                        return new Point(xstart + X, ystart + Y); // collision!
                    }
                }
            }
        }
        return null; // no collision!
    }

    /**
     * check a collision between two sprites and return the first collision point
     */
    public static Point getSoftwarePointHB(IPlain2D home, IPlain2D dest, Mask mhome, Mask mdest) {
        if (home == null || dest == null) {
            Utility.error("Null Plain!",
                    "Collisions.trueHit(IPlain,IPlain,Mask,Mask)");
            return null;
        }
        if (mhome == null || mdest == null) {
            Utility.error("Null Masks! - " + home.toString() + " " + dest.toString(),
                    "Collisions.trueHit(IPlain,IPlain,Mask,Mask)");
            return null;
        }

        if (mhome.full && mdest.full) {
            return new Point((int) Math.max(home.getX(), dest.getX()),
                    (int) Math.max(home.getY(), dest.getY()));
        }
        // coordinate
        final int x = (int) home.getX();
        final int y = (int) home.getY();
        final int x1 = (int) dest.getX();
        final int y1 = (int) dest.getY();
        // partenza e arrivo sezione in comune
        final int xstart = (int) Math.max(x, x1);
        final int ystart = (int) Math.max(y, y1);
        final int xend = (int) Math.min(x + mhome.width, x1 + mdest.width);
        final int yend = (int) Math.min(y + mhome.height, y1 + mdest.height);
        final int Toty = Math.abs(yend - ystart);
        final int Totx = Math.abs(xend - xstart);
        // partenza zona comune relativa
        final int xstarth = Math.abs(xstart - (int) x);
        final int ystarth = Math.abs(ystart - (int) y);
        final int xstartd = Math.abs(xstart - (int) x1);
        final int ystartd = Math.abs(ystart - (int) y1);
        // variabili di ciclo
        int X, Y;
        int ny, ny1, nx, nx1;
        if (!(mhome.full || mdest.full)) {
            for (Y = Toty - 1; Y >= 0; --Y) {
                ny = ystarth + Y;
                ny1 = ystartd + Y;
                for (X = 0; X < Totx; ++X) {
                    nx = xstarth + X;
                    nx1 = xstartd + X;
                    if (mhome.mask[nx][ny] && mdest.mask[nx1][ny1]) {
                        return new Point(xstart + X, ystart + Y); // collision!
                    }
                }
            }
        } else {
            Mask mask = mhome.full ? mdest : mhome;
            final int ys = mhome.full ? ystartd : ystarth;
            final int xs = mhome.full ? xstartd : xstarth;
            for (Y = Toty - 1; Y >= 0; --Y) {
                ny = ys + Y;
                for (X = 0; X < Totx; ++X) {
                    nx = xs + X;
                    if (mask.mask[nx][ny]) {
                        return new Point(xstart + X, ystart + Y); // collision!
                    }
                }
            }
        }
        return null; // no collision!
    }

    /**
     * check a collision between two sprites and return the first collision point
     */
    public static Point getSoftwarePointVL(IPlain2D home, IPlain2D dest, Mask mhome, Mask mdest) {
        if (home == null || dest == null) {
            Utility.error("Null Plain!",
                    "Collisions.trueHit(IPlain,IPlain,Mask,Mask)");
            return null;
        }
        if (mhome == null || mdest == null) {
            Utility.error("Null Masks! - " + home.toString() + " " + dest.toString(),
                    "Collisions.trueHit(IPlain,IPlain,Mask,Mask)");
            return null;
        }

        if (mhome.full && mdest.full) {
            return new Point((int) Math.max(home.getX(), dest.getX()),
                    (int) Math.max(home.getY(), dest.getY()));
        }
        // coordinate
        final int x = (int) home.getX();
        final int y = (int) home.getY();
        final int x1 = (int) dest.getX();
        final int y1 = (int) dest.getY();
        // partenza e arrivo sezione in comune
        final int xstart = (int) Math.max(x, x1);
        final int ystart = (int) Math.max(y, y1);
        final int xend = (int) Math.min(x + mhome.width, x1 + mdest.width);
        final int yend = (int) Math.min(y + mhome.height, y1 + mdest.height);
        final int Toty = Math.abs(yend - ystart);
        final int Totx = Math.abs(xend - xstart);
        // partenza zona comune relativa
        final int xstarth = Math.abs(xstart - (int) x);
        final int ystarth = Math.abs(ystart - (int) y);
        final int xstartd = Math.abs(xstart - (int) x1);
        final int ystartd = Math.abs(ystart - (int) y1);
        // variabili di ciclo
        int X, Y;
        int ny, ny1, nx, nx1;
        if (!(mhome.full || mdest.full)) {
            for (X = 0; X < Totx; ++X) {
                nx = xstarth + X;
                nx1 = xstartd + X;
                for (Y = 0; Y < Toty; ++Y) {
                    ny = ystarth + Y;
                    ny1 = ystartd + Y;
                    //for (X = 0; X < Totx; X++) {
                    //   nx = xstarth + X;
                    //   nx1 = xstartd + X;
                    if (mhome.mask[nx][ny] && mdest.mask[nx1][ny1]) {
                        return new Point(xstart + X, ystart + Y); // collision!
                    }
                }
            }
        } else {
            Mask mask = mhome.full ? mdest : mhome;
            final int ys = mhome.full ? ystartd : ystarth;
            final int xs = mhome.full ? xstartd : xstarth;
            for (X = 0; X < Totx; X++) {
                nx = xs + X;
                for (Y = 0; Y < Toty; Y++) {
                    ny = ys + Y;
                    if (mask.mask[nx][ny]) {
                        return new Point(xstart + X, ystart + Y); // collision!
                    }
                }
            }
        }
        return null; // no collision!
    }

    /**
     * check a collision between two sprites and return the first collision point
     */
    public static Point getSoftwarePointVR(IPlain2D home, IPlain2D dest, Mask mhome, Mask mdest) {
        if (home == null || dest == null) {
            Utility.error("Null Plain!",
                    "Collisions.trueHit(IPlain,IPlain,Mask,Mask)");
            return null;
        }
        if (mhome == null || mdest == null) {
            Utility.error("Null Masks! - " + home.toString() + " " + dest.toString(),
                    "Collisions.trueHit(IPlain,IPlain,Mask,Mask)");
            return null;
        }

        if (mhome.full && mdest.full) {
            return new Point((int) Math.max(home.getX(), dest.getX()),
                    (int) Math.max(home.getY(), dest.getY()));
        }
        // coordinate
        final int x = (int) home.getX();
        final int y = (int) home.getY();
        final int x1 = (int) dest.getX();
        final int y1 = (int) dest.getY();
        // partenza e arrivo sezione in comune
        final int xstart = (int) Math.max(x, x1);
        final int ystart = (int) Math.max(y, y1);
        final int xend = (int) Math.min(x + mhome.width, x1 + mdest.width);
        final int yend = (int) Math.min(y + mhome.height, y1 + mdest.height);
        final int Toty = Math.abs(yend - ystart);
        final int Totx = Math.abs(xend - xstart);
        // partenza zona comune relativa
        final int xstarth = Math.abs(xstart - (int) x);
        final int ystarth = Math.abs(ystart - (int) y);
        final int xstartd = Math.abs(xstart - (int) x1);
        final int ystartd = Math.abs(ystart - (int) y1);
        // variabili di ciclo
        int X, Y;
        int ny, ny1, nx, nx1;
        if (!(mhome.full || mdest.full)) {
            for (X = Totx - 1; X >= 0; --X) {
                nx = xstarth + X;
                nx1 = xstartd + X;
                for (Y = 0; Y < Toty; ++Y) {
                    ny = ystarth + Y;
                    ny1 = ystartd + Y;
                    //for (X = 0; X < Totx; X++) {
                    //   nx = xstarth + X;
                    //   nx1 = xstartd + X;
                    if (mhome.mask[nx][ny] && mdest.mask[nx1][ny1]) {
                        return new Point(xstart + X, ystart + Y); // collision!
                    }
                }
            }
        } else {
            Mask mask = mhome.full ? mdest : mhome;
            final int ys = mhome.full ? ystartd : ystarth;
            final int xs = mhome.full ? xstartd : xstarth;
            for (X = Totx - 1; X >= 0; --X) {
                nx = xs + X;
                for (Y = 0; Y < Toty; ++Y) {
                    ny = ys + Y;
                    if (mask.mask[nx][ny]) {
                        return new Point(xstart + X, ystart + Y); // collision!
                    }
                }
            }
        }
        return null; // no collision!
    }

    public static boolean trueHitAlpha(ISpriteColl home, ISpriteColl dest,
            float alpha, float beta, ITexture imgHome, ITexture imgDest) {
        if (imgHome != null) {
            if (imgDest != null) {
                return newAlphaTest(home, dest, (Mask) home.getCollisionData(), (Mask) dest.getCollisionData(), alpha, beta,
                        imgHome.getXStart(), imgHome.getXEnd(), imgHome.getYStart(), imgHome.getYEnd(),
                        imgDest.getXStart(), imgDest.getXEnd(), imgDest.getYStart(), imgDest.getYEnd());
            }

            return newAlphaTest(home, dest, (Mask) home.getCollisionData(), (Mask) dest.getCollisionData(), alpha, beta,
                    imgHome.getXStart(), imgHome.getXEnd(), imgHome.getYStart(), imgHome.getYEnd(),
                    0, 1, 0, 1);
        }
        if (imgDest != null) {
            return newAlphaTest(home, dest, (Mask) home.getCollisionData(), (Mask) dest.getCollisionData(), alpha, beta,
                    0, 1, 0, 1,
                    imgDest.getXStart(), imgDest.getXEnd(), imgDest.getYStart(), imgDest.getYEnd());
        }

        return newAlphaTest(home, dest, (Mask) home.getCollisionData(), (Mask) dest.getCollisionData(), alpha, beta,
                0, 1, 0, 1, 0, 1, 0, 1);
    }

    /**
     * A PRECISE test collision<br>
     * O( n + m ) where: n = |mhome.mask|, m = |mdest.mask|
     *
     * @param home
     * @param dest
     * @param mhome
     * @param mdest
     * @param alpha
     * @param beta
     * @return
     */
    public static boolean newAlphaTest(IPlain2D home, IPlain2D dest,
            Mask mhome, Mask mdest, float alpha, float beta,
            float startx1, float endx1, float startx2, float endx2,
            float starty1, float endy1, float starty2, float endy2) {
        // degree to radiant
        // alpha = -alpha;
        // beta = -beta;
        alpha *= rad;
        beta *= rad;
        // direct object's coordinates
        final int x = (int) home.getX();
        final int y = (int) home.getY();
        final int x1 = (int) dest.getX();
        final int y1 = (int) dest.getY();
        // direct object's sizes
        final int width = home.getWidth();
        final int height = home.getHeight();
        final int width1 = dest.getWidth();
        final int height1 = dest.getHeight();
        // min x\y
        final int xstart = (int) Math.min(x, x1);
        final int ystart = (int) Math.min(y, y1);
        // x\y relative to the min x\y
        // the relativex\y will be = 0 or > 0
        final int relativex = x - xstart;
        final int relativex1 = x1 - xstart;
        final int relativey = y - ystart;
        final int relativey1 = y1 - ystart;
        // diag of objects
        final int diag = (int) Math.sqrt(width * width + height * height);
        final int diag1 = (int) Math.sqrt(width1 * width1 + height1 * height1);
        // total area that can be considered in the collision
        final int totX = diag + diag1;// I add 2 for the error
        final int totY = diag + diag1;// I add 4 for the error
        // make the area
        int area[][] = new int[totX][totY];
        // center of objects
        final int acx = diag / 2 + relativex;
        final int acy = diag / 2 + relativey;
        final int acx1 = diag1 / 2 + relativex1;
        final int acy1 = diag1 / 2 + relativey1;
        // middle width\height
        final int cx = width / 2;
        final int cy = height / 2;
        final int cx1 = width1 / 2;
        final int cy1 = height1 / 2;
        // factor scale
        final int incx1 = (int) (mhome.width * startx1);
        final int incy1 = (int) (mhome.height * starty1);
        final int incx2 = (int) (mdest.width * startx2);
        final int incy2 = (int) (mdest.height * starty2);
        final float scalex = (float) width / (mhome.width * endx1 - incx1);//(float) mhome.width;
        final float scaley = (float) height / (mhome.height * endy1 - incy1);
        final float scalex1 = (float) width1 / (mdest.width * endx2 - incx2);
        final float scaley1 = (float) height1 / (mdest.height * endy2 - incy2);

        //
        int i = 0, j = 0, i1 = 0, j1 = 0;
        int px, py;
        int npx;

        boolean noend = true, noend1 = true;
        final double sinalpha = Math.sin(alpha);
        final double cosalpha = Math.cos(alpha);
        final double sinbeta = Math.sin(beta);
        final double cosbeta = Math.cos(beta);
        while (true) {
            if (noend) {
                if (mhome.mask[(int) (i / scalex)][(int) (j / scaley)]) {
                    npx = px = i - cx;
                    py = j - cy;
                    // if (alpha != 0) {
                    npx = (int) Math.round(((double) px * cosalpha - (double) py * sinalpha));
                    py = (int) Math.round(((double) py * cosalpha + (double) px * sinalpha));
                    // }
                    npx += acx;
                    py += acy;
                    //if (py < totY && npx < totX) {

                    if (area[npx][py] == 2) {
                        // print(area, totX, totY);
                        // System.out.println("==2");
                        return true; // collision!
                    }
                    area[npx][py] = 1;
                    //}
                }
                ++i;
                if (i == width) {
                    i = 0;
                    ++j;
                    if (j == height) {
                        // print(area, totX, totY);
                        // return false; // no collision
                        if (noend1 == false) {
                            return false;
                        }
                        noend = false;
                    }
                }
            }
            if (noend1) {
                if (mdest.mask[(int) (i1 / scalex1)][(int) (j1 / scaley1)]) {
                    npx = px = i1 - cx1;
                    py = j1 - cy1;
                    // if (beta != 0) {
                    npx = (int) Math.round(((double) px * cosbeta - (double) py * sinbeta));
                    py = (int) Math.round(((double) py * cosbeta + (double) px * sinbeta));
                    // }
                    npx += acx1;
                    py += acy1;
                    //if (py < totY && npx < totX) {
                    if (area[npx][py] == 1) {
                        // print(area, totX, totY);
                        return true; // collision!
                    }
                    area[npx][py] = 2;
                    //}
                }
                ++i1;
                if (i1 == width1) {
                    i1 = 0;
                    ++j1;
                    if (j1 == height1) {
                        // print(area, totX, totY);
                        if (noend == false) {
                            return false; // no collision
                        }
                        noend1 = false;
                    }
                }
            }
        }
    }
    /**
     * this method is simply used to debugging the new functions..
     */
    // private static final void print(int a[][], int x, int y) {
    // for (int j = 0; j < y; ++j) {
    // for (int i = 0; i < x; ++i) {
    // if (a[i][j] == 0) {
    // System.out.print(".");
    // } else {
    // System.out.print(a[i][j]);
    // }
    // // System.out.print(" ");
    // }
    // System.out.println();
    // }
    // }
    private static final double rad = Math.PI / 180;

    public static boolean trueHitResized(IPlain2D home, IPlain2D dest,
            Mask mhome, Mask mdest) {
        final double x = home.getX();
        final double y = home.getY();
        final double x1 = dest.getX();
        final double y1 = dest.getY();
        final int xstart = (int) Math.max(x, x1);
        final int ystart = (int) Math.max(y, y1);
        final int xend = (int) Math.min(x + home.getWidth(), x1 + dest.getWidth());
        final int yend = (int) Math.min(y + home.getHeight(), y1 + dest.getHeight());
        final int Toty = Math.abs(yend - ystart);
        final int Totx = Math.abs(xend - xstart);

        final int xstarth = Math.abs(xstart - (int) x);
        final int ystarth = Math.abs(ystart - (int) y);
        final int xstartd = Math.abs(xstart - (int) x1);
        final int ystartd = Math.abs(ystart - (int) y1);

        int X, Y;

        final float fhx = (float) home.getWidth() / (float) mhome.width;
        final float fhy = (float) home.getHeight() / (float) mhome.height;
        final float fdx = (float) dest.getWidth() / (float) mdest.width;
        final float fdy = (float) dest.getHeight() / (float) mdest.height;

        int ny, ny1, nx, nx1;
        if (!(mhome.full || mdest.full)) { // 0.1.9
            for (Y = 0; Y < Toty; Y++) {
                ny = (int) ((ystarth + Y) / fhy);
                ny1 = (int) ((ystartd + Y) / fdy);
                for (X = 0; X < Totx; X++) {
                    nx = (int) ((xstarth + X) / fhx);
                    nx1 = (int) ((xstartd + X) / fdx);
                    if (mhome.mask[nx][ny] && mdest.mask[nx1][ny1]) {
                        return true; // collision!
                    }
                }
            }
        } else { // 0.1.9
            Mask mask = mhome.full ? mdest : mhome;
            final int ys = mhome.full ? ystartd : ystarth;
            final int xs = mhome.full ? xstartd : xstarth;
            final float factorx = mhome.full ? fdx : fhx; // 0.3.4.1
            final float factory = mhome.full ? fdy : fhy;// 0.3.4.1
            for (Y = 0; Y < Toty; Y++) {
                ny = (int) ((ys + Y) / factory); // 0.3.4.1
                for (X = 0; X < Totx; X++) {
                    nx = (int) ((xs + X) / factorx); // 0.3.4.1
                    if (mask.mask[nx][ny]) {
                        return true; // collision!
                    }
                }
            }
        } // 0.1.9
        return false;
    }

    /**
     * check if the ArrayList contain one or more Tiles of type defined.<br>
     * returns ONLY the FIRST TileSprite found.
     *
     * @param list
     *            the ArrayList<ISpriteColl>
     * @param tiles
     *            the types of Tiles to search
     * @return the FIRST TileSprite found
     * @see #searchTile(ArrayList, String[])
     */
    public static TileSprite containTile(ArrayList<ISpriteColl> list,
            String... tiles) {
        // boolean wallFinded = false;
        ISpriteColl spr;
        TileSprite founded = null;
        Iterator<ISpriteColl> iterator = list.iterator();
        while (iterator.hasNext()) {
            spr = iterator.next();
            //if (spr.getType().equals("$_TILESPRITE")) {
            if (spr instanceof TileSprite) {
                for (String s : tiles) {
                    if (((TileSprite) spr).getTileType().equals(s)) {
                        // wallFinded = true;
                        founded = (TileSprite) spr;
                        break;
                    }
                }
            }
        }
        // if (!wallFinded)
        // return;
        return founded;
    }

    @Override
    protected void finalize() throws Throwable {
        queryBuffer.rewind();
        // problem with opengl context?
        // GL15.glDeleteQueries(queryBuffer);
        super.finalize();
    }

    /**
     *
     * @param <T>
     * @param p
     * @param x x coordinate on SCREEN of testing point
     * @param y y coordinate on SCREEN of testing point
     * @return
     */
    public synchronized static boolean testCollision(ISpriteColl p,  int x, int y) {
        Mask mask = (Mask)p.getCollisionData();
        if (mask == null) {
            //return testRectangleCollision((IPlain2D) p);
            if (x >= p.getXOnScreen() && y >= p.getYOnScreen() && x <= (p.getWidth() - 1) + p.getXOnScreen() && y <= (p.getHeight() - 1) + p.getYOnScreen()) {
                return true;
            }return false;
        }
        if (!(x >= p.getXOnScreen() && y >= p.getYOnScreen() && x <= (p.getWidth() - 1) + p.getXOnScreen() && y <= (p.getHeight() - 1) + p.getYOnScreen())) {
            return false;
        }
        if (mask.full) {
            return true;
        }
        int nx = x - (int) p.getXOnScreen();
        int ny = y - (int) p.getYOnScreen();
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
     * check if the ArrayList contain one or more Tiles of type defined.<br>
     * returns ALL the TileSprite found.
     *
     * @param list
     *            the ArrayList<ISpriteColl>
     * @param tiles
     *            the types of Tiles to search
     * @return ALL the TileSprite found
     * @see #containTile(ArrayList, String[])
     */
    public static ArrayList<TileSprite> searchTile(ArrayList<ISpriteColl> list,
            String... tiles) {
        ArrayList<TileSprite> tlist = new ArrayList<TileSprite>(10);
        ISpriteColl spr;
        Iterator<ISpriteColl> iterator = list.iterator();
        while (iterator.hasNext()) {
            spr = iterator.next();
            //if (spr.getType().equals("$_TILESPRITE")) {
            if (spr instanceof TileSprite) {
                for (String s : tiles) {
                    if (((TileSprite) spr).getTileType().equals(s)) {
                        // wallFinded = true;
                        tlist.add((TileSprite) spr);
                    }
                }
            }
        }
        return tlist;
    }
}
