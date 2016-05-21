package me.handong.surfaceviewrender;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by wynter on 5/11/2016.
 */
public class GameLoopThread extends Thread {
    private GameView view;
    private boolean running = false;


    public GameLoopThread(GameView view) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }

    @Override
    public void run() {

        while (running) {
            Canvas c = null;
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.myDraw(c);

                }

            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                    //Log.v("canvas", c.isHardwareAccelerated()+"");
                }
            }
        }
    }
}