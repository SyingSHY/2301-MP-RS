package com.example.roguelikesurvival.object.item;

import static com.example.roguelikesurvival.object.GameObject.getDistanceBetweenObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.roguelikesurvival.Camera;
import com.example.roguelikesurvival.GameLoop;
import com.example.roguelikesurvival.R;
import com.example.roguelikesurvival.object.Circle;
import com.example.roguelikesurvival.object.Enemy;
import com.example.roguelikesurvival.object.GameObject;
import com.example.roguelikesurvival.object.Player;

public class RotateAttack {
    private static final float PLAYER_SPRITE_WIDTH = 95;
    private static final float PLAYER_SPRITE_HEIGHT = 167;
    private static final int WITHINATTACKFROMPLAYER = 120;
    private double attackPerMinute = 12;
    private double attackPerSecond = attackPerMinute / 60.0;
    private double updatePerAttack = GameLoop.MAX_UPS / attackPerSecond;
    private double updateUntilNextAttack = updatePerAttack;
    private Player player;
    private Bitmap rotateAttackBitmap = null;
    private Bitmap[] rotateAttack = new Bitmap[4];
    private int updateBeforeNextAttack = 3;
    private int moveIdx = 0;
    private boolean animationState = false;
    private boolean damageState = false;
    private boolean playerGetItem = false;
    private int playerHaveThisItem = 0;

    public RotateAttack(Context context, Player player, Bitmap bitmap) {

        this.player = player;
        rotateAttackBitmap = bitmap;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        rotateAttack[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rotate_attack_1, bitmapOptions);
        rotateAttack[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rotate_attack_02, bitmapOptions);
        rotateAttack[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rotate_attack_03, bitmapOptions);
        rotateAttack[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.rotate_attack_04, bitmapOptions);
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
        //아이템을 선택했을 때부터 활성화
        if(playerGetItem) {
            updateBeforeNextAttack--;
            damageState = false;
            if (updateBeforeNextAttack == 0) {
                updateBeforeNextAttack = 3;
                if (moveIdx == 0) {
                    moveIdx++;
                } else if (moveIdx == 1)
                    moveIdx++;
                else if (moveIdx == 2)
                    moveIdx++;
                else {
                    moveIdx = 0;
                    animationState = false;
                }
            }

            if (animationState) {
                canvas.drawBitmap(rotateAttack[moveIdx],
                        (float) camera.gameToScreenCoordinateX(player.getPositionX()) - PLAYER_SPRITE_WIDTH - 30,
                        (float) camera.gameToScreenCoordinateY(player.getPositionY()) - PLAYER_SPRITE_HEIGHT + 65, null);
            }
        }
    }

    // 공격 범위내에 있으면 true를 return
    public boolean withinAttackDistance(Camera camera, Enemy enemy) {

        double distance = getDistanceBetweenObject(player, enemy);
        double distanceToCollision = WITHINATTACKFROMPLAYER + enemy.getRadius();
        if (distance < distanceToCollision)
            return true;
        else
            return false;
    }

    public void isSelect() {
        playerGetItem = true;
        playerHaveThisItem++;
    }

    public boolean getIsSelect(){
        return playerGetItem;
    }

    public boolean isPlayerGetItem() {
        return playerGetItem;
    }

    public Bitmap getBitmap() {
        return rotateAttackBitmap;
    }

    public void startAnimationState() {
        animationState = true;
        damageState = true;
    }

    public boolean getAnimationState() {
        return animationState;
    }

    public boolean getDamageState() {
        return damageState;
    }

    public void setDamageState(boolean state) {
        damageState = state;
    }
}

