package org.easyway.tiles;

import java.util.ArrayList;

import org.easyway.collisions.CollisionUtils;
import org.easyway.collisions.SingleCollisionList;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.lists.GameList;

public class TiledCollisionList extends SingleCollisionList {

    /**
     * generated serialVersionUID
     */
    private static final long serialVersionUID = 7521714433142102538L;

    public TiledCollisionList(boolean toAdd, ISpriteColl spr) {
        super(toAdd, spr);
    }

    public TiledCollisionList(ISpriteColl spr) {
        super(false, spr);
    }

    @Override
    public void loop() {
        ArrayList<ISpriteColl> collList = sprite.getCollisionList();
        //ICollisionMethod collMethod = sprite.getCollisionMethod();
        if (collList == null) {
            return;
        }
        //for (int i = size() - 1; i >= 0; i--) {
        //    spr = get(i);
        for (ISpriteColl spr : this) {
            /*if (spr == null) {
                continue;
            }*/
            if (CollisionUtils.rectangleHit(spr, sprite)) {
                collList.add(spr);
            }
            /*if (collMethod.isCompatible(spr.getCollisionMethod())) {
                if (collMethod.checkCollision(sprite, spr).isCollided()) {
                    System.out.println("added!");
                    collList.add(spr);
                }
            } else if (spr.getCollisionMethod().isCompatible(collMethod)) {
                if (spr.getCollisionMethod().checkCollision(sprite, spr).isCollided()) {
                    System.out.println("added!");
                    collList.add(spr);
                }
            }*/
        /*
        if (CollisionUtils.rectangleHit(sprite, spr)) {
        if (CollisionUtils.trueHit(sprite, spr)) {
        if (collList != null) {
        collList.add(spr);
        }
        }
        }
         */
        }
    }

    /**
     * sets the list of this object to the object 'list'
     *
     */
    public void setList(GameList<ISpriteColl> list) {
        //super.setList(list);
        this.removeAll();
        for (ISpriteColl spr : list) {
            this.add(spr);
        }

    }
}
