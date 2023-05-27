package com.example.roguelikesurvival.object.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.roguelikesurvival.Camera;
import com.example.roguelikesurvival.GameLoop;
import com.example.roguelikesurvival.R;
import com.example.roguelikesurvival.Utils;
import com.example.roguelikesurvival.object.Circle;
import com.example.roguelikesurvival.object.Player;
import com.example.roguelikesurvival.object.Spell;

import java.util.Iterator;

public class BasicAttack extends Spell {

    public static final double SPEED_PIXELS_PER_SECOND = 600.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private double attackPerMinute = 20;
    private double attackPerSecond = attackPerMinute / 60.0;
    private double updatePerAttack = GameLoop.MAX_UPS / attackPerSecond;
    private double updateUntilNextAttack = updatePerAttack;
    private static final float SPRITE_WIDTH = 192;
    private static final float SPRITE_HEIGHT = 96;
    private Bitmap[] fireBall = new Bitmap[5];
    private Bitmap[] fireBallL = new Bitmap[5];
    private int updateBeforeNextMove = 5;
    private int moveIdx = 0;
    private int jobs;

    public BasicAttack(Context context, Player spellcaster, int jobs) {
        super(context, spellcaster);

        this.jobs = jobs;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        fireBall[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fireball_01, bitmapOptions);
        fireBall[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fireball_02, bitmapOptions);
        fireBall[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fireball_03, bitmapOptions);
        fireBall[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fireball_04, bitmapOptions);
        fireBall[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fireball_05, bitmapOptions);

        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        //반대쪽 이동 애니메이션을 구현하기 위해 비트맵이미지 좌우반전
        fireBallL[0] = Bitmap.createBitmap(fireBall[0], 0, 0, (int) SPRITE_WIDTH, (int) SPRITE_HEIGHT, matrix, false);
        fireBallL[1] = Bitmap.createBitmap(fireBall[1], 0, 0, (int) SPRITE_WIDTH, (int) SPRITE_HEIGHT, matrix, false);
        fireBallL[2] = Bitmap.createBitmap(fireBall[2], 0, 0, (int) SPRITE_WIDTH, (int) SPRITE_HEIGHT, matrix, false);
        fireBallL[3] = Bitmap.createBitmap(fireBall[3], 0, 0, (int) SPRITE_WIDTH, (int) SPRITE_HEIGHT, matrix, false);
        fireBallL[4] = Bitmap.createBitmap(fireBall[4], 0, 0, (int) SPRITE_WIDTH, (int) SPRITE_HEIGHT, matrix, false);

        if(spellcaster.getDirectionX() > 0)
            velocityX = MAX_SPEED;
        else
            velocityX = -MAX_SPEED;
    }

    public boolean readyToAttack() {
        if (updateUntilNextAttack <= 0) {
            updateUntilNextAttack += updatePerAttack;
            return true;
        } else {
            updateUntilNextAttack--;
            return false;
        }
    }

    public void draw(Canvas canvas, Camera camera) {
        updateBeforeNextMove--;
        if (updateBeforeNextMove == 0) {
            updateBeforeNextMove = 5;
            if (moveIdx == 0)
                moveIdx++;
            else if (moveIdx == 1)
                moveIdx++;
            else if (moveIdx == 2)
                moveIdx++;
            else if (moveIdx == 3)
                moveIdx++;
            else
                moveIdx = 0;
        }

        if (velocityX > 0)
            canvas.drawBitmap(fireBall[moveIdx], (float) camera.gameToScreenCoordinateX(positionX) - (SPRITE_WIDTH / 2),
                    (float) camera.gameToScreenCoordinateY(positionY) - (SPRITE_HEIGHT / 2) + 40, null);
        else
            canvas.drawBitmap(fireBallL[moveIdx], (float) camera.gameToScreenCoordinateX(positionX) - (SPRITE_WIDTH / 2),
                    (float) camera.gameToScreenCoordinateY(positionY) - (SPRITE_HEIGHT / 2) + 40, null);

    }

    public void update() {
        positionX += velocityX;
    }
}
