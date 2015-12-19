/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.easyway.collisions.quad;

import java.io.Serializable;
import java.util.ArrayList;
import org.easyway.collisions.CollisionUtils;
import org.easyway.collisions.IQuadTreeUsable;
import org.easyway.interfaces.sprites.IPlain2D;
import org.easyway.lists.Entry;
import org.easyway.system.Core;
import static org.easyway.collisions.CollisionUtils.rectangleHit;

/**
 *
 * @author Daniele Paggi
 */
public class QuadTree<T extends IQuadTreeUsable> implements Serializable {

    static QuadTree defaultInstance;
    public static boolean USE_QUADTREE = false;

    public static boolean isUsingQuadTree() {
        return USE_QUADTREE;
    }

    public static QuadTree getDefaultInstance() {
        if (!USE_QUADTREE) {
            return null;
        }
        if (defaultInstance == null) {
            return new QuadTree(0, 0,
                    Core.getInstance().getWidth() * 2, Core.getInstance().getHeight() * 2);
        }
        return defaultInstance;
    }

    public static void setDefaultInstance(QuadTree qt) {
        defaultInstance = qt;
    }
    ArrayList<QuadNode> leafsQuads;
    QuadNode firstNode;

    public QuadTree(int x, int y, int w, int h) {
        defaultInstance = this;
        leafsQuads = new ArrayList<QuadNode>(64);
        firstNode = new QuadNode(x, y, w, h);
        // DEBUG:
        /*firstNode.createChildren();
        for (QuadNode n : firstNode.children) {
            n.createChildren();
            for (QuadNode q : n.children) {
                q.createChildren();
            }
        }*/
        firstNode.father = null; // first node!
        //leafsQuads.add(firstNode);
    }

    public void remove(T obj) {
        //System.out.println("Removing: "+obj);
        ArrayList<Entry> entries = obj.getQuadEntries();
        //for (Entry entry : entries) {
        for (int i = entries.size() - 1; i >= 0; --i) {
            entries.get(i).remove();
            /*if (entry.getList().getType().equals("$_QUADLIST")) {
            entry.remove();
            } else {
            throw new RuntimeException("BUG");
            }*/
        }
        ArrayList<QuadNode> nodes = obj.getUsedInQuadNodes();
        nodes.clear();
    }

    private QuadNode getDedicate(T obj, IPlain2D outPlain) {
        QuadNode dedicate = null;
        ArrayList<QuadNode> nodes = obj.getUsedInQuadNodes();
        for (QuadNode node : nodes) {
            if (CollisionUtils.contains(node, outPlain)) {
                dedicate = node;
                break;
            }
        }
        int c = 0;
        if (dedicate == null) {
            if (nodes.size() != 0) {
                dedicate = nodes.get(0);
                do {
                    dedicate = dedicate.getFather();
                    ++c;
                } while (dedicate != null && !CollisionUtils.contains(dedicate, outPlain));
            }
        }
        nodes.clear();
        if (dedicate == null) {
            dedicate = firstNode;
        }
        return dedicate;
    }

    private void addToDedicate(T obj, IPlain2D outPlain, QuadNode dedicate) {
        ArrayList<QuadNode> childs = new ArrayList<QuadNode>(100); // queue list
        childs.add(dedicate);
        QuadNode current;
        for (int i = 0; i < childs.size(); ++i) {

            if (rectangleHit(outPlain, current = childs.get(i))) {
                if (current.isLeaf()) {
                    // ok add it!
                    current.addObject(obj);
                } else {
                    // little speed up
                    childs.add(current.children[0]);
                    childs.add(current.children[1]);
                    childs.add(current.children[2]);
                    childs.add(current.children[3]);
                }
            }

        }

    }

    public void add(T obj) {
        ArrayList<Entry> entries = obj.getQuadEntries();
        for (int i = entries.size() - 1; i >= 0; --i) {
            entries.get(i).remove();
        }

        IPlain2D outPlain = CollisionUtils.getCircleRectangle(obj);
        // DEBUG
        /*Rectangle r = new Rectangle(outPlain.getX(), outPlain.getY(), outPlain.getWidth(), outPlain.getHeight());
        r.fillIt = false;
        r.setColor(1, 0, 0);*/
        QuadNode dedicate = getDedicate(obj, outPlain);
        addToDedicate(obj, outPlain, dedicate);
    }

    public ArrayList<QuadNode> getLeafs() {
        return leafsQuads;
    }
}
