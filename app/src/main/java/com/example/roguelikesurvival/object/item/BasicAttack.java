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
import com.example.roguelikesurvival.object.Enemy;
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
    private static final float FIREBALL_SPRITE_WIDTH = 192;
    private static final float FIREBALL_SPRITE_HEIGHT = 96;
    private static final float SWORDEFFECT_SPRITE_WIDTH = 128;
    private static final float SWORDEFFECT_SPRITE_HEIGHT = 108;
    private Player spellcaster;
    private Bitmap[] fireBall = new Bitmap[5];
    private Bitmap[] fireBallL = new Bitmap[5];
    private Bitmap[] swordEffect = new Bitmap[5];
    private Bitmap[] swordEffectL = new Bitmap[5];
    private int updateBeforeNextAttack = 5;
    private int moveIdx = 0;
    private boolean animationState = false;
    private boolean damageState = false;
    private int jobs;
    // 공격방향 0이 오른쪽 1이 왼쪽
    private int attackDirection = 0;

    public BasicAttack(Context context, Player spellcaster, int jobs, int radius) {
        super(context, spellcaster, radius);

        this.spellcaster = spellcaster;
        this.jobs = jobs;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        fireBall[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fireball_01, bitmapOptions);
        fireBall[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fireball_02, bitmapOptions);
        fireBall[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fireball_03, bitmapOptions);
        fireBall[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fireball_04, bitmapOptions);
        fireBall[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.fireball_05, bitmapOptions);

        swordEffect[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.sword_effect_01, bitmapOptions);
        swordEffect[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.sword_effect_02, bitmapOptions);
        swordEffect[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.sword_effect_03, bitmapOptions);
        swordEffect[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.sword_effect_04, bitmapOptions);
        swordEffect[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.sword_effect_05, bitmapOptions);

        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        //반대쪽 애니메이션을 구현하기 위해 비트맵이미지 좌우반전
        fireBallL[0] = Bitmap.createBitmap(fireBall[0], 0, 0, (int) FIREBALL_SPRITE_WIDTH, (int) FIREBALL_SPRITE_HEIGHT, matrix, false);
        fireBallL[1] = Bitmap.createBitmap(fireBall[1], 0, 0, (int) FIREBALL_SPRITE_WIDTH, (int) FIREBALL_SPRITE_HEIGHT, matrix, false);
        fireBallL[2] = Bitmap.createBitmap(fireBall[2], 0, 0, (int) FIREBALL_SPRITE_WIDTH, (int) FIREBALL_SPRITE_HEIGHT, matrix, false);
        fireBallL[3] = Bitmap.createBitmap(fireBall[3], 0, 0, (int) FIREBALL_SPRITE_WIDTH, (int) FIREBALL_SPRITE_HEIGHT, matrix, false);
        fireBallL[4] = Bitmap.createBitmap(fireBall[4], 0, 0, (int) FIREBALL_SPRITE_WIDTH, (int) FIREBALL_SPRITE_HEIGHT, matrix, false);

        swordEffectL[0] = Bitmap.createBitmap(swordEffect[0], 0, 0, (int) SWORDEFFECT_SPRITE_WIDTH, (int) SWORDEFFECT_SPRITE_HEIGHT, matrix, false);
        swordEffectL[1] = Bitmap.createBitmap(swordEffect[1], 0, 0, (int) SWORDEFFECT_SPRITE_WIDTH, (int) SWORDEFFECT_SPRITE_HEIGHT, matrix, false);
        swordEffectL[2] = Bitmap.createBitmap(swordEffect[2], 0, 0, (int) SWORDEFFECT_SPRITE_WIDTH, (int) SWORDEFFECT_SPRITE_HEIGHT, matrix, false);
        swordEffectL[3] = Bitmap.createBitmap(swordEffect[3], 0, 0, (int) SWORDEFFECT_SPRITE_WIDTH, (int) SWORDEFFECT_SPRITE_HEIGHT, matrix, false);
        swordEffectL[4] = Bitmap.createBitmap(swordEffect[4], 0, 0, (int) SWORDEFFECT_SPRITE_WIDTH, (int) SWORDEFFECT_SPRITE_HEIGHT, matrix, false);


        if (spellcaster.getDirectionX() > 0)
            velocityX = MAX_SPEED;
        else
            velocityX = -MAX_SPEED;
    }

    public boolean readyToAttack() {
        if (updateUntilNextAttack <= 0) {
            updateUntilNextAttack += updatePerAttack;
            // 공격방향 설정
            if (spellcaster.getDirectionX() > 0)
                attackDirection = 0;
            else
                attackDirection = 1;
            return true;
        } else {
            updateUntilNextAttack--;
            return false;
        }
    }

    public void draw(Canvas canvas, Camera camera) {

        updateBeforeNextAttack--;
        if (updateBeforeNextAttack == 0) {
            updateBeforeNextAttack = 5;
            if (moveIdx == 0) {
                moveIdx++;
                damageState = false;
            }
            else if (moveIdx == 1)
                moveIdx++;
            else if (moveIdx == 2)
                moveIdx++;
            else if (moveIdx == 3)
                moveIdx++;
            else {
                moveIdx = 0;
                animationState = false;
            }
        }

        // 전사일때
        if (jobs == 0) {
            if (animationState) {
                if (attackDirection == 0)
                    canvas.drawBitmap(swordEffect[moveIdx],
                            (float) camera.gameToScreenCoordinateX(spellcaster.getPositionX()) - (SWORDEFFECT_SPRITE_WIDTH / 2) + 100,
                            (float) camera.gameToScreenCoordinateY(spellcaster.getPositionY()) - (SWORDEFFECT_SPRITE_HEIGHT / 2) + 40, null);
                else if (attackDirection == 1)
                    canvas.drawBitmap(swordEffectL[moveIdx],
                            (float) camera.gameToScreenCoordinateX(spellcaster.getPositionX()) - (SWORDEFFECT_SPRITE_WIDTH / 2) - 100,
                            (float) camera.gameToScreenCoordinateY(spellcaster.getPositionY()) - (SWORDEFFECT_SPRITE_HEIGHT / 2) + 40, null);
            }
        }
        // 마법사일때
        else if (jobs == 1) {
            if (velocityX > 0)
                canvas.drawBitmap(fireBall[moveIdx], (float) camera.gameToScreenCoordinateX(positionX) - (FIREBALL_SPRITE_WIDTH / 2),
                        (float) camera.gameToScreenCoordinateY(positionY) - (FIREBALL_SPRITE_HEIGHT / 2) + 40, null);
            else
                canvas.drawBitmap(fireBallL[moveIdx], (float) camera.gameToScreenCoordinateX(positionX) - (FIREBALL_SPRITE_WIDTH / 2),
                        (float) camera.gameToScreenCoordinateY(positionY) - (FIREBALL_SPRITE_HEIGHT / 2) + 40, null);
        }
    }

    public void update() {
        // 전사일때
        if (jobs == 0) {

        }
        // 마법사일때
        else if (jobs == 1)
            positionX += velocityX;
    }

    // 공격 범위내에 있으면 true를 return(기사 직업 전용)
    public boolean withinAttackDistance(Camera camera, Enemy enemy) {
        double startX = camera.gameToScreenCoordinateX(spellcaster.getPositionX());
        double endXRight = startX + SWORDEFFECT_SPRITE_WIDTH + 85;
        double endXLeft = startX - SWORDEFFECT_SPRITE_WIDTH - 85;
        double startY = camera.gameToScreenCoordinateY(spellcaster.getPositionY()) - (SWORDEFFECT_SPRITE_HEIGHT / 2) - 45;
        double endY = camera.gameToScreenCoordinateY(spellcaster.getPositionY()) + (SWORDEFFECT_SPRITE_HEIGHT / 2) + 45;
        //플레이어 방향이 오른쪽일때
        if (attackDirection == 0) {
            if ((camera.gameToScreenCoordinateX(enemy.getPositionX()) >= startX && camera.gameToScreenCoordinateX(enemy.getPositionX()) <= endXRight)
                    && (camera.gameToScreenCoordinateY(enemy.getPositionY()) >= startY && camera.gameToScreenCoordinateY(enemy.getPositionY()) <= endY)) {
                return true;
            } else
                return false;
        }
        //플레이어 방향이 왼쪽일때
        else if (attackDirection == 1) {
            if ((camera.gameToScreenCoordinateX(enemy.getPositionX()) <= startX && camera.gameToScreenCoordinateX(enemy.getPositionX()) >= endXLeft)
                    && (camera.gameToScreenCoordinateY(enemy.getPositionY()) >= startY && camera.gameToScreenCoordinateY(enemy.getPositionY()) <= endY)) {
                return true;
            } else
                return false;
        }
        return false;
    }

    public void startAnimationState() {
        animationState = true;
        damageState = true;

    }

    public boolean getAnimationState() {
        return animationState;
    }

    public boolean getDamageState(){
        return damageState;
    }
}
