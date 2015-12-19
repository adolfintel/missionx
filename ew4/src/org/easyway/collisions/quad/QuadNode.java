/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.collisions.quad;

import java.io.Serializable;
import java.util.ArrayList;
import org.easyway.collisions.CollisionUtils;
import org.easyway.collisions.GroupCollision;
import org.easyway.collisions.IQuadTreeUsable;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.objects.Camera;
import org.easyway.system.StaticRef;

/**
 *
 * @author Daniele Paggi
 */
public class QuadNode<T extends IQuadTreeUsable> implements IPlain2D, Serializable {

    QuadNodeChildList<T> childList;
    ArrayList<GroupCollision> collisionGroups;
    QuadNode children[];
    QuadNode father;
    // QuadTree quadTree;
    float x;
    float y;
    float width;
    float height;
    public static int MAX_SIZE_LIST = 100;
    public static int minWidth = 128;
    public static int minHeight = 128;

    public QuadNode(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        childList = new QuadNodeChildList<T>();
        collisionGroups = new ArrayList<GroupCollision>(8);
        // a new quadNode will be automatically a leaf..
        QuadTree.getDefaultInstance().leafsQuads.add(this);
        // DEBUG:
        //new Rectangle(x, y, (int)width, (int)height).fillIt = false;
    }

    protected void setGroups(ArrayList<GroupCollision> groups) {
        collisionGroups = new ArrayList<GroupCollision>(groups.size());
        GroupCollision ngc;
        for (GroupCollision gc : groups) {
            collisionGroups.add(ngc = new GroupCollision(gc.getType()));
            ngc.setMgcFather(gc.getMgcFather());
        }
    }

    public boolean isLeaf() {
        return children == null;
    }

    public boolean isOnScreen() {
        final float nx = getXOnScreen();
        final float ny = getYOnScreen();
        final Camera camera = StaticRef.getCamera();
        return nx < camera.getWidth() && ny < camera.getHeight() && (nx + width) > 0 && (ny + height) > 0;
    }

    public void addObject(T obj) {
        childList.add(obj);
        if (StaticRef.DEBUG && obj.getUsedInQuadNodes().contains(this)) {
            throw new RuntimeException("Object already contained!");
        }
        obj.getUsedInQuadNodes().add(this);
        if (childList.size() >= MAX_SIZE_LIST && width > minWidth && height > minHeight) {
            createChildren();
        }
    }

    public void createChildren() {
        if (children != null) {
            throw new RuntimeException("children already exists");
        }

        float W = width / 2f;
        float H = height / 2f;
        children = new QuadNode[4];
        children[0] = new QuadNode(x, y, W, H);
        children[1] = new QuadNode(x + W, y, W, H);
        children[2] = new QuadNode(x, y + H, W, H);
        children[3] = new QuadNode(x + W, y + H, W, H);

        children[0].father = this;
        children[1].father = this;
        children[2].father = this;
        children[3].father = this;

        // only 1 quad tree at runtime!
        /*children[0].quadTree = quadTree;
        children[1].quadTree = quadTree;
        children[2].quadTree = quadTree;
        children[3].quadTree = quadTree;*/
        QuadTree.getDefaultInstance().leafsQuads.remove(this);

        children[0].setGroups(collisionGroups);
        children[1].setGroups(collisionGroups);
        children[2].setGroups(collisionGroups);
        children[3].setGroups(collisionGroups);
        IPlain2D outPlain;
        for (T child : childList) {
            child.getUsedInQuadNodes().remove(this);
            outPlain = CollisionUtils.getCircleRectangle(child);
            for (int i = 0; i < 4; ++i) {
                if (CollisionUtils.rectangleHit(outPlain, children[i])) {
                    children[i].addObject(child);
                }
            }
        }
        childList.removeAll();
        childList = null;
        for (GroupCollision gc : collisionGroups) {
            gc.destroy();
        }
        collisionGroups = null;
    }

    public int getWidth() {
        return (int) width;
    }

    public int getHeight() {
        return (int) height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setXY(float x, float y) {
        setX(x);
        setY(y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getXOnScreen() {
        return x - StaticRef.getCamera().getX();
    }

    public float getYOnScreen() {
        return y - StaticRef.getCamera().getY();
    }

    public QuadNode getFather() {
        return father;
    }

    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isDestroyed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getType() {
        return "$_QUADNODE";
    }

    public void addGroupCollision(GroupCollision gc) {
        collisionGroups.add(gc);
    }

    public void removeGroupCollision(GroupCollision gc) {
        collisionGroups.remove(gc);
    }

    public ArrayList<GroupCollision> getGroupCollisions() {
        return collisionGroups;
    }

    public ArrayList getEntries() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList getQuadEntries() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList getUsedInQuadNodes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isQuadTreeUsable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getScaleX() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getScaleY() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
