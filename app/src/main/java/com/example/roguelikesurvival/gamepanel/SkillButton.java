package com.example.roguelikesurvival.gamepanel;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.roguelikesurvival.Game;
import com.example.roguelikesurvival.Utils;

public class SkillButton {
    private int centerX;
    private int centerY;
    private int radius;
    private Paint paint;
    private boolean isPressed;

    private Game game;

    public SkillButton(int centerX, int centerY, int radius, Game game) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.game = game;


        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    public boolean isPressed(double touchPosX, double touchPosY) {
        double distance = Utils.getDistanceBetweenPoints(centerX, centerY, touchPosX, touchPosY);
        return distance < radius;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
        if (isPressed) {
            game.onSkillButtonPressed();
        }
    }

    public boolean getIsPressed() {
        return isPressed;
    }
}
