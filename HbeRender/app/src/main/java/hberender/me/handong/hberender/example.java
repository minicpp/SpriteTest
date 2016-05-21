package hberender.me.handong.hberender;


import android.graphics.Rect;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;


import java.util.Random;

import game.hummingbird.HbeConfig;
import game.hummingbird.core.HbEngine;
import game.hummingbird.core.HbeActivity;
import game.hummingbird.core.HbeColor;
import game.hummingbird.core.HbeTexture;
import game.hummingbird.helper.HbeAnimation;
import game.hummingbird.helper.HbeFont;
import game.hummingbird.helper.HbeHelper;

import game.hummingbird.helper.HbeKeyControl;
import game.hummingbird.helper.HbeParticleSystem;
import game.hummingbird.helper.HbeParticleSystemInfo;
import game.hummingbird.helper.HbeSprite;
import game.hummingbird.helper.HbeTouchControl;


public class example extends HbeActivity {

    private HbeSprite sprite;
    //private HbeTexture texture;
    //private HbeAnimation ani;
    //private HbeParticleSystem par;
    //private HbeParticleSystemInfo info;

    private Random r = new Random();

    private int screenWidth = 0;
    private int screenHeight = 0;
    private int leftBound = 0;
    private int rightBound = 0;
    private int topBound = 0;
    private int bottomBound = 0;


    HbeTexture [] bmp;
    int spriteSize = 0;
    int spriteIncreaseStep = 100;
    private Sprite [] spArr = null;
    private HbeSprite bird;
    private HbeTexture birdTexture;

    private HbeFont _fontFPS;
    private StringBuilder _strbFPS;

    private void loadBmp(){
        bmp = new HbeTexture[9];
        bmp[0] = HbEngine.textureLoad("ball1.png");
        bmp[1] = HbEngine.textureLoad("ball2.png");
        bmp[2] = HbEngine.textureLoad("ball3.png");
        bmp[3] = HbEngine.textureLoad("ball4.png");
        bmp[4] = HbEngine.textureLoad("ball5.png");
        bmp[5] = HbEngine.textureLoad("ball6.png");
        bmp[6] = HbEngine.textureLoad("ball7.png");
        bmp[7] = HbEngine.textureLoad("ball8.png");
        bmp[8] = HbEngine.textureLoad("ball9.png");
        birdTexture = HbEngine.textureLoad("hummingbird.png");
    }

    private void ShowFPS(){
        if(HbeConfig.IS_SHOW_FPS){
            _strbFPS.setLength(0);//just reset length, buffer is not released.
            _strbFPS.append("Hummingbird Engine, Sprite Amount:"+this.spriteSize+", FPS:");
            _strbFPS.append(HbEngine.timerGetFPS());
            _fontFPS.render(50, 20, _strbFPS);
        }
    }

    public boolean gameMain()
    {
        Log.v("HbEngine:","gameMain()");
        HbEngine.sysSetActivity(this);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        HbEngine.sysSetScreen(metrics.widthPixels, metrics.heightPixels);
        Log.v("Window Size:", "width="+metrics.widthPixels+", height="+metrics.heightPixels);

        this.screenWidth = metrics.widthPixels;
        this.screenHeight = metrics.heightPixels;

        HbEngine.sysSetFPS(60);
        HbEngine.randomSeed(0);
        HbEngine.sysInit();
        HbeHelper.helperLibInit();

        HbeConfig.REAL_WINDOW_WIDTH=metrics.widthPixels;
        HbeConfig.REAL_WINDOW_HEIGHT=metrics.heightPixels;
        HbeTouchControl.InitTouch();

        return true;
    }


    double getSign(double v) {
        return v >= 0.0 ? 1.0 : -1.0;
    }

    public boolean gameUpdate()
    {
        //par.update(1.0f/40.0f);
        //ani.update(1/40.0f);


        for (int i = 0; i < HbeKeyControl.GetCurButtonNum(); i++) {
            if (HbeKeyControl.GetKeyArray(i).keyCode == KeyEvent.KEYCODE_BACK) {
                if (HbeKeyControl.GetKeyArray(i).KeyAction == HbeKeyControl.KEY_ACTION_UP) {
                    HbEngine.sysExit();
                }
            }
            //HbeKeyControl.remove();
        }

        if(HbeTouchControl.isJustPressed()){
            Log.v("touch","pressed");
            explode();
            HbeTouchControl.ResetJustPressed();
        }


        double speedX, speedY;
        double sign = 0;
        if(spArr != null) {
            for (Sprite sp : spArr) {
                speedX = sp.speedX;
                speedY = sp.speedY;

                if (sp.getPosX() < leftBound || sp.getPosX() > rightBound) {
                    speedX = -speedX;
                    sign = getSign(speedX);
                    if (Math.abs(speedX) <= 1.0) {
                        speedX = sign * (Math.abs(speedX) + 5.0 * r.nextDouble());
                    }
                }


                if (sp.getPosY() < topBound || sp.getPosY() > bottomBound) {
                    speedY = -speedY;
                    sign = getSign(speedY);
                    if (Math.abs(speedY) <= 1.0) {
                        speedY = sign * (Math.abs(speedY) + 5.0 * r.nextDouble());
                    }
                }

                sp.setSpeed(speedX, speedY);

                sp.update();

            }
        }

        return true;
    }
    public void gameDraw()
    {
        HbEngine.graphicClear(HbeColor.convertColor(0, 0, 0, 255));

        double speedX, speedY;
        if(spArr != null) {
            for (Sprite sp : spArr) {

                sp.draw();
            }
        }
        bird.render(screenWidth/2-birdTexture.width/2, screenHeight/2-birdTexture.height/2);

        ShowFPS();
    }
    @Override
    public void gameExit() {
        // TODO Auto-generated method stub
        Log.v("HbEngien:","gameExit()");
    }

