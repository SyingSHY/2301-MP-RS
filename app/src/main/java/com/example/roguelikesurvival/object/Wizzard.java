package com.example.roguelikesurvival.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;

import com.example.roguelikesurvival.Camera;
import com.example.roguelikesurvival.GameLoop;
import com.example.roguelikesurvival.R;
import com.example.roguelikesurvival.Utils;
import com.example.roguelikesurvival.gamepanel.HealthBar;
import com.example.roguelikesurvival.gamepanel.Joystick;

import java.util.HashMap;
import java.util.Map;

public class Wizzard extends Player {
    public static final double SPEED_PIXELS_PER_SECOND = 400.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static final float SPRITE_WIDTH = 95;
    private static final float SPRITE_HEIGHT = 167;
    private final Joystick joystick;
    private int maxHealthPoint = 10;
    private HealthBar healthBar;
    private int healthPoint;
    private Bitmap[] bitmap = new Bitmap[5];
    private Bitmap[] bitmapL = new Bitmap[5];
    private MoveState moveState = MoveState.NOT_MOVING;
    private int updateBeforeNextMove = 5;
    private int moveIdx = 1;

    private long lastSkillUseTime;
    private long skillCooldown = 30000;  // 30 seconds in milliseconds

    public Wizzard(Context context, Joystick joystick, double positionX, double positionY,
                   double radius) {
        super(context, joystick, positionX, positionY, radius);

        this.joystick = joystick;
        this.healthBar = new HealthBar(context, this);
        this.healthPoint = maxHealthPoint;

        //비트맵으로 이미지설정
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        bitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wizzard_run_anim_f0, bitmapOptions);
        bitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wizzard_run_anim_f1, bitmapOptions);
        bitmap[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wizzard_run_anim_f2, bitmapOptions);
        bitmap[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wizzard_run_anim_f3, bitmapOptions);
        bitmap[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.wizzard_hit_anim, bitmapOptions);

        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        //반대쪽 이동 애니메이션을 구현하기 위해 비트맵이미지 좌우반전
        bitmapL[0] = Bitmap.createBitmap(bitmap[0], 0, 0, (int) SPRITE_WIDTH, (int) SPRITE_HEIGHT, matrix, false);
        bitmapL[1] = Bitmap.createBitmap(bitmap[1], 0, 0, (int) SPRITE_WIDTH, (int) SPRITE_HEIGHT, matrix, false);
        bitmapL[2] = Bitmap.createBitmap(bitmap[2], 0, 0, (int) SPRITE_WIDTH, (int) SPRITE_HEIGHT, matrix, false);
        bitmapL[3] = Bitmap.createBitmap(bitmap[3], 0, 0, (int) SPRITE_WIDTH, (int) SPRITE_HEIGHT, matrix, false);
        bitmapL[4] = Bitmap.createBitmap(bitmap[4], 0, 0, (int) SPRITE_WIDTH, (int) SPRITE_HEIGHT, matrix, false);
    }

    public void update() {
        //조이스틱의 방향으로 player 이동
        velocityX = joystick.getActuatorX() * MAX_SPEED;
        velocityY = joystick.getActuatorY() * MAX_SPEED;
        positionX += velocityX;
        positionY += velocityY;

        if (velocityX != 0 || velocityY != 0) {
            double distance = Utils.getDistanceBetweenPoints(0, 0, velocityX, velocityY);
            directionX = velocityX / distance;
            directionY = velocityY / distance;
        }

        //플레이어 애니메이션 구현을 위해 움직임 상태 파악
        switch (moveState) {
            case NOT_MOVING:
                if (velocityX != 0 || velocityY != 0)
                    moveState = MoveState.STARTED_MOVING;
                break;
            case STARTED_MOVING:
                if (velocityX != 0 || velocityY != 0)
                    moveState = MoveState.IS_MOVING;
                break;
            case IS_MOVING:
                if (velocityX == 0 && velocityY == 0)
                    moveState = MoveState.NOT_MOVING;
                break;
            default:
                break;
        }
    }

    public void draw(Canvas canvas, Camera camera) {

        // moveState에 따라 플레이어 애니메이션 구현
        switch (moveState) {
            case NOT_MOVING:
                if (directionX > 0)
                    canvas.drawBitmap(bitmap[0], (float) camera.gameToScreenCoordinateX(positionX) - (SPRITE_WIDTH / 2),
                            (float) camera.gameToScreenCoordinateY(positionY) - (SPRITE_HEIGHT / 2), null);
                else
                    canvas.drawBitmap(bitmapL[0], (float) camera.gameToScreenCoordinateX(positionX) - (SPRITE_WIDTH / 2),
                            (float) camera.gameToScreenCoordinateY(positionY) - (SPRITE_HEIGHT / 2), null);
                break;
            case STARTED_MOVING:
                moveIdx = 1;
                updateBeforeNextMove = 5;
                if (directionX > 0)
                    canvas.drawBitmap(bitmap[moveIdx], (float) camera.gameToScreenCoordinateX(positionX) - (SPRITE_WIDTH / 2),
                            (float) camera.gameToScreenCoordinateY(positionY) - (SPRITE_HEIGHT / 2), null);
                else
                    canvas.drawBitmap(bitmapL[moveIdx], (float) camera.gameToScreenCoordinateX(positionX) - (SPRITE_WIDTH / 2),
                            (float) camera.gameToScreenCoordinateY(positionY) - (SPRITE_HEIGHT / 2), null);
                break;
            case IS_MOVING:
                updateBeforeNextMove--;
                if (updateBeforeNextMove == 0) {
                    updateBeforeNextMove = 5;
                    if (moveIdx == 1)
                        moveIdx++;
                    else if (moveIdx == 2)
                        moveIdx++;
                    else
                        moveIdx = 1;
                }
                if (directionX > 0)
                    canvas.drawBitmap(bitmap[moveIdx], (float) camera.gameToScreenCoordinateX(positionX) - (SPRITE_WIDTH / 2),
                            (float) camera.gameToScreenCoordinateY(positionY) - (SPRITE_HEIGHT / 2), null);
                else
                    canvas.drawBitmap(bitmapL[moveIdx], (float) camera.gameToScreenCoordinateX(positionX) - (SPRITE_WIDTH / 2),
                            (float) camera.gameToScreenCoordinateY(positionY) - (SPRITE_HEIGHT / 2), null);
                break;
            default:
                break;
        }

        healthBar.draw(canvas, camera);
    }

    public int getMaxHealthPoint() {
        return maxHealthPoint;
    }

    public int getHealthPoint() {
        return healthPoint;
    }

    public void setHealthPoint(int healthPoint) {
        if (healthPoint >= 0)
            this.healthPoint = healthPoint;
    }

    @Override
    public void useSkill() {

    }
}
