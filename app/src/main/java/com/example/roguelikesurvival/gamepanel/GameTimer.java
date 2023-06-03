package com.example.roguelikesurvival.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.R;
import com.example.roguelikesurvival.SelectItem;

public class GameTimer {
    private Context context;
    private long startTime;
    private long currentTime;
    private long levelUpTime;
    private long stopTime;
    private boolean isStop = false;
    private int minute = 0;
    private int second = 0;

    public GameTimer(Context context) {
        this.context = context;
        startTime = System.currentTimeMillis();
        levelUpTime = 0;
        stopTime = 0;
    }

    public void draw(Canvas canvas, SelectItem selectItem) {
        //1.레벨업시 실행
        if(selectItem.isLevelUp() && isStop == false && levelUpTime == 0) {
            isStop = true;
        }//3.멈춰있는 시간만큼 stopTime에 저장
        else if(selectItem.isLevelUp() && isStop == false && levelUpTime != 0){
            stopTime = System.currentTimeMillis() - levelUpTime;
        }
        //2.레벨업한 시점의 시간을 저장
        if(selectItem.isLevelUp() && isStop == true){
            levelUpTime = System.currentTimeMillis();
            isStop = false;
        }
        //4.아이템선택이 끝나면 멈춘 시간만큼 startTime에 더하여 draw되는 시간이 멈추게함
        if(!selectItem.isLevelUp()){
            startTime += stopTime;
            levelUpTime = 0;
            stopTime = 0;
        }

        currentTime = System.currentTimeMillis() - startTime - stopTime;
        minute = (int) (currentTime / (1000 * 60)) % 60;
        second = (int) (currentTime / 1000) % 60;

        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        canvas.drawText("시간: " + minute + ":" + second, 800, 90, paint);
    }

    public int getMinute(){
        return minute;
    }

    public int getSecond(){
        return second;
    }
}
