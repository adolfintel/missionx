package mygame;

import java.io.IOException;
import java.util.ArrayList;
import mygame.sounds.SoundBox;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.input.Mouse;
import org.easyway.interfaces.base.ITexture;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.easyway.sounds.WaveCore;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;

/**
 *
 * @author prog5ia1
 */
class Loader extends Sprite {

    ITexture sfondo = Texture.getTexture("images/menu/" + CommonVar.skin + "/loadBarDesat.png");
    ITexture barretta = Texture.getTexture("images/menu/" + CommonVar.skin + "/loadBar.png");
    private final ArrayList<String> images;
    private final Menu clazz;

    public Loader(ArrayList<String> images, Menu clazz) {
        this.clazz = clazz;
        setIdLayer(15);
        Core.getInstance().setVSync(false);
        this.images = images;
        setImage(barretta);
        setXY(385, 690);
        //for(String i:images) System.out.println(""+i);
        initialWidth = getImage().getWidth();
    }
    int index = 0;
    int initialWidth;
    private static boolean loaded = false;

    @Override
    public void render() {
        if (index < images.size()) {
            if (loaded) {
                index++;
            } else if (images.get(index).contains("backgrounds") && org.easyway.system.Core.getGLIntVersion() < 15) {
                index++;
            } else {
                if (CommonVar.lowRes) {
                    if (images.get(index).contains("backgrounds")) {
                        Texture.getTexture(images.get(index).replace(".png", "_lo.png").replace(".bmp", "_lo.bmp"));
                    } else {
                        Texture.getTexture(images.get(index));
                    }
                }
                index++;
            }
        } else if (index == images.size()) {
            postLoader();
            index++;
        } else {
            // barretta.destroy();
            destroy();
            return;
        }
        StaticRef.getCamera().setCollectingObjectsOnScreen(false);

        //setRGBA(0.3f, 0.3f, 0.3f, 1f);
        setWidth(initialWidth);
        //barretta.setRegion(0, 0, initialWidth, barretta.getHeight());
        setImage(sfondo);
        super.render();

        //        setRGBA(1f, 1f, 1f, 1f);
        //        setScaleX((float) index / (float) (images.size()+1) );
        //        setImage(barretta);
        //        super.render();
        setRGBA(1f, 1f, 1f, 1f);
        float scale = (float) index / (float) (images.size() + 1);
        setWidth((int) (initialWidth * scale));
        barretta.setRegion(0, 0, (int) (initialWidth * scale), barretta.getHeight());
        setImage(barretta);
        super.render();

        StaticRef.getCamera().setCollectingObjectsOnScreen(true);
    }

    private void postLoader() {
        try {
            clazz.loadMap(CommonVar.level);
        } catch (IOException ex) {
            System.out.println("Error while loading map!");
        }
        //StaticRef.getCamera().setBackgroundColor(0, 0, 0);
        SoundBox.setEnabled(true);
        if (CommonVar.useQuadTree) {
            QuadTree.setDefaultInstance(new QuadTree(0, 0, (int) clazz.wi.getLevelSize(), 768));
        }
        CommonVar.GOSprite = new Sprite(0, 1024, Texture.getTexture("images/menu/" + CommonVar.skin + "/" + CommonVar.loc + "/gameOver.png"), 8); //prepara la schermata di game over
        CommonVar.GOSprite.setIdLayer(14);
        Mouse.hide();   //sparisci, cursoraccio inutile!
        clazz.loading.setImage(Texture.getTexture("images/menu/" + CommonVar.skin + "/" + CommonVar.loc + "/loading2.png"));  //immagine clicca x continuare
        loaded = true;
        Core.getInstance().setTimeOut(10000);   //riattiva l'autoterminazione in caso di freeze
        //Core.getInstance().initFpsCounter();
        Core.getInstance().pauseGame();
        Core.getInstance().setVSync(CommonVar.VSynch);
        Core.getInstance().initFpsCounter();
    }
}
