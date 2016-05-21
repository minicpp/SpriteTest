package me.handong.surfaceviewrender;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Random;

/**
 * Created by wynter on 5/11/2016.
 */
public class GameView extends SurfaceView {
    private Bitmap [] bmp;
    private Bitmap logo;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;

    Paint paint;
    Paint alphaPaint = new Paint();

    private int rate = 0;
    private int currentRate = 0;
    private long beginTime = 0;
    private long elapse = 0;
    private Random r = new Random();

    private int screenWidth = 0;
    private int screenHeight = 0;
    private int leftBound = 0;
    private int rightBound = 0;
    private int topBound = 0;
    private int bottomBound = 0;

    private int spriteSize = 0;
    private int spriteIncreaseStep = 100;
    int spWidth = 32;
    int spHeight = 32;
    private Sprite [] spArr = null;
    boolean touched = false;

    public GameView(Context context) {
        super(context);

        //this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;



        loadBmp();


        Log.v("game", "width:"+screenWidth+" height:"+screenHeight);
        leftBound = spWidth/2;
        rightBound = screenWidth - spHeight/2;
        topBound = spHeight/2+100;
        bottomBound = screenHeight - spHeight/2;
    }

    private void loadBmp(){
        bmp = new Bitmap[9];
        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.ball1);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.ball2);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.ball3);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.ball4);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.ball5);
        bmp[5] = BitmapFactory.decodeResource(getResources(), R.drawable.ball6);
        bmp[6] = BitmapFactory.decodeResource(getResources(), R.drawable.ball7);
        bmp[7] = BitmapFactory.decodeResource(getResources(), R.drawable.ball8);
        bmp[8] = BitmapFactory.decodeResource(getResources(), R.drawable.ball9);
        logo = BitmapFactory.decodeResource(getResources(), R.drawable.android);
    }

    double getSign(double v) {
        return v >= 0.0 ? 1.0 : -1.0;
    }

    private void explode(){

        int count = 0;
        int id = 0;
        double degree;
        double speed;
        this.spriteSize += this.spriteIncreaseStep;
        spArr = new Sprite[this.spriteSize];
        int seg = spArr.length/bmp.length;

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

    protected void myDraw(Canvas canvas) {

        if(this.touched){
            explode();
            this.touched = false;
        }

        if(canvas == null) return;

        if(beginTime == 0)
            beginTime = SystemClock.elapsedRealtime();



        if(canvas != null && paint == null){
            paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(50);
            alphaPaint.setAlpha(185);

            //canvas.drawPaint(paint);
        }

        canvas.drawColor(Color.BLACK);
        //paint.setColor(Color.WHITE);
        //paint.setTextSize(20);
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
                sp.draw(canvas);
            }
        }

        canvas.drawBitmap(logo,screenWidth/2-logo.getWidth()/2, screenHeight/2 - logo.getHeight()/2, alphaPaint);

        //Log.v("width",""+bmp.getWidth());
        canvas.drawText("Android Canvas, Sprite Amount:"+spriteSize +", FPS: "+currentRate, 50, 50, paint);
        ++rate;
        elapse = SystemClock.elapsedRealtime() - beginTime;
        if(elapse > 1000){ //1 second
            elapse = 0;
            beginTime = SystemClock.elapsedRealtime();
            currentRate = rate;
            rate = 0;
            //Log.v("canvas", canvas.isHardwareAccelerated()+"");
            //Log.v("view", this.isHardwareAccelerated()+"");
        }
    }
}

class Sprite{
    public Bitmap bmp;
    public Rect imgRect;
    public Rect destRect;

    public double speedX = 0;
    public double speedY = 0;

    private double posX;
    private double posY;
    private double halfWidth;
    private double halfHeight;

    public Sprite(Bitmap bmp, double x, double y,double width, double height){
        this.bmp = bmp;
        setSize(width, height);
        this.posX = x;
        this.posY = y;
        destRect = new Rect();
        imgRect = new Rect(0,0,bmp.getWidth(),bmp.getHeight());
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

    public void draw(Canvas canvas){
        canvas.drawBitmap(bmp, imgRect, getDest(), null);
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