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

public abstract class Enemy extends Circle {


    public Enemy(Context context, Player player, Camera camera) {

        super(context, ContextCompat.getColor(context, R.color.enemy),player.getPositionX() + Math.cos(Math.random()*2*Math.PI)*((Math.random()*500)+2000),
                player.getPositionY() + Math.cos(Math.random()*2*Math.PI)*((Math.random()*500)+2000), 30);

    }

    //설정한 시간간격마다 true를 return하여 스폰준비
    public abstract boolean readyToSpawn();
}
