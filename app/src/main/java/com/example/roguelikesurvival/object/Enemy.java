package com.example.roguelikesurvival.object;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.Camera;
import com.example.roguelikesurvival.Game;
import com.example.roguelikesurvival.GameLoop;
import com.example.roguelikesurvival.R;
import com.example.roguelikesurvival.object.Circle;
import com.example.roguelikesurvival.object.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Enemy extends Circle {

    private boolean isFrozen = false;
    

    public Enemy(Context context, Player player, Camera camera, double spawnPositionX, double spawnPositionY, int radius) {
        super(context, ContextCompat.getColor(context, R.color.enemy),
                player.getPositionX() + spawnPositionX,
                player.getPositionY() + spawnPositionY, radius);
    }






    //설정한 시간간격마다 true를 return하여 스폰준비
    public abstract boolean readyToSpawn();
    public abstract boolean getHitImage();
    public  abstract void setHitImage(boolean state);
    public abstract int getHealthPoint();
    public abstract void setHealthPoint(int healthPoint);
    public void freeze(){
        this.isFrozen=true;
    }
    public void unfreeze(){
        this.isFrozen=false;
    }

    public boolean isFrozen() {
        return this.isFrozen;
    }
}
