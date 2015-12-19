/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples.example15;

import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.text.Text;
import org.easyway.objects.text.TextAlign;
import org.easyway.objects.texture.Texture;
import org.easyway.shader.Shader;
import org.easyway.shader.WaveShader;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.system.state.Game;
import static org.easyway.input.Keyboard.*;

/**
 *
 * @author Daniele Paggi
 */
public class ShaderExample extends Game {

    Shader shader1;
    Shader shader2;
    Shader shader3;
    WaveShader shader4;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        StaticRef.use_shaders = true;
        new ShaderExample();
    }
    Sprite spr;
    Text info;

    @Override
    public void creation() {
        new Text(0, 0, "Press [space] for enabling\\disabling shading\nPress 1-4 for select the shading effect");
        info = new Text(0,getCamera().getHeight(),"");
        info.setAlignV(TextAlign.BOTTOM);
        Texture img = Texture.getTexture("images/apple.png");
        for (int i = 0; i < 100; ++i) {
            float x = (float) Math.random() * (getWidth() - img.getWidth());
            float y = (float) Math.random() * (getHeight() - img.getHeight());
            new Sprite(x, y, img);
        }
        img = Texture.getTexture("images/Flame.png");
        spr = new Sprite(getWidth() / 2 - img.getWidth(), getHeight() / 2 - img.getHeight(), img);
        spr.setSize(spr.getWidth() * 2, spr.getHeight() * 2);
        spr.setLayer(1);

        shader1 = new Shader(null, "shaders/Bloom.fs");
        shader2 = new Shader(null, "shaders/EmbossFilter.fs");
        shader3 = new Shader("shaders/ShadeColor.vs", "shaders/ShadeColor.fs");
        shader3.setUniform("screenWidth", getWidth());
        shader3.setUniform("startColor", 1, 0, 0); //red
        shader3.setUniform("endColor", 0, 0, 1); //blue

        shader4 = new WaveShader();
        shader4.setAmplitude(2f / getWidth());
        shader4.setPower(2);
        shader4.setLenght(50);
    }

    @Override
    public void loop() {
        if (isKeyPressed(KEY_ESCAPE)) {
            endGame();
        }
        if (isKeyPressed(KEY_SPACE)) {
            Core.getInstance().usePostProcessing(!Core.getInstance().isUsePostProcessing());
        }

        if (isKeyPressed(KEY_1)) {
            Core.getInstance().setShader(shader1);
        } else if (isKeyPressed(KEY_2)) {
            Core.getInstance().setShader(shader2);
        } else if (isKeyPressed(KEY_3)) {
            Core.getInstance().setShader(shader3);
        } else if (isKeyPressed(KEY_4)) {
            Core.getInstance().setShader(shader4);
        }

        float time = Core.getInstance().getTotalElaspedTime() / 500f;
        shader4.setTime(time);
        info.setText("is using shading: "+Core.getInstance().isUsePostProcessing());
    }
}
