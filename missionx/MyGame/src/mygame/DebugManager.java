/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import mygame.sounds.SoundBox;
import org.easyway.collisions.GroupCollision;
import org.easyway.input.Keyboard;
import org.easyway.interfaces.extended.ILoopable;
import org.easyway.objects.BaseObject;
import org.easyway.objects.extra.FpsDrawer;
import org.easyway.objects.text.Text;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.system.state.GameState;
import static org.easyway.input.Keyboard.*;

/**
 *
 * @author Do$$e
 */
public class DebugManager extends BaseObject implements ILoopable {
    private Text txt;
    private static boolean enabled = false;
    //private FpsDrawer fps;

    public DebugManager() {
        txt = new Text(0, 0, "");
    }

    public static boolean isDebugging() {
        return enabled;
    }

    @Override
    public void loop() {
        if (Keyboard.isKeyPressed(KEY_F12)) {
            /*if (!enabled) {
                fps = new FpsDrawer();
                fps.setX(StaticRef.getCamera().getWidth() - fps.getWidth());
                fps.setY(StaticRef.getCamera().getHeight() - fps.getHeight());
            } else {
                fps.destroy();
                fps = null;
            }*/
            enabled = !enabled;
            CommonVar.useCheats=!CommonVar.useCheats;
        }
        int nsc=0;
        nsc+=GroupCollision.getGroup("playerShots").getDestinationSize()+GroupCollision.getGroup("playerShots").getSourceSize();
        nsc+=GroupCollision.getGroup("ships").getDestinationSize()+GroupCollision.getGroup("ships").getSourceSize();
        nsc+=GroupCollision.getGroup("enemyShots").getDestinationSize()+GroupCollision.getGroup("enemyShots").getSourceSize();
        nsc+=GroupCollision.getGroup("powerUp").getDestinationSize()+GroupCollision.getGroup("powerUp").getSourceSize();


        if (enabled) {
            txt.setText("FPS: " + Core.getInstance().getFpsHP() + " Speed compensation: " + Core.getInstance().getSpeedMultiplier() + "\n" +
                    "Loaded textures: " + StaticRef.textures.size() + "\n"+
                    "Camera: " + StaticRef.getCamera().x + " - "+(StaticRef.getCamera().x+1024)+ "\n" +
                    "LoopableActors: " + GameState.getCurrentGEState().getLoopList().size() +" SpriteColl: "+nsc+ " Animos: " + GameState.getCurrentGEState().getAnimoList().size() + "\n" +
                    "SFX: " + CommonVar.useSFX + " Music: " + CommonVar.music + " SoundBox enabled: " + SoundBox.isEnabled() + "\n" +
                    "CD Mode:" + CommonVar.CDMode + "\n"+
                    "Gamepad: "+CommonVar.useGamePad+ "\n"+
                    "Difficulty:"+CommonVar.difficulty+"\n" +
                    "GFX Quality"+CommonVar.gfxQuality+" Low Res textures: "+CommonVar.lowRes+" Screen resolution: "+CommonVar.xRes+"x"+CommonVar.yRes+" OpenGL version: "+Core.getGLMajorVersion()+"."+Core.getGLMinorVersion()+"\n"+
                    "Used memory: " + ((float) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576) + "MB\nTotal memory: " + ((float) Runtime.getRuntime().totalMemory() / 1048576) + "MB");
            if (Keyboard.isKeyPressed(Keyboard.KEY_NUMPAD2)) {
                System.out.println("executing garbage collector...");
                Runtime.getRuntime().gc();
                System.out.println("garbage collector executed");
            }
        } else {
            txt.setText("");
        }
    }
}
