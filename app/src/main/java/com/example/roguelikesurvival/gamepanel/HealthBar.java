package com.example.roguelikesurvival.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.Camera;
import com.example.roguelikesurvival.R;
import com.example.roguelikesurvival.object.Player;

public class HealthBar {
    private Player player;
    private int width, height, margin;
    private Paint borderPaint, healthPaint;

    public HealthBar(Context context, Player player) {
        this.player = player;
        this.width = 100;
        this.height = 20;
        this.margin = 2;

        this.borderPaint = new Paint();
        int borderColor = ContextCompat.getColor(context, R.color.healthBarBolder);
        borderPaint.setColor(borderColor);

        this.healthPaint = new Paint();
        int healthColor = ContextCompat.getColor(context, R.color.healthBarHealth);
        healthPaint.setColor(healthColor);
    }

    public void draw(Canvas cansvas, Camera camera) {
        float x = (float) player.getPositionX();
        float y = (float) player.getPositionY();
        float distanceToPlayer = 50;
        float healthPointPercentage = (float) player.getHealthPoint() / player.MAX_HEALTH_POINT;

        //border 그리기
        float borderLeft, borderTop, borderRight, borderBottom;
        borderLeft = x - width / 2;
        borderRight = x + width / 2;
        borderBottom = y + distanceToPlayer;
        borderTop = borderBottom - height;
        cansvas.drawRect(
                (float) camera.gameToScreenCoordinateX(borderLeft),
                (float) camera.gameToScreenCoordinateY(borderTop),
                (float) camera.gameToScreenCoordinateX(borderRight),
                (float) camera.gameToScreenCoordinateY(borderBottom),
                borderPaint);

        //체력 그리기
        float healthLeft, healthTop, healthRight, healthBottom, healthWidth, healthHeight;
        healthWidth = width - 2 * margin;
        healthHeight = height - 2 * margin;
        healthLeft = borderLeft + margin;
        healthRight = healthLeft + healthWidth * healthPointPercentage;
        healthBottom = borderBottom - margin;
        healthTop = healthBottom - healthHeight;
        cansvas.drawRect(
                (float) camera.gameToScreenCoordinateX(healthLeft),
                (float) camera.gameToScreenCoordinateY(healthTop),
                (float) camera.gameToScreenCoordinateX(healthRight),
                (float) camera.gameToScreenCoordinateY(healthBottom),
                healthPaint);
    }
}
