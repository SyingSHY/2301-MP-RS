package com.example.roguelikesurvival.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.R;
import com.example.roguelikesurvival.object.Player;

public class PlayerState {
    private Context context;
    private Player player;

    public PlayerState(Context context, Player player) {
        this.context = context;
        this.player = player;
    }

    public void draw(Canvas canvas){
        int color = ContextCompat.getColor(context, R.color.white);
        Paint state = new Paint();
        state.setColor(color);
        state.setTextSize(50);

        canvas.drawText("체력: " + player.getHealthPoint() + "/" + player.getMaxHealthPoint(), 10, 140, state);
        canvas.drawText("공격력: " + player.getAttackPower(), 10, 190, state);
    }
}
