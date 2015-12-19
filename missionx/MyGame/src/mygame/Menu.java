/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import org.easyway.interfaces.base.ITexture;
import bsh.EvalError;
import bsh.Interpreter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.logic.CommTrigger;
import mygame.sounds.SoundBox;
import mygame.sounds.MusicLoop;
import mygame.logic.WorldInfo;
import mygame.art.ParallaxStarsMenu;
import mygame.logic.enemy.Enemy3;
import mygame.logic.enemy.Enemy4;
import mygame.logic.enemy.Enemy2;
import mygame.logic.enemy.Enemy7;
import mygame.logic.enemy.Enemy5;
import mygame.logic.enemy.Enemy1;
import mygame.logic.enemy.Enemy6;
import mygame.menu.ClickableSprite;
import mygame.logic.enemy.Boss1;
import mygame.art.BackgroundSprite;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JOptionPane;
import mygame.art.ParallaxSprite;
import mygame.art.SlidingSprite;
import mygame.art.StarEmitter;
import mygame.logic.GamePad;
import mygame.logic.LevelChanger;
import mygame.logic.MusicChanger;
import mygame.logic.ScriptOnScreenEnter;
import mygame.logic.SpeedChanger;
import mygame.logic.enemy.AsteroidField;
import mygame.logic.enemy.Boss2;
import mygame.logic.enemy.Boss3;
import mygame.logic.enemy.Boss4;
import mygame.logic.enemy.Macron;
import org.easyway.collisions.quad.QuadNode;
import org.easyway.collisions.quad.QuadTree;
import org.easyway.input.Keyboard;
import org.easyway.input.Mouse;
import org.easyway.objects.Camera;
import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.ImageData;
import org.easyway.objects.texture.Texture;
import org.easyway.objects.texture.TextureNotFoundException;
import org.easyway.shader.Shader;
import org.easyway.system.Core;
import org.easyway.system.StaticRef;
import org.easyway.system.state.Game;
import org.easyway.utils.Timer.SyncTimer;
import org.easyway.utils.Utility;
import org.lwjgl.opengl.Display;
import static org.easyway.input.Keyboard.*;

/**
 *
 * @author Do$$e
 */
public class Menu extends Game implements Serializable {

    WorldInfo wi;
    Sprite loading, helpSprite, logo, spr, pause, cursor = null, menuBkImg = null, froggyLogo = null;
    private ParallaxStarsMenu menuBk = null;
    private static int phase = 15; //con phase=15 parte dalla froggy
    private ClickableSprite newGame, help, quit, button, credits, easy, normal, hard;
    private boolean canContinue = false, canPlay = false, startGame = true, inMenu = true;
    private HiScoreWriter hs;
    private String loc = CommonVar.loc, skin = CommonVar.skin;
    private StarEmitter stars;
    private float aux = 0;
    private final boolean usePreCache = true, confidential = false;
    private boolean last = false;
    private int speed = 0, progress = 0;
    private Shader shader;
    private Loader loader;
    private ClickableSprite pmButton1, pmButton2;
    private String initLevel = "";
    private boolean hasSF = false, hasLasers = false;
    private int score = 0, health = 99, rocketCount = 20, lives = 3, shotsPerSecond = 7;
    private float oldEasy, oldNorm, oldHard;
    private MusicLoop menuMusic = null;

    public static int getPhase() {
        return phase;
    }

    public static void setPhase(int phase) {
        Menu.phase = phase;
    }
    private boolean phase16_init = false;

    public Menu() {
        super(CommonVar.xRes, CommonVar.yRes, 24, CommonVar.fullscreen);
        if (CommonVar.gfxQuality == -1) {
            CommonVar.gfxQuality = CommonVar.gfx_hi;
        }
        setTitle("Mission X");
        Display.setLocation(0, 0);
        loadHiScore();  //carica il punteggio massimo da file
        Texture.autoCreateMask = true;
        if (CommonVar.useQuadTree) {
            QuadTree.USE_QUADTREE = true;
            QuadNode.MAX_SIZE_LIST = 500;
            QuadNode.minHeight = 128;
            QuadNode.minWidth = 128;
        } else {
            QuadTree.USE_QUADTREE = false;
        }
        initLevel = CommonVar.level;
        if (CommonVar.useGamePad) {
            GamePad.initialise();
        }
        oldEasy = CommonVar.DIFF_EASY;
        oldNorm = CommonVar.DIFF_NORMAL;
        oldHard = CommonVar.DIFF_HARD;
    }

    private void loadHiScore() {
        try {
            String hsPath = CommonVar.level;
            hsPath.replace("\\", "/");
            StringTokenizer st = new StringTokenizer(hsPath, "/");
            while (st.hasMoreTokens()) {
                hsPath = st.nextToken();
            }
            hsPath = hsPath.replace(".txt", "");
            hsPath += ".hs";
            File f = new File(hsPath);
            FileInputStream fis = new FileInputStream(f);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            CommonVar.hiScore1 = Integer.parseInt(br.readLine());
            CommonVar.hiScore2 = Integer.parseInt(br.readLine());
            CommonVar.hiScore3 = Integer.parseInt(br.readLine());
        } catch (Exception e) {
            System.out.println("Can't load hi score: " + e.toString());
        }
        if (CommonVar.hiScore1 < 0 || CommonVar.hiScore2 < 0 || CommonVar.hiScore3 < 0) {
            System.out.println("Warning: negative hi score. resetting...");
            CommonVar.hiScore1 = 0;
            CommonVar.hiScore2 = 0;
            CommonVar.hiScore3 = 0;
        }
    }

