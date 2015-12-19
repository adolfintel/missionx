/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example20;

import org.easyway.input.Mouse;
import org.easyway.interfaces.extended.IDrawing;
import org.easyway.objects.BaseObject;
import org.easyway.objects.Camera;
import org.easyway.objects.sprites2D.SimpleSprite;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;
import org.easyway.objects.texture.TextureFBO;
import org.easyway.system.Core;
import org.easyway.system.state.GameState;

/**
 *
 * @author RemalKoil
 */
public class TexturePainterObject extends BaseObject implements IDrawing {

    TextureFBO texturePaint;
    SimpleSprite backgroundSprite;
    Sprite drawingSprite;

    public TexturePainterObject() {
        Texture textureDest = new Texture(Core.getInstance().getWidth(),
                Core.getInstance().getHeight());
        texturePaint = new TextureFBO(textureDest);
        backgroundSprite = new SimpleSprite(textureDest);
        
        // with the following code the game engine will automatically calls the
        // render method
        GameState.getCurrentGEState().getLayers()[0].add(this);

        // don't autoactivate the sprite!
        // you can do the same removing the comment from the following line...
        // BaseObject.autoAddToLists = false;
        drawingSprite = new Sprite(false, Texture.getTexture("images/apple.png"));
        // BaseObject.autoAddToLists = true;
    }

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void render() {
        texturePaint.startDrawing(false);
        {
            if (Mouse.isLeftPressed()) {
                drawingSprite.setXY(Mouse.getX(), Mouse.getY());
                drawingSprite.render();
            }
            float nx = (float) Math.random() * Camera.getCurrentCamera().getWidth();
            float ny = (float) Math.random() * Camera.getCurrentCamera().getHeight();
            drawingSprite.setXY(nx - 64, ny - 64);
            drawingSprite.render();
        }
        texturePaint.endDrawing();
    }

    @Override
    public void destroy() {
        if (isDestroyed()) {
            return;
        }
        texturePaint.getTexture().destroy();
        texturePaint.destroy();
        backgroundSprite.destroy();
        super.destroy();
    }
}
