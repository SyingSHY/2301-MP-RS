package com.example.roguelikesurvival.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.R;

public class GameTimer {
    private Context context;
    private long startTime;
    private long currentTime;
    private int minute = 0;
    private int second = 0;

    public GameTimer(Context context) {
        this.context = context;
        startTime = System.currentTimeMillis();
    }

    public void draw(Canvas canvas) {
        currentTime = System.currentTimeMillis() - startTime;
        minute = (int) (currentTime / (1000 * 60)) % 60;
        second = (int) (currentTime / 1000) % 60;

        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("시간: " + minute + ":" + second, 100, 100, paint);
    }

    public int getMinute(){
        return minute;
    }

    public int getSecond(){
        return second;
    }
}