    private void saveHiScore() {
        try {
            String hsPath = CommonVar.level;
            hsPath.replace("\\", "/");
            StringTokenizer st = new StringTokenizer(hsPath, "/");
            while (st.hasMoreTokens()) {
                hsPath = st.nextToken();
            }
            hsPath = hsPath.replace(".txt", "");
            hsPath += ".hs";
            File f = new File(hsPath);
            FileOutputStream fos = new FileOutputStream(f);
            PrintStream ps = new PrintStream(fos);
            ps.println("" + CommonVar.hiScore1);
            ps.println("" + CommonVar.hiScore2);
            ps.println("" + CommonVar.hiScore3);
            ps.close();
            fos.close();
        } catch (Exception e) {
            System.out.println("Can't save hi score: " + e.toString());
        }
    }

    @Override
    public void creation() {
        setTimeOut(0);
        new DebugManager();
        if (CommonVar.gfxQuality == 0) { //auto graphics quality
            System.out.println("Auto setting gfx quality for opengl "+Core.getGLIntVersion());
            if (Core.getGLIntVersion() < 20) {
                System.out.println("Mid GFX Quality selected");
                CommonVar.gfxQuality = CommonVar.gfx_mid;
                CommonVar.lowRes = true;
            } else {
                System.out.println("Hi GFX Quality selected");
                CommonVar.gfxQuality = CommonVar.gfx_hi;
                CommonVar.lowRes = false;
            }
        }
        if (CommonVar.usePProc) {
            usePostProcessing(true);
            shader = new Shader(null, "shaders/bloom.fs");
            setShader(shader);
        }
        StaticRef.getCamera().setZoom2D((float) (768f / (double) CommonVar.yRes));
        setTimeOut(10000);
    }
    private ArrayList<String> images = new ArrayList<String>();

