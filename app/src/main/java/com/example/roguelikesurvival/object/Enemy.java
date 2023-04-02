package com.example.roguelikesurvival.object;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.GameLoop;
import com.example.roguelikesurvival.R;

public class Enemy extends Circle {
    private static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND * 0.4;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;

    //SPAWM_PER_MINUTE 분당 스폰할 적 수 설정
    private static final double SPAWN_PER_MINUTE = 20;
    private static final double SPAWN_PER_SECOND = SPAWN_PER_MINUTE / 60.0;
    private static final double UPDATE_PER_SPAWN = GameLoop.MAX_UPS / SPAWN_PER_SECOND;
    private static double updateUntilNextSpawn = UPDATE_PER_SPAWN;
    private final Player player;

    public Enemy(Context context, Player player, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.enemy), positionX, positionY, radius);
        this.player = player;
    }

    public Enemy(Context context, Player player) {
        super(context, ContextCompat.getColor(context, R.color.enemy), Math.random() * 1000, Math.random() * 1000, 30);
        this.player = player;
    }

    //설정한 시간간격마다 true를 return하여 스폰준비
    public static boolean readyToSpawn() {
        if (updateUntilNextSpawn <= 0) {
            updateUntilNextSpawn += UPDATE_PER_SPAWN;
            return true;
        } else {
            updateUntilNextSpawn--;
            return false;
        }
    }

    @Override
    public void update() {
        //플레이어와 적사이의 거리 구하기
        double distanceToPlayerX = player.getPositionX() - positionX;
        double distanceToPlayerY = player.getPositionY() - positionY;
        double distanceToPlayer = GameObject.getDistanceBetweenObject(this, player);

        //단위백터를 활용하여 플레이어쪽 방향 구하기
        double directionX = distanceToPlayerX / distanceToPlayer;
        double directionY = distanceToPlayerY / distanceToPlayer;

        //플레이어쪽으로 적 이동시키기
        if (distanceToPlayer > 0) {
            velocityX = directionX * MAX_SPEED;
            velocityY = directionY * MAX_SPEED;
        } else {
            velocityX = 0;
            velocityY = 0;
        }
        positionX += velocityX;
        positionY += velocityY;
    }
}
