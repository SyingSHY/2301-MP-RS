package com.example.roguelikesurvival.object;

import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.Camera;
import com.example.roguelikesurvival.Game;
import com.example.roguelikesurvival.GameLoop;
import com.example.roguelikesurvival.R;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends Circle {
    private static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND * 0.4;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static final double AVOID_POWER = 5;

    //SPAWM_PER_MINUTE 분당 스폰할 적 수 설정
    private static final double SPAWN_PER_MINUTE = 20;
    private static final double SPAWN_PER_SECOND = SPAWN_PER_MINUTE / 60.0;
    private static final double UPDATE_PER_SPAWN = GameLoop.MAX_UPS / SPAWN_PER_SECOND;
    private static double updateUntilNextSpawn = UPDATE_PER_SPAWN;
    private final Player player;
    private Game game;

    public Enemy(Context context, Player player, double positionX, double positionY, double radius) {
        super(context, ContextCompat.getColor(context, R.color.enemy), positionX, positionY, radius);
        this.player = player;
    }

    public Enemy(Context context, Player player, Camera camera) {
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
        //각 enemy 객체를 돌면서 거리를 구하고 거리에 반비례 하는 Avoidance Vector 계산
        //this.game = new Game(super.context);
        List<Enemy> enemyList = Game.enemyList;

        double avoidanceX = 0;
        double avoidanceY = 0;

        for (Enemy enemy : enemyList) {
            double avoidanceDist = GameObject.getDistanceBetweenObject(this, enemy);

            if (avoidanceDist != 0f) {
                avoidanceX -= (1 / (enemy.positionX - positionX));
                avoidanceY -= (1 / (enemy.positionY - positionY));
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
        if (distanceToPlayer > 0) {
            velocityX = (directionX + avoidanceX * AVOID_POWER) * MAX_SPEED;
            velocityY = (directionY + avoidanceY * AVOID_POWER) * MAX_SPEED;
        } else {
            velocityX = 0;
            velocityY = 0;
        }
        positionX += velocityX;
        positionY += velocityY;
    }
}
