package com.example.roguelikesurvival.gamepanel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;

import com.example.roguelikesurvival.Game;
import com.example.roguelikesurvival.R;
import com.example.roguelikesurvival.Utils;

public class SkillButton {
    private int centerX;
    private int centerY;
    private int radius;
    private Paint paint;
    private boolean isPressed;

    private Game game;
    private Bitmap buttonBitmap;
    private Bitmap buttonPressedBitmap;

    private long lastClickTime;
    private long skillCooldown = 30000;  // 쿨타임: 30초

    public SkillButton(int centerX, int centerY, int radius, Game game) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.game = game;

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        // 버튼 이미지 로드
        buttonBitmap = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.green_button);
        buttonPressedBitmap = BitmapFactory.decodeResource(game.getContext().getResources(), R.drawable.green_button_pushed);
    }

    public void draw(Canvas canvas) {
        // 현재 버튼의 상태에 따라서 그리기
        if (isPressed || !isCooldownElapsed()) {
            canvas.drawBitmap(buttonPressedBitmap, centerX - radius, centerY - radius, null);
        } else {
            canvas.drawBitmap(buttonBitmap, centerX - radius, centerY - radius, null);
        }
    }

    public boolean isPressed(double touchPosX, double touchPosY) {
        double distance = Utils.getDistanceBetweenPoints(centerX, centerY, touchPosX, touchPosY);
        return distance < radius;
    }

    public void setIsPressed(boolean isPressed) {
        if (isPressed && isCooldownElapsed()) {
            lastClickTime = SystemClock.elapsedRealtime();
            game.onSkillButtonPressed();
        }
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    private boolean isCooldownElapsed() {
        long elapsedTime = SystemClock.elapsedRealtime() - lastClickTime;
        return elapsedTime >= skillCooldown;
    }

    public void update() {
        if (!isCooldownElapsed()) {
            // 쿨타임 중일 때는 버튼을 누른 상태로 유지
            isPressed = true;
        } else {
            // 쿨타임이 끝났을 때는 버튼을 누르지 않은 상태로 변경
            isPressed = false;
        }
    }
}

