package me.handong.surfaceviewrender;

import android.app.Activity;
import android.content.Context;
import android.os.PowerManager;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity
{
    private GameView gview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gview = new GameView(this);
        setContentView(gview);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == android.view.MotionEvent.ACTION_UP) {
            gview.touched = true;
        }
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

}
