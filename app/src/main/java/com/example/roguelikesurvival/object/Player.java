package com.example.roguelikesurvival.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.GameLoop;
import com.example.roguelikesurvival.Camera;
import com.example.roguelikesurvival.gamepanel.Joystick;
import com.example.roguelikesurvival.R;
import com.example.roguelikesurvival.Utils;
import com.example.roguelikesurvival.gamepanel.HealthBar;

public abstract class Player extends Circle {

    public static final double SPEED_PIXELS_PER_SECOND = 400.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static final float SPRITE_WIDTH = 95;
    private static final float SPRITE_HEIGHT = 167;
    private int maxHealthPoint = 10;
    private int level;
    protected boolean isUsingSkill = false;

    public Player(Context context, Joystick joystick, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
    }

    public boolean isUsingSkill() {
        return isUsingSkill;
    }

    public abstract void update();

    public abstract void draw(Canvas canvas, Camera camera);

    public abstract int getMaxHealthPoint();

    public abstract void plusMaxHealthPoint(int point);

    public abstract int getHealthPoint();

    public abstract int getAttackPower();

    public abstract void plusAttackPower(int point);

    public abstract void plusSpeed(int point);

    public abstract double getSpeed();

    public abstract void setHealthPoint(int healthPoint);

    public abstract void levelUp();

    public abstract int getLevel();

    public abstract boolean isDamage();

    public void useSkill() {

    }
}