    public void preCache() {        //questo apparentemente inutile metodo pre carica in memoria le immy + grandi prima di iniziare a giocare, così da evitare stupidi caricamentini in-game
        try {
            CSVToolkit pc = new CSVToolkit(CommonVar.level.replace(".txt", ".precache"));
            Vector<Vector<String>> v = pc.getOutput();
            for (Vector<String> s : v) {
                String i = s.get(0);
                i = i.replaceAll("_LOC_", CommonVar.loc);
                i = i.replaceAll("_SKIN_", skin);
                images.add(i);
            }
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        SoundBox.preCache();
    }

    private void preCacheMenu() {    //questo metodo pre carica in memoria le immy + grandi del menu in modo da permettere uno scorrimento più rapido delle pagine
        //Texture.getTexture("images/menu/menuBk.png");
        Texture.getTexture("images/menu/" + skin + "/" + loc + "/helpText.png");
        Texture.getTexture("images/menu/" + skin + "/" + loc + "/creditsText.png");
    }

    private void cleanMemory() { //pulisce la memoria
        //if (!CommonVar.CDMode) {  //questo deve essere eseguito solo se non in modalità cd
           /* for (int i = 0; i < StaticRef.textures.size(); i++)
        StaticRef.textures.get(i).destroy();*/
        Texture.clearTextures();
        Runtime.getRuntime().gc();   //esegue il garbage collector
        //}
    }

    private class GameOverChronometer extends SyncTimer {

        public GameOverChronometer(int delay) {
            super(delay);
        }

        @Override
        public void onTick() {
            canContinue = true;
            stop();
        }
    }

    private class FroggyLogoChronometer extends SyncTimer {

        public FroggyLogoChronometer() {
            super(2000);
        }

        public void onTick() {
            phase = 0;
            stop();
        }
    }

    private boolean execute(Vector<String> v) {
        // <editor-fold defaultstate="collapsed" desc="Map Interpreter">
        System.out.print("Checking & Executing instruction: ");
        for (int j = 0; j < v.size(); j++) {
            System.out.print(v.get(j) + " ");
        }
        System.out.print("\n");
        if (v.get(0).equals(";")) {
            return true;
        }
        if (v.size() == 3 && v.get(0).equals("WorldInfo")) {
            wi = new WorldInfo();
            wi.setLevelSize(Integer.parseInt(v.get(1)));
            wi.setSpeedX(Float.parseFloat(v.get(2)));
            wi.setHealth(health);
            wi.setLives(lives);
            wi.setScore(score);
            wi.setSuperFire(hasSF);
            wi.setRocketCount(rocketCount);
            wi.setLasers(hasLasers);
            wi.setShotsPerSecond(shotsPerSecond);
            return true;
        }
        if (v.size() == 4 && v.get(0).equals("Enemy1")) {
            new Enemy1(Integer.parseInt(v.get(1)), Integer.parseInt(v.get(2)), Integer.parseInt(v.get(3)));
            return true;
        }
        if (v.size() == 4 && v.get(0).equals("Enemy2")) {
            new Enemy2(Integer.parseInt(v.get(1)), Integer.parseInt(v.get(2)), Integer.parseInt(v.get(3)));
            return true;
        }
        if (v.size() == 4 && v.get(0).equals("Enemy3")) {
            new Enemy3(Integer.parseInt(v.get(1)), Integer.parseInt(v.get(2)), Integer.parseInt(v.get(3)));
            return true;
        }
        if (v.size() == 4 && v.get(0).equals("Enemy4")) {
            new Enemy4(Integer.parseInt(v.get(1)), Integer.parseInt(v.get(2)), Integer.parseInt(v.get(3)));
            return true;
        }
        if (v.size() == 4 && v.get(0).equals("Enemy5")) {
            new Enemy5(Integer.parseInt(v.get(1)), Integer.parseInt(v.get(2)), Integer.parseInt(v.get(3)));
            return true;
        }
        if (v.size() == 4 && v.get(0).equals("Enemy6")) {
            new Enemy6(Integer.parseInt(v.get(1)), Integer.parseInt(v.get(2)), Integer.parseInt(v.get(3)));
            return true;
        }
        if (v.size() == 4 && v.get(0).equals("Enemy7")) {
            new Enemy7(Integer.parseInt(v.get(1)), Integer.parseInt(v.get(2)), Integer.parseInt(v.get(3)));
            return true;
        }
        if (v.size() == 4 && v.get(0).equals("Boss1")) {
            new Boss1(Integer.parseInt(v.get(1)), Integer.parseInt(v.get(2)), Integer.parseInt(v.get(3)));
            return true;
        }
        if (v.size() == 4 && v.get(0).equals("Boss2")) {
            new Boss2(Integer.parseInt(v.get(1)), Integer.parseInt(v.get(2)), Integer.parseInt(v.get(3)));
            return true;
        }
        if (v.size() == 4 && v.get(0).equals("Boss3")) {
            new Boss3(Integer.parseInt(v.get(1)), Integer.parseInt(v.get(2)), Integer.parseInt(v.get(3)));
            return true;
        }
        if (v.size() == 4 && v.get(0).equals("Boss4")) {
            new Boss4(Integer.parseInt(v.get(1)), Integer.parseInt(v.get(2)), Integer.parseInt(v.get(3)));
            return true;
        }
        if (v.size() == 4 && v.get(0).equals("Macron")) {
            new Macron(Integer.parseInt(v.get(1)), Integer.parseInt(v.get(2)), Integer.parseInt(v.get(3)));
            return true;
        }
        if (v.size() == 5 && v.get(0).equals("CommTrigger")) {
            new CommTrigger(Integer.parseInt(v.get(1)), v.get(2), v.get(3), Integer.parseInt(v.get(4)));
            return true;
        }
        if (v.size() == 3 && v.get(0).equals("SpeedChanger")) {
            new SpeedChanger(Integer.parseInt(v.get(1)), Float.parseFloat(v.get(2)));
            return true;
        }
        if (v.size() == 6 && v.get(0).equals("BackgroundSprite")) {
            new BackgroundSprite(Float.parseFloat(v.get(1)), Float.parseFloat(v.get(2)), v.get(3), Boolean.parseBoolean(v.get(4)), Boolean.parseBoolean(v.get(5)));
            return true;
        }
        if (v.size() == 4 && v.get(0).equals("SlidingSprite")) {
            new SlidingSprite(Integer.parseInt(v.get(1)), v.get(2), Float.parseFloat(v.get(3)));
            return true;
        }
        if (v.size() == 5 && v.get(0).equals("ParallaxStars")) {
            if (v.get(1).equals("1")) {
                stars = new StarEmitter(Integer.parseInt(v.get(2)), Integer.parseInt(v.get(3)), Float.parseFloat(v.get(4)));
            } else {
                stars = null;
            }
            return true;
        }
        if (v.size() == 4 && v.get(0).equals("MusicChanger")) {
            new MusicChanger(Integer.parseInt(v.get(1)), v.get(2), Integer.parseInt(v.get(3)));
            return true;
        }
        if (v.size() == 6 && v.get(0).equals("AsteroidField")) {
            new AsteroidField(Integer.parseInt(v.get(1)), Integer.parseInt(v.get(2)), Integer.parseInt(v.get(3)), Integer.parseInt(v.get(4)), Integer.parseInt(v.get(5)));
            return true;
        }
        if (v.size() == 9 && v.get(0).equals("ParallaxSprite")) {
            new ParallaxSprite(Integer.parseInt(v.get(1)), Integer.parseInt(v.get(2)), v.get(3), Integer.parseInt(v.get(4)), Float.parseFloat(v.get(5)), Boolean.parseBoolean(v.get(6)), Integer.parseInt(v.get(7)), Integer.parseInt(v.get(8)));
            return true;
        }
        if (v.size() == 4 && v.get(0).equals("BkgColor")) {
            StaticRef.getCamera().setBackgroundColor(Integer.parseInt(v.get(1)), Integer.parseInt(v.get(2)), Integer.parseInt(v.get(3)));
            return true;
        }
        if (v.size() >= 2 && v.get(0).equals("Script")) {
            String instruction = "";
            try {
                for (int i = 1; i < v.size(); i++) {
                    instruction += v.get(i) + ",";
                }
                instruction = instruction.substring(0, instruction.length() - 2);
                Interpreter i = new Interpreter();
                i.eval(instruction);
            } catch (Exception e) {
                System.out.println("Invalid instruction in script: " + instruction);
            }
            return true;
        }
        if (v.size() == 2 && v.get(0).equals("ScriptFromFile")) {
            Interpreter i = new Interpreter();
            InputStreamReader reader;
            InputStream is = null;
            String code = v.get(1);
            if (code.startsWith("/")) {
                code = code.substring(1);
            }
            int index;
            while ((index = code.indexOf("\\")) != -1) {
                code = code.substring(0, index) + '/' + code.substring(index + 1);
            }
            URL url = Thread.currentThread().getContextClassLoader().getResource(code);
            if (url != null) {
                try {
                    is = url.openStream();
                } catch (IOException ex) {
                    Logger.getLogger(ImageData.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Utility.error("ScriptFromFile", "Scriptfile " + code + " not found!");
                throw new TextureNotFoundException(code);
            }
            reader = new InputStreamReader(is);
            try {
                i.eval(reader);
            } catch (EvalError ex) {
                System.out.println("Invalid instruction in script file: " + v.get(1));
            }
            return true;
        }
        if (v.size() == 3 && v.get(0).equals("ScriptOnScreenEnter")) {
            new ScriptOnScreenEnter(Integer.parseInt(v.get(1)), v.get(2));
            return true;
        }
        if (v.size() == 2 && v.get(0).equals("LevelAt")) {
            CommonVar.addLevel(v.get(1));
            return true;
        }
        if (v.size() == 3 && v.get(0).equals("LoadLevel")) {
            new LevelChanger(Integer.parseInt(v.get(1)), v.get(2));
            return true;
        }
        if (v.size() == 3 && v.get(0).equals("MOV")) {
            if (v.get(1).equals("fa")) {
                AuxVars.fa = Float.parseFloat(v.get(2));
                return true;
            }
            if (v.get(1).equals("fb")) {
                AuxVars.fb = Float.parseFloat(v.get(2));
                return true;
            }
            if (v.get(1).equals("fc")) {
                AuxVars.fc = Float.parseFloat(v.get(2));
                return true;
            }
            if (v.get(1).equals("fd")) {
                AuxVars.fd = Float.parseFloat(v.get(2));
                return true;
            }
            if (v.get(1).equals("fe")) {
                AuxVars.fe = Float.parseFloat(v.get(2));
                return true;
            }
            if (v.get(1).equals("da")) {
                AuxVars.da = Double.parseDouble(v.get(2));
                return true;
            }
            if (v.get(1).equals("db")) {
                AuxVars.db = Double.parseDouble(v.get(2));
                return true;
            }
            if (v.get(1).equals("dc")) {
                AuxVars.dc = Double.parseDouble(v.get(2));
                return true;
            }
            if (v.get(1).equals("dd")) {
                AuxVars.dd = Double.parseDouble(v.get(2));
                return true;
            }
            if (v.get(1).equals("de")) {
                AuxVars.de = Double.parseDouble(v.get(2));
                return true;
            }
            if (v.get(1).equals("ia")) {
                AuxVars.ia = Integer.parseInt(v.get(2));
                return true;
            }
            if (v.get(1).equals("ib")) {
                AuxVars.ib = Integer.parseInt(v.get(2));
                return true;
            }
            if (v.get(1).equals("ic")) {
                AuxVars.ic = Integer.parseInt(v.get(2));
                return true;
            }
            if (v.get(1).equals("id")) {
                AuxVars.id = Integer.parseInt(v.get(2));
                return true;
            }
            if (v.get(1).equals("ie")) {
                AuxVars.ie = Integer.parseInt(v.get(2));
                return true;
            }
            if (v.get(1).equals("ba")) {
                AuxVars.ba = Boolean.parseBoolean(v.get(2));
                return true;
            }
            if (v.get(1).equals("bb")) {
                AuxVars.bb = Boolean.parseBoolean(v.get(2));
                return true;
            }
            if (v.get(1).equals("bc")) {
                AuxVars.bc = Boolean.parseBoolean(v.get(2));
                return true;
            }
            if (v.get(1).equals("bd")) {
                AuxVars.bd = Boolean.parseBoolean(v.get(2));
                return true;
            }
            if (v.get(1).equals("be")) {
                AuxVars.be = Boolean.parseBoolean(v.get(2));
                return true;
            }
            System.out.println(v.get(1) + " doesn't exist!");
        }

        /*if(v.size()==11&&v.get(0).equals("ParallaxNebula")){
        for(int i=0;i<Integer.parseInt(v.get(5));i++) new ParallaxSprite((int)(Math.random()*(Integer.parseInt(v.get(3))-Integer.parseInt(v.get(1)))+Integer.parseInt(v.get(1))),(int)(Math.random()*(Integer.parseInt(v.get(4))-Integer.parseInt(v.get(2)))+Integer.parseInt(v.get(2))),v.get(6),Integer.parseInt(v.get(7)),(float)(Math.random()*(Float.parseFloat(v.get(9))-Float.parseFloat(v.get(8)))+Float.parseFloat(v.get(8))),Boolean.parseBoolean(v.get(10)));
        return true;
        }*/
        return false;
        //</editor-fold>
    }

    public void loadMap(String filename) throws IOException {
        CSVToolkit conv = new CSVToolkit(filename);
        Vector<Vector<String>> csv = conv.getOutput();
        for (int i = 0; i < csv.size(); i++) {
            Vector<String> v = csv.get(i);
            if (!execute(v)) {
                System.out.print("Invalid instruction: ");
                for (int j = 0; j < v.size(); j++) {
                    System.out.print(v.get(j) + " ");
                }
                System.out.print("\n");
            } else {
                System.out.println("done!");
            }
        }
    }

    @Override
    @SuppressWarnings("empty-statement")
    public void loop() {


        if (startGame) {
            if (Keyboard.isKeyPressed(Keyboard.KEY_RETURN)) {   //switch between gamepad and mouse
                if (CommonVar.useGamePad) {
                    CommonVar.useGamePad = false;
                } else {
                    if (GamePad.isGamePadPlugged()) {
                        CommonVar.useGamePad = true;
                        if (!GamePad.isInitialised()) {
                            GamePad.initialise();
                        }
                    }
                }
            }
            if (phase == 0) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 0: Menu generation">
                //creazione menu e inizializzazione gioco
                setTimeOut(0);   //disabilita l'autoterminazione in caso di ritardo prolungato
                //TextureID.USE_MIPMAP = true;
                CommonVar.DIFF_EASY = oldEasy;
                CommonVar.DIFF_NORMAL = oldNorm;
                CommonVar.DIFF_HARD = oldHard;
                preCacheMenu();
                CommonVar.level = initLevel;
                getCamera().setBackgroundColor(0, 0, 0);
                hasSF = false;
                hasLasers = false;
                score = 0;
                health = 99;
                rocketCount = 20;
                lives = 3;
                shotsPerSecond = 7;
                setVSync(CommonVar.VSynch);
                SoundBox.setEnabled(false);
                CommonVar.destroyAll = false;
                StaticRef.getCamera().setx(0);
                StaticRef.getCamera().sety(2048);
                aux = 0;
                //if(menuBk==null) menuBk=new ChainedMenuBackgrounds(7,2048,"images/backgrounds/");//=new Sprite(0,2048,Texture.getTexture("images/menu/menuBk.png"),5);
                if (menuBk == null) {
                    menuBk = new ParallaxStarsMenu(2048);
                }
                if (menuBkImg == null) {
                    menuBkImg = new Sprite(0, 2048, Texture.getTexture("images/menu/" + skin + "/menuBk.png"), 1);
                }
                if (confidential) {
                    spr = new Sprite(0, 2784, Texture.getTexture("images/menu/" + skin + "/" + loc + "/confidential.png"), 1);
                }
                if (froggyLogo != null) {
                    froggyLogo.destroy();
                    froggyLogo = null;
                }
                if (logo == null) {
                    logo = new Sprite(0, 2048, Texture.getTexture("images/menu/" + skin + "/logo.png"), 6);
                }
                newGame = new ClickableSprite(0, 2496, Texture.getTexture("images/menu/" + skin + "/" + loc + "/newGame.png"), Texture.getTexture("images/menu/" + skin + "/" + loc + "/newGameSelected.png"));
                help = new ClickableSprite(0, 2560, Texture.getTexture("images/menu/" + skin + "/" + loc + "/help.png"), Texture.getTexture("images/menu/" + skin + "/" + loc + "/helpSelected.png"));
                credits = new ClickableSprite(0, 2624, Texture.getTexture("images/menu/" + skin + "/" + loc + "/credits.png"), Texture.getTexture("images/menu/" + skin + "/" + loc + "/creditsSelected.png"));
                quit = new ClickableSprite(0, 2688, Texture.getTexture("images/menu/" + skin + "/" + loc + "/quit.png"), Texture.getTexture("images/menu/" + skin + "/" + loc + "/quitSelected.png"));
                hs = new HiScoreWriter(512, 2496, skin);
                Mouse.setCursor(Texture.getTexture("images/nullCursor.png"));
                if (cursor == null) {
                    cursor = new Sprite(0, 0, Texture.getTexture("images/menu/" + skin + "/Cursor.png"), 7);
                    cursor.setFixedOnScreen(true);
                    cursor.setWidth((int) (36 / StaticRef.getCamera().getZoom2D()));
                    cursor.setHeight((int) (36 / StaticRef.getCamera().getZoom2D()));
                }
                inMenu = true;
                if (!CommonVar.CDMode) {
                    Runtime.getRuntime().gc();   //esegue il garbage collector
                }
                setTimeOut(10000);   //riabilita l'autoterminazione in caso di ritardo prolungato
                CommonVar.gameStarted = true; //avverte il configuratore che il gioco è caricato e può sparire
            /*Thread t=new BkgCacher();
                t.start();*/
                CommonVar.resetLevels();
                Core.getInstance().initFpsCounter();
                phase++;
                //</editor-fold>
            } else if (phase == 1) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 1: Main menu, Waiting for user input">
                //in attesa della selezione dell'utente
                CommonVar.gameOver = false;   //previene falsi game over...
                if (newGame.hasBeenClicked()) {
                    phase = 13;   //distrugge menu e va a selezione difficoltà
                }
                if (help.hasBeenClicked()) {
                    phase = 2;  //distruzione menu e mostra guida
                }
                if (credits.hasBeenClicked()) {
                    phase = 9;   //distruzione menu e mostra crediti
                }
                if (quit.hasBeenClicked()) {
                    saveHiScore();

                    endGame();
                }    //esce del gioco salvando il punteggio massimo
                //</editor-fold>
            } else if (phase == 2) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 2: Help screen generation">
                //distrugge menu e mostra guida
                newGame.destroy();
                help.destroy();
                quit.destroy();
                //logo.destroy();
                credits.destroy();
                hs.destroy();
                helpSprite = new Sprite(0, 2048, Texture.getTexture("images/menu/" + skin + "/" + loc + "/helpText.png"), 6);
                button = new ClickableSprite(384, 2752, Texture.getTexture("images/menu/" + skin + "/" + loc + "/back.png"), Texture.getTexture("images/menu/" + skin + "/" + loc + "/backSelected.png"));
                phase++;
                //</editor-fold>
            } else if (phase == 3) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 3: Help screen, Waiting for user input">
                //in attesa della selezione dell'utente
                if (button.hasBeenClicked()) {
                    phase = 4;    //distrugge la guida e rigenera il menu
                }
                //</editor-fold>
            } else if (phase == 4) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 4: Return to menu">
                button.destroy();
                helpSprite.destroy();
                if (confidential) {
                    spr.destroy();
                }
                phase = 0;    //rigenera il menu
                //</editor-fold>
            } else if (phase == 5) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 5: Menu Destruction and load screen generation">
                //distrugge il menu e mostra la scritta caricamento
                //distrugge gli sfondi e setta a null per poterli ricreare dopo
                menuBk.destroy();
                menuBk = null;
                menuBkImg.destroy();
                menuBkImg = null;
                //distrugge il puntatore e setta a null per poterlo ricreare dopo
                inMenu = false;
                cursor.kill();
                cursor = null;
                //logo.destroy();
                easy.destroy();
                normal.destroy();
                hard.destroy();
                button.destroy();
                if (confidential) {
                    spr.destroy();
                }
                cleanMemory(); //libera memoria
                loading = new Sprite(0, 0, Texture.getTexture("images/menu/" + skin + "/" + loc + "/loading1.png"), 14);
                loading.setIdLayer(14);
                Camera.getCurrentCamera().setx(0);
                Camera.getCurrentCamera().sety(0);
                phase++;
                //</editor-fold>
            } else if (phase == 6) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 6: Load screen">
                //carica il gioco
                System.out.println("loading started");
                Core.getInstance().setTimeOut(0);   //disabilita l'autoterminazione in caso di ritardo prolungato
                CommonVar.destroyAll = false;
                cleanMemory(); //libera memoria
                //TextureID.USE_MIPMAP = false;

                if (usePreCache) {
                    preCache();
                } else {
                    images = new ArrayList<String>();
                }
                phase = 19;
                //</editor-fold>
            } else if (phase == 19) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 19: see Loader class">
                if (loader == null) {
                    loader = new Loader(images, this);
                } else {
                    if (loader.isDestroyed()) {
                        loader = null;
                        phase = 17;
                    }
                }
            } //</editor-fold>
            else if (phase == 7) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 7: Playing...">
                /*if (Keyboard.isKeyPressed(KEY_ESCAPE)) {
                phase = 8;  //distrugge il gioco e torna al menu
                }*/
                if (pmButton1 != null) {
                    pmButton1.destroy();
                    pmButton1 = null;
                }
                if (pmButton2 != null) {
                    pmButton2.destroy();
                    pmButton2 = null;
                }
                if (cursor != null) {
                    cursor.destroy();
                    cursor = null;
                }
                if (menuMusic != null) {
                    menuMusic.stop();
                    menuMusic = null;
                }

                if (Keyboard.isKeyPressed(KEY_ESCAPE) || GamePad.isButtonPressed(CommonVar.GPPause)) {
                    phase = 11;   //va in pausa
                }
                if (CommonVar.gameOver) {
                    CommonVar.gameOver = false;
                    SoundBox.setEnabled(false); //stoppiamo i suoni che non servono più
                    new GameOverChronometer(3000).start();  //per quanto tempo lasciamo la schermata di gameOver?
                    phase = 10;   //va alla schermata di gameOver
                }
                //</editor-fold>
            } else if (phase == 8) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 8: Return to menu (from game)">
                //ritorno al menu
                if (pmButton1 != null) {
                    pmButton1.destroy();
                    pmButton1 = null;
                }
                if (pmButton2 != null) {
                    pmButton2.destroy();
                    pmButton2 = null;
                }
                Mouse.show();   //ripristina il cursore
                if (CommonVar.musicL != null) {
                    CommonVar.musicL.stop();
                }
                CommonVar.musicL = null;
                CommonVar.destroyAll = true;
                StaticRef.getCamera().attrack = false;
                cleanMemory(); //libera la memoria
                menuMusic = new MusicLoop("music/menu.mp3", 83059);
                phase = 0;
                //</editor-fold>
            } else if (phase == 9) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 9: Credits screen generation">
                //distrugge menu e mostra crediti
                newGame.destroy();
                help.destroy();
                quit.destroy();
                //logo.destroy();
                credits.destroy();
                hs.destroy();
                helpSprite = new Sprite(0, 2048, Texture.getTexture("images/menu/" + skin + "/" + loc + "/creditsText.png"), 6);
                button = new ClickableSprite(384, 2752, Texture.getTexture("images/menu/" + skin + "/" + loc + "/back.png"), Texture.getTexture("images/menu/" + skin + "/" + loc + "/backSelected.png"));
                phase = 3;    //vai in attesa della pressione di button
                //</editor-fold>
            } else if (phase == 10) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 10: Game over screen">
                CommonVar.GOSprite.setXY(0, 0);
                CommonVar.GOSprite.fixedOnScreen = true;
                CommonVar.GOSprite.setWidth(CommonVar.xRes);
                CommonVar.GOSprite.setHeight(CommonVar.yRes);
                CommonVar.GOSprite.setRGBA(1, 1, 1, aux); //aux è la transizione sulla schermata di game over
                if (canContinue) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    CommonVar.GOSprite.destroy();  //distrugge la schermata di game over
                    canContinue = false;  //resetta schermata di gameOver per il prossimo utilizzo
                    aux = 0;
                    phase = 8; //ritorna al menu
                } else {
                    aux += 0.015f * Core.getInstance().getSpeedMultiplier();
                }
                //</editor-fold>
            } else if (phase == 11) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 11: Pause menu generation">
                pause = new Sprite(0, 0, Texture.getTexture("images/menu/" + skin + "/" + loc + "/paused.png"), 8);
                pause.setIdLayer(14);
                pause.setLayer(65536);
                pause.setFixedOnScreen(true);
                pause.setWidth(CommonVar.xRes);
                pause.setHeight(CommonVar.yRes);
                pauseGame();
                SoundBox.play(17);
                SoundBox.setEnabled(false);
                pmButton1 = new ClickableSprite(StaticRef.getCamera().x, 512, Texture.getTexture("images/menu/" + skin + "/" + loc + "/continue.png"), Texture.getTexture("images/menu/" + skin + "/" + loc + "/continueSelected.png"));
                pmButton2 = new ClickableSprite(StaticRef.getCamera().x, 576, Texture.getTexture("images/menu/" + skin + "/" + loc + "/backToMM.png"), Texture.getTexture("images/menu/" + skin + "/" + loc + "/backToMMSelected.png"));
                pmButton1.setIdLayer(15);
                pmButton2.setIdLayer(15);
                Mouse.show();   //riappare il cursore
                Mouse.setCursor(Texture.getTexture("images/nullCursor.png"));
                if (cursor == null) {
                    cursor = new Sprite(0, 0, Texture.getTexture("images/menu/" + skin + "/Cursor.png"), 7);
                    cursor.setFixedOnScreen(true);
                    cursor.setWidth((int) (36 / StaticRef.getCamera().getZoom2D()));
                    cursor.setHeight((int) (36 / StaticRef.getCamera().getZoom2D()));
                    cursor.setIdLayer(15);
                }
                inMenu = true;
                phase++;
                //</editor-fold>
            } else if (phase == 12) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 12: Pause menu, Waiting for user input">
                if (Keyboard.isKeyPressed(KEY_ESCAPE) || pmButton1.hasBeenClicked()) {
                    pause.destroy();
                    resumeGame();
                    SoundBox.setEnabled(true);
                    SoundBox.play(-1);
                    Mouse.hide();   //sparisci, cursoraccio inutile!
                    phase = 7;
                    inMenu = false;
                }
                if (Keyboard.isKeyPressed(KEY_Q) || pmButton2.hasBeenClicked()) {
                    pause.destroy();
                    resumeGame();
                    SoundBox.setEnabled(true);
                    SoundBox.play(17);
                    phase = 8;
                }
                //</editor-fold>
            } else if (phase == 13) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 13: Skill selection screen generation">
                cleanMemory(); //libera memoria
                newGame.destroy();
                help.destroy();
                quit.destroy();
                credits.destroy();
                hs.destroy();
                easy = new ClickableSprite(0, 2496, Texture.getTexture("images/menu/" + skin + "/" + loc + "/easy.png"), Texture.getTexture("images/menu/" + skin + "/" + loc + "/easySelected.png"));
                normal = new ClickableSprite(0, 2560, Texture.getTexture("images/menu/" + skin + "/" + loc + "/normal.png"), Texture.getTexture("images/menu/" + skin + "/" + loc + "/normalSelected.png"));
                hard = new ClickableSprite(0, 2624, Texture.getTexture("images/menu/" + skin + "/" + loc + "/hard.png"), Texture.getTexture("images/menu/" + skin + "/" + loc + "/hardSelected.png"));
                button = new ClickableSprite(384, 2752, Texture.getTexture("images/menu/" + skin + "/" + loc + "/back.png"), Texture.getTexture("images/menu/" + skin + "/" + loc + "/backSelected.png"));
                phase++;    //va ad attendere la pressione di un pulsante
                //</editor-fold>
            } else if (phase == 14) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 14: Skill selection screen, Waiting for user input">
                if (easy.hasBeenClicked()) {
                    logo.destroy();
                    logo = null;
                    CommonVar.difficulty = CommonVar.DIFF_EASY;
                    phase = 5;
                }
                if (normal.hasBeenClicked()) {
                    logo.destroy();
                    logo = null;
                    CommonVar.difficulty = CommonVar.DIFF_NORMAL;
                    phase = 5;
                }
                if (hard.hasBeenClicked()) {
                    logo.destroy();
                    logo = null;
                    CommonVar.difficulty = CommonVar.DIFF_HARD;
                    phase = 5;
                }
                if (button.hasBeenClicked()) {
                    button.destroy();
                    easy.destroy();
                    normal.destroy();
                    hard.destroy();
                    if (confidential) {
                        spr.destroy();
                    }
                    phase = 0;    //ritorna al menu
                }
                //</editor-fold>
            } else if (phase == 15) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 15: First screen (froggysoft logo) generation">
                setTimeOut(0);   //disabilita l'autoterminazione in caso di ritardo prolungato
                inMenu = false;
                StaticRef.getCamera().setx(0);
                StaticRef.getCamera().sety(2048);
                if (Core.getGLIntVersion() < 15) {
                    froggyLogo = new Sprite(0, 2048, Texture.getTexture("images/menu/froggysoft_lo.png"), 7);
                } else {
                    froggyLogo = new Sprite(0, 2048, Texture.getTexture("images/menu/froggysoft.png"), 7);
                }
                setTimeOut(10000);   //disabilita l'autoterminazione in caso di ritardo prolungato
                phase = 16;
                //</editor-fold>
            } else if (phase == 16) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 16: Froggysoft logo and game initialisation">
                if (!phase16_init) {
                    new FroggyLogoChronometer().start();
                    SoundBox.initSB();  //inizializza la SoundBox (migliori performance)
                    org.easyway.utils.ScriptInterpreter.init();
                    org.easyway.utils.ScriptInterpreter.eval("import mygame.*;");
                    CommonVar.initAnimos();  //inizializza le animazioni usate nel gioco
                    menuMusic = new MusicLoop("music/menu.mp3", 83059);
                    preCacheMenu();
                    phase16_init = true; //gioco inizializzato
                }
                //</editor-fold>
            } else if (phase == 17) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 17: Load Screen: Click to continue...">
                if (Mouse.isLeftPressed() || Mouse.isRightPressed() || Mouse.isMiddlePressed() || CommonVar.useGamePad) {
                    Mouse.setXY(CommonVar.xRes / 2, CommonVar.yRes / 2 - 1);
                    phase = 18;
                }
                //</editor-fold>
            } else if (phase == 18) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 18: Load screen destruction and game start">
                loading.destroy();  //distrugge la scritta caricamento
                resumeGame();
                phase = 7;    //si gioca!
                //</editor-fold>
            } else if (phase == 21) {
                // <editor-fold defaultstate="collapsed" desc="PHASE 21: Loads new level (used by LevelChanger class)">
                if (pmButton1 != null) {
                    pmButton1.destroy();
                    pmButton1 = null;
                }
                if (pmButton2 != null) {
                    pmButton2.destroy();
                    pmButton2 = null;
                }
                Mouse.show();   //ripristina il cursore
                if (CommonVar.musicL != null) {
                    CommonVar.musicL.stop();
                }
                hasSF = wi.getSuperFire();
                hasLasers = wi.getLasers();
                lives = wi.getLives();
                health = wi.getHealth();
                rocketCount = wi.getRocketCount();
                score = wi.getScore();
                shotsPerSecond = wi.getShotsPerSecond();
                CommonVar.musicL = null;
                CommonVar.destroyAll = true;
                StaticRef.getCamera().attrack = false;
                loading = new Sprite(0, 0, Texture.getTexture("images/menu/" + skin + "/" + loc + "/loading1.png"), 14);
                loading.setIdLayer(14);
                Camera.getCurrentCamera().setx(0);
                Camera.getCurrentCamera().sety(0);
                phase = 6;
                //</editor-fold>
            }
            if (inMenu && phase < 15) {
                if (CommonVar.useGamePad) {
                    if (Math.abs(GamePad.getXAxis()) > 0.2 || Math.abs(GamePad.getYAxis()) > 0.2) {
                        Mouse.setXY((int) (Mouse.getX() + GamePad.getXAxis() * 10), (int) (Mouse.getY() + GamePad.getYAxis() * 10));
                    }
                }
                cursor.setXY(Mouse.getX() / StaticRef.getCamera().getZoom2D(), Mouse.getY() / StaticRef.getCamera().getZoom2D());
            }
            /*if (Keyboard.isKeyPressed(Keyboard.KEY_F8)) {
            Texture screenShot = ImageUtils.getScreenShot();
            ImageUtils.savePngImage(screenShot, "screenShot" + System.nanoTime() + ".png");
            screenShot.destroy();
            }*/
        }
    }
}
