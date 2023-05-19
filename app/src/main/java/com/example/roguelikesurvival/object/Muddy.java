package com.example.roguelikesurvival.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.roguelikesurvival.Camera;
import com.example.roguelikesurvival.Game;
import com.example.roguelikesurvival.GameLoop;
import com.example.roguelikesurvival.R;

import java.util.List;

public class Muddy extends Enemy {
    private static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND * 0.4;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static final double AVOID_POWER = 5;

    //SPAWM_PER_MINUTE 분당 스폰할 적 수 설정
    private static final double SPAWN_PER_MINUTE = 30;
    private static final double SPAWN_PER_SECOND = SPAWN_PER_MINUTE / 60.0;
    private static final double UPDATE_PER_SPAWN = GameLoop.MAX_UPS / SPAWN_PER_SECOND;
    private static double updateUntilNextSpawn = UPDATE_PER_SPAWN;
    private static final float SPRITE_WIDTH = 96;
    private static final float SPRITE_HEIGHT = 96;
    private int healthPoint = 3;
    private boolean hitImage = false;
    private final Player player;

    private Bitmap[] bitmap = new Bitmap[5];
    private Bitmap[] bitmapL = new Bitmap[5];

    // 이미지 애니메이션 속도 설정
    private int updateBeforeNextMove = 5;
    private int moveIdx = 0;

    public Muddy(Context context, Player player, Camera camera, double spawnPositionX, double spawnPositionY) {
        super(context, player, camera, spawnPositionX, spawnPositionY);
        this.player = player;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        bitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.muddy_run_anim_f0, bitmapOptions);
        bitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.muddy_run_anim_f1, bitmapOptions);
        bitmap[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.muddy_run_anim_f2, bitmapOptions);
        bitmap[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.muddy_run_anim_f3, bitmapOptions);
        bitmap[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.muddy_hit_anim, bitmapOptions);

        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        //반대쪽 이동 애니메이션을 구현하기 위해 비트맵이미지 좌우반전
        bitmapL[0] = Bitmap.createBitmap(bitmap[0], 0, 0, (int) SPRITE_WIDTH, (int) SPRITE_HEIGHT, matrix, false);
        bitmapL[1] = Bitmap.createBitmap(bitmap[1], 0, 0, (int) SPRITE_WIDTH, (int) SPRITE_HEIGHT, matrix, false);
        bitmapL[2] = Bitmap.createBitmap(bitmap[2], 0, 0, (int) SPRITE_WIDTH, (int) SPRITE_HEIGHT, matrix, false);
        bitmapL[3] = Bitmap.createBitmap(bitmap[3], 0, 0, (int) SPRITE_WIDTH, (int) SPRITE_HEIGHT, matrix, false);
        bitmapL[4] = Bitmap.createBitmap(bitmap[4], 0, 0, (int) SPRITE_WIDTH, (int) SPRITE_HEIGHT, matrix, false);
    }

    //설정한 시간간격마다 true를 return하여 스폰준비
    public boolean readyToSpawn() {
        if (updateUntilNextSpawn <= 0) {
            updateUntilNextSpawn += UPDATE_PER_SPAWN;
            return true;
        } else {
            updateUntilNextSpawn--;
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
            else
                moveIdx = 0;
        }

        // 적의 방향을 체크하여 이미지 방향 결정
        if (velocityX > 0)
            canvas.drawBitmap(bitmap[moveIdx], (float) camera.gameToScreenCoordinateX(positionX) - (SPRITE_WIDTH / 2),
                    (float) camera.gameToScreenCoordinateY(positionY) - (SPRITE_HEIGHT / 2), null);
        else
            canvas.drawBitmap(bitmapL[moveIdx], (float) camera.gameToScreenCoordinateX(positionX) - (SPRITE_WIDTH / 2),
                    (float) camera.gameToScreenCoordinateY(positionY) - (SPRITE_HEIGHT / 2), null);

        //데미지 받을때 모션
        if (hitImage) {
            if (velocityX > 0)
                canvas.drawBitmap(bitmap[4], (float) camera.gameToScreenCoordinateX(positionX) - (SPRITE_WIDTH / 2),
                        (float) camera.gameToScreenCoordinateY(positionY) - (SPRITE_HEIGHT / 2), null);
            else
                canvas.drawBitmap(bitmapL[4], (float) camera.gameToScreenCoordinateX(positionX) - (SPRITE_WIDTH / 2),
                        (float) camera.gameToScreenCoordinateY(positionY) - (SPRITE_HEIGHT / 2), null);
            hitImage = false;
        }
    }

    @Override
    public void update() {
        //각 enemy 객체를 돌면서 거리를 구하고 거리에 반비례 하는 Avoidance Vector 계산
        //this.game = new Game(super.context);
        List<Enemy> enemyList = Game.enemyList;

        double avoidanceX = 0;
        double avoidanceY = 0;

        for (Enemy enemy : enemyList) {
            double avoidanceDist = GameObject.getDistanceBetweenObject(this, enemy);

            if (avoidanceDist != 0f) {
                avoidanceX -= (1 / (enemy.getPositionX() - positionX));
                avoidanceY -= (1 / (enemy.getPositionY() - positionY));
            }
        }


        //플레이어와 적사이의 거리 구하기
        double distanceToPlayerX = player.getPositionX() - positionX;
        double distanceToPlayerY = player.getPositionY() - positionY;
        double distanceToPlayer = GameObject.getDistanceBetweenObject(this, player);

        //단위백터를 활용하여 플레이어쪽 방향 구하기
        double directionX = distanceToPlayerX / distanceToPlayer;
        double directionY = distanceToPlayerY / distanceToPlayer;

        //플레이어쪽으로 적 이동시키기
        if (distanceToPlayer > 0 && isFrozen()==false) {
            velocityX = (directionX + avoidanceX * AVOID_POWER) * MAX_SPEED;
            velocityY = (directionY + avoidanceY * AVOID_POWER) * MAX_SPEED;
        } else {
            velocityX = 0;
            velocityY = 0;
        }
        positionX += velocityX;
        positionY += velocityY;
    }

    public boolean getHitImage() {
        return hitImage;
    }

    public void setHitImage(boolean state) {
        hitImage = state;
    }

    public int getHealthPoint() {
        return healthPoint;
    }

    public void setHealthPoint(int healthPoint) {
        if (healthPoint >= 0)
            this.healthPoint = healthPoint;
    }
}