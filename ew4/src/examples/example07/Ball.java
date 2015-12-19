package examples.example07;

import java.util.ArrayList;
import org.easyway.collisions.GroupCollision;
import org.easyway.collisions.ManagedGroupCollision;
import org.easyway.collisions.methods.RectangleCollisionMethod;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.interfaces.sprites.ISpriteColl;
import org.easyway.objects.sprites2D.SpriteColl;

public class Ball extends SpriteColl implements ILoopable {

    private static final long serialVersionUID = 1L;
    float speedx = (float) Math.random() * 10 - 5, speedy = (float) Math.random() * 10 - 5;
    float oldx, oldy;

    public Ball(int i) {
        // try to change the collision method!
        //setCollisionMethod(CircleCollisionMethod.getDefaultInstance());
        //setCollisionMethod(SoftwareCollisionMethod.getDefaultInstance());
        //setCollisionMethod(RectangleCollisionMethod.getDefaultInstance());
        setImage("images/particella.png");

        if (i % 2 == 0) {
            // set a symbolic name to this instance
            // can be userful in the collision and other things..
            setType("RED");
            setRGBA(1, 0, 0, 1);
            // add this sprite in the source list of the group of collision
            // named "groupA"
            //GroupCollision.getGroup("GroupA").source.add(this);
            if (QuadTree.isUsingQuadTree()) {
                ManagedGroupCollision.get("groupA").addToSource(this);
            } else {
                GroupCollision.getGroup("groupA").addToSource(this);
            }
            setXY((float) Math.random() * (400 - 32), (float) Math.random() * (600 - 32));
        } else {
            setType("GREEN");
            setRGBA(0, 1, 0, 1);
            // add this sprite in the destination list of the group of collision
            // named "groupA"
            //GroupCollision.getGroup("GroupA").destination.add(this);
            if (QuadTree.isUsingQuadTree()) {
                ManagedGroupCollision.get("groupA").addToDestination(this);
            } else {
                GroupCollision.getGroup("groupA").addToDestination(this);
            }
            setXY((float) Math.random() * (400 - 32) + 400, (float) Math.random() * (600 - 32));
        }
        setRotation((float) Math.random() * 360);
    }

    @Override
    public void loop() {
        if (getX() <= 0 || getX() + getWidth() >= 800) {
            speedx = -speedx;
            setX(oldx);
        }
        if (getY() <= 0 || getY() + getHeight() >= 600) {
            speedy = -speedy;
            setY(oldy);
        }

        // save old x,y
        oldx = getX();
        oldy = getY();

        // move speedx, speedy
        move(speedx, speedy);
    }

    @Override
    public void onCollision() {
        // ONLY the RED sprite manage the collision..
        if (this.getType().equals("GREEN")) {
            return;
            // for each SpriteColl contained in the collisionList
            // the collisionList is filled with the sprites that had got a collision
            // with the current sprite

        }
        Ball ball;
        
        for (ISpriteColl spr : getCollisionList()) {
            // we can use a line like the following too..
            if (spr instanceof Ball) {
                ball = (Ball) getCollisionList().get(0);
                // collision between RED and GREEN
                if (ball.getType().equals("GREEN")) {
                    // x = oldx, y = oldy
                    setXY(oldx, oldy);
                    ball.setXY(ball.oldx, ball.oldy);

                    //ball.move(-ball.speedx, -ball.speedy);
                    // switch this.speedx with ball.speedx
                    float temp = speedx;
                    speedx = ball.speedx;
                    ball.speedx = temp;
                    // switch this.speedy with ball.speedy
                    temp = speedy;
                    speedy = ball.speedy;
                    ball.speedy = temp;

                    

                }
            }
        }
        //move(speedx, speedy);
    }

    @Override
    public ArrayList<ISpriteColl> getCollisionList() {
        return super.getCollisionList();
    }
}
