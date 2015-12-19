package examples.example07;

import org.easyway.collisions.quad.QuadNode;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.objects.text.FPSWriter;
import org.easyway.objects.texture.Texture;
import org.easyway.system.state.Game;

public class CollisionableSprites extends Game {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        QuadTree.USE_QUADTREE = true;
        QuadNode.MAX_SIZE_LIST = 500;
        QuadNode.minHeight = 128;
        QuadNode.minWidth = 128;
        new CollisionableSprites();
    }

    public CollisionableSprites() {
        super(800, 600, 24, false);
        Texture.autoCreateMask = true;
        if (QuadTree.isUsingQuadTree()) {
            QuadTree.setDefaultInstance(new QuadTree(0, 0, getWidth(), getHeight()));
        }
    }

    @Override
    public void creation() {
        setTimeOut(-1);
        for (int i = 0; i < 50; ++i) {
            new Ball(i);
        }

        new FPSWriter(null, 0, 0);
    }


    @Override
    public void loop() {
    }
}
