/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example21bis;

import org.easyway.collisions.quad.QuadNode;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.objects.extra.FpsDrawer;
import org.easyway.objects.texture.Texture;
import org.easyway.objects.texture.TextureID;
import org.easyway.system.StaticRef;
import org.easyway.system.state.Game;

/**
 *
 * @author RemalKoil
 */
public class MainKillThemAllBis extends Game {

    public static void main(String[] args) {
        QuadTree.USE_QUADTREE = false;/*
        QuadTree.setDefaultInstance(new QuadTree(0, 0, 5120, 5120));
        QuadNode.minWidth = 100;
        QuadNode.minHeight = 100;
        QuadNode.MAX_SIZE_LIST = 10;
        StaticRef.use_shaders = true;*/
        new MainKillThemAllBis();
    }
    static BackGroundMap backgroundMap;

    @Override
    public void creation() {
        getCamera().move(100, 100);
        Texture texture = Texture.getTexture("examples/example21/images/terrain.jpg");
        backgroundMap = new BackGroundMap(10, 10, 512, 512, "BackGround");
        
        BackgroundTile tile;
        for (int y = 0; y < 10; ++y) {
            for (int x = 0; x < 10; ++x) {
                TextureID cloneImageData = new TextureID(texture.getTextureId());
                Texture cloneImageHandler = new Texture(cloneImageData, "clone_" + x + "_" + y);
                tile = new BackgroundTile(cloneImageHandler);
                backgroundMap.setTile(x, y, tile);
            }
        }

        new Player(10, 10);
        for (int i = 0; i < 1000; ++i) {
            new Enemy();
        }
        new FpsDrawer();

    }

    @Override
    public void loop() {

    }
}
