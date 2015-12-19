/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame;

import org.easyway.objects.sprites2D.Sprite;
import org.easyway.objects.texture.Texture;

/**
 *
 * @author Do$$e
 */
public class HiScoreWriter extends Sprite{
Sprite hiScore1[];
Sprite hiScore2[];
Sprite hiScore3[];
Sprite easy,normal,hard;
String skin=CommonVar.skin;
    public HiScoreWriter(float x, float y,String skin) {
        super(x, y,Texture.getTexture("images/menu/"+skin+"/hiScore.png"),6);
        this.setSmoothImage(CommonVar.smoothing);
        hiScore1=new Sprite[6];
        hiScore2=new Sprite[6];
        hiScore3=new Sprite[6];
        easy=new Sprite(x-40,y+getHeight(),Texture.getTexture("images/menu/"+skin+"/hsEasy.png"),6);
        normal=new Sprite(x-40,easy.getY()+easy.getHeight()+16,Texture.getTexture("images/menu/"+skin+"/hsNormal.png"),6);
        hard=new Sprite(x-40,normal.getY()+normal.getHeight()+16,Texture.getTexture("images/menu/"+skin+"/hsHard.png"),6);
        for(int i=0;i<hiScore1.length;i++){
            if(i==0){
                hiScore1[i]=new Sprite(easy.getX()+easy.getWidth()+16,easy.getY(),Texture.getTexture("images/menu/"+skin+"/bigNumbers/0.png"),6);
                hiScore1[i].setSmoothImage(CommonVar.smoothing);
                }else{
                hiScore1[i]=new Sprite(hiScore1[i-1].getX()+hiScore1[i-1].getWidth(),easy.getY(),Texture.getTexture("images/menu/"+skin+"/bigNumbers/0.png"),6);
                hiScore1[i].setSmoothImage(CommonVar.smoothing);
                }
        }
        for(int i=0;i<hiScore2.length;i++){
            if(i==0){
                hiScore2[i]=new Sprite(normal.getX()+normal.getWidth()+16,normal.getY(),Texture.getTexture("images/menu/"+skin+"/bigNumbers/0.png"),6);
                hiScore2[i].setSmoothImage(CommonVar.smoothing);
                }else{
                hiScore2[i]=new Sprite(hiScore2[i-1].getX()+hiScore2[i-1].getWidth(),normal.getY(),Texture.getTexture("images/menu/"+skin+"/bigNumbers/0.png"),6);
                hiScore2[i].setSmoothImage(CommonVar.smoothing);
                }
        }
        for(int i=0;i<hiScore3.length;i++){
            if(i==0){
                hiScore3[i]=new Sprite(hard.getX()+hard.getWidth()+16,hard.getY(),Texture.getTexture("images/menu/"+skin+"/bigNumbers/0.png"),6);
                hiScore3[i].setSmoothImage(CommonVar.smoothing);
                }else{
                hiScore3[i]=new Sprite(hiScore3[i-1].getX()+hiScore3[i-1].getWidth(),hard.getY(),Texture.getTexture("images/menu/"+skin+"/bigNumbers/0.png"),6);
                hiScore3[i].setSmoothImage(CommonVar.smoothing);
                }
        }
        String aux;
        if(CommonVar.hiScore1<100000&&CommonVar.hiScore1>=10000)
            aux="0"+CommonVar.hiScore1;
        else
            if(CommonVar.hiScore1<10000&&CommonVar.hiScore1>=1000)
                aux="00"+CommonVar.hiScore1;
            else
                if(CommonVar.hiScore1<1000&&CommonVar.hiScore1>=100)
                    aux="000"+CommonVar.hiScore1;
                else
                    if(CommonVar.hiScore1<100&&CommonVar.hiScore1>=10)
                        aux="0000"+CommonVar.hiScore1;
                    else
                        if(CommonVar.hiScore1<10)
                            aux="00000"+CommonVar.hiScore1;
                        else
                            aux=""+CommonVar.hiScore1;
        for(int i=0;i<hiScore1.length;i++)
            hiScore1[i].setImage(Texture.getTexture("images/menu/"+skin+"/bigNumbers/"+aux.charAt(i)+".png"));

        if(CommonVar.hiScore2<100000&&CommonVar.hiScore2>=10000)
            aux="0"+CommonVar.hiScore2;
        else
            if(CommonVar.hiScore2<10000&&CommonVar.hiScore2>=1000)
                aux="00"+CommonVar.hiScore2;
            else
                if(CommonVar.hiScore2<1000&&CommonVar.hiScore2>=100)
                    aux="000"+CommonVar.hiScore2;
                else
                    if(CommonVar.hiScore2<100&&CommonVar.hiScore2>=10)
                        aux="0000"+CommonVar.hiScore2;
                    else
                        if(CommonVar.hiScore2<10)
                            aux="00000"+CommonVar.hiScore2;
                        else
                            aux=""+CommonVar.hiScore2;
        for(int i=0;i<hiScore2.length;i++)
            hiScore2[i].setImage(Texture.getTexture("images/menu/"+skin+"/bigNumbers/"+aux.charAt(i)+".png"));

        if(CommonVar.hiScore3<100000&&CommonVar.hiScore3>=10000)
            aux="0"+CommonVar.hiScore3;
        else
            if(CommonVar.hiScore3<10000&&CommonVar.hiScore3>=1000)
                aux="00"+CommonVar.hiScore3;
            else
                if(CommonVar.hiScore3<1000&&CommonVar.hiScore3>=100)
                    aux="000"+CommonVar.hiScore3;
                else
                    if(CommonVar.hiScore3<100&&CommonVar.hiScore3>=10)
                        aux="0000"+CommonVar.hiScore3;
                    else
                        if(CommonVar.hiScore3<10)
                            aux="00000"+CommonVar.hiScore3;
                        else
                            aux=""+CommonVar.hiScore3;
        for(int i=0;i<hiScore3.length;i++)
            hiScore3[i].setImage(Texture.getTexture("images/menu/"+skin+"/bigNumbers/"+aux.charAt(i)+".png"));
    }
    @Override
    protected void onDestroy(){
        for(int i=0;i<hiScore1.length;i++)
            hiScore1[i].destroy();
        for(int i=0;i<hiScore2.length;i++)
            hiScore2[i].destroy();
        for(int i=0;i<hiScore3.length;i++)
            hiScore3[i].destroy();
        easy.destroy();
        normal.destroy();
        hard.destroy();
        this.kill();
    }
}
