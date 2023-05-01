package com.example.roguelikesurvival.object;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.R;

public class Goblin extends Enemy{

    private final Player player;

    public Goblin(Context context, Player player, double positionX, double positionY, double radius) {
        super(context, player, positionX, positionY, radius);
        this.player = player;
    }
}