    public void explode(){
        int spWidth = 32;
        int spHeight = 32;
        double degree;
        double speed;
        this.spriteSize += this.spriteIncreaseStep;
        spArr = new Sprite[this.spriteSize];
        int seg = spArr.length/bmp.length;
        int count = 0;
        int id = 0;
        for(int i=0; i<spArr.length; ++i){
            ++count;
            if(count > seg){
                count = 0;
                ++id;
            }
            degree = r.nextDouble()*Math.PI*2.0;
            speed = 1.0 + r.nextDouble()*10;
            spArr[i] = new Sprite(bmp[id%9], screenWidth/2, screenHeight/2, spWidth, spHeight);
            spArr[i].setSpeed( speed*Math.cos(degree), speed*Math.sin(degree));
        }
    }

    @Override
    public boolean gameInit() {
        // TODO Auto-generated method stub
        Log.v("HbEngien:","gameInit()");

        _fontFPS = new HbeFont(HbeConfig.CHAR_STANDARD);
        _fontFPS.setColor(255,255,255,255);
        _fontFPS.setScale(2.0f);
        _fontFPS.setProportion(0.4f);
        _fontFPS.setTracking(-5.0f);
        _strbFPS = new StringBuilder(10);

        loadBmp();

        this.bird = new HbeSprite(birdTexture,0, 0, birdTexture.width, birdTexture.height);
        bird.setColor(HbeColor.setA(0xFFFFFFFF, 185));


        int spWidth = 32;
        int spHeight = 32;

        leftBound = spWidth/2;
        rightBound = screenWidth - spHeight/2;
        topBound = spHeight/2 + 100;
        bottomBound = screenHeight - spHeight/2;

        //texture = HbEngine.textureLoad("particles.png");
        //sprite =new HbeSprite(texture, 0, 0, 32, 32);
        //sprite.setBlendMode(HbEngine.BLEND_ALPHAADD);
        //sprite.setHotSpot(16, 16);
        //HbeTexture tt = HbEngine.textureLoad("bat.png", 0, 0, 256, 128);
        //ani = new HbeAnimation(tt, 4, 8, 0, 0, 64, 128);
        //ani.setHotSpot(32, 32);
        //ani.play();

        //par =new HbeParticleSystem("particle1.psi", sprite);
       // par.info.bReverse = true;
        //par.info.fRadiusMax = 200.0f;
       // par.info.fRadiusMin = 110.0f;
        //par.info.fTangentialAccelMax = 0;
        //par.info.fTangentialAccelMin = 0;
       //par.fireAt(112, 192);

        return true;
    }
}

class Sprite{

    public HbeSprite sp;

    public Rect imgRect;
    public Rect destRect;

    public double speedX = 0;
    public double speedY = 0;

    private double posX;
    private double posY;
    private double halfWidth;
    private double halfHeight;

    public Sprite(HbeTexture bmp, double x, double y,double width, double height){
        sp = new HbeSprite(bmp, 0, 0, bmp.width, bmp.height);
        setSize(width, height);
        this.posX = x;
        this.posY = y;
        destRect = new Rect();
        imgRect = new Rect(0,0,bmp.width, bmp.height);
    }

    public double getPosX(){return posX;}
    public double getPosY(){return posY;}

    public int getWidth(){return imgRect.right; }
    public int getHeight(){return imgRect.bottom;}

    public void setSpeed(double x, double y){
        this.speedX = x;
        this.speedY = y;
    }

    public void update(){
        this.posX += this.speedX;
        this.posY += this.speedY;
    }

    public void draw(){
        getDest();
        sp.renderStretch(destRect.left, destRect.top, destRect.right, destRect.bottom);
    }

    public void setPosition(double x, double y){
        posX = x;
        posY = y;
    }
    public void setSize(double width, double height){
        this.halfWidth = width/2.0;
        this.halfHeight = height/2.0;
    }
    public Rect getDest(){
        destRect.left = (int)(posX - halfWidth);
        destRect.top = (int)(posY - halfHeight);
        destRect.right = (int)(posX + halfWidth);
        destRect.bottom = (int)(posY + halfHeight);
        return destRect;
    }

    public Rect getSrc(){
        return imgRect;
    }
}