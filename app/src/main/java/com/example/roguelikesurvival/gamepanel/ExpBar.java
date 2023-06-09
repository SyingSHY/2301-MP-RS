package com.example.roguelikesurvival.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Display;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.Camera;
import com.example.roguelikesurvival.MainActivity;
import com.example.roguelikesurvival.R;
import com.example.roguelikesurvival.SelectItem;
import com.example.roguelikesurvival.object.Player;

public class ExpBar {
    private Player player;
    private Context context;
    private SelectItem selectItem;
    private int width, height;
    private Paint borderPaint, healthPaint;
    private int maxExpPoint = 5;
    private int expPoint = 0;

    public ExpBar(Context context, Player player, SelectItem selectItem) {
        this.player = player;
        this.context = context;
        this.selectItem = selectItem;
        this.width = MainActivity.displayWidth;
        this.height = 30;

        this.borderPaint = new Paint();
        int borderColor = ContextCompat.getColor(context, R.color.healthBarBolder);
        borderPaint.setColor(borderColor);

        this.healthPaint = new Paint();
        int healthColor = ContextCompat.getColor(context, R.color.healthBarHealth);
        healthPaint.setColor(healthColor);
    }

    public void draw(Canvas canvas) {
        float x = (float) player.getPositionX();
        float y = (float) player.getPositionY();
        float expPointPercentage = (float) expPoint / maxExpPoint;

        //border 그리기
        float borderLeft, borderTop, borderRight, borderBottom;
        borderLeft = 0;
        borderRight = width;
        borderBottom = height;
        borderTop = 0;
        canvas.drawRect(borderLeft, borderTop, borderRight, borderBottom, borderPaint);

        //expBar 그리기
        float expLeft, expTop, expRight, expBottom;
        expLeft = 0;
        expRight = expLeft + width * expPointPercentage;
        expBottom = height;
        expTop = 0;
        canvas.drawRect(
                expLeft, expTop, expRight, expBottom, healthPaint);

        Paint paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Level: " + player.getLevel(), 10, 90, paint);
    }

    public void update(){
        if(maxExpPoint <= expPoint){
            maxExpPoint = (maxExpPoint * 2) - (maxExpPoint / 2);
            expPoint = 0;
            player.levelUp();
            selectItem.levelUp();
        }
    }

    public void plusExpPoint(int plusNum){
        expPoint += plusNum;
    }
}
