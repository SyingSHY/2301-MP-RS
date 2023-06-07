package com.example.roguelikesurvival.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.R;
import com.example.roguelikesurvival.SelectItem;
import com.example.roguelikesurvival.object.Player;

public class PlayerState {
    private Context context;
    private Player player;
    private SelectItem selectItem;
    private boolean showState = false;

    public PlayerState(Context context, Player player, SelectItem selectItem) {
        this.context = context;
        this.player = player;
        this.selectItem = selectItem;
    }

    public void draw(Canvas canvas) {
        int color = ContextCompat.getColor(context, R.color.white);
        Paint state = new Paint();
        state.setColor(color);
        state.setTextSize(50);

        if (showState) {
            canvas.drawText("체력: " + player.getHealthPoint() + "/" + player.getMaxHealthPoint(), 10, 140, state);
            canvas.drawText("공격력: " + player.getAttackPower(), 10, 190, state);
            canvas.drawText("이동속도: " + (int) player.getSpeed(), 10, 240, state);
            if (selectItem.getRotateAttack().getIsSelect())
                canvas.drawText("아이템 공격력: " + selectItem.getItemAttackPower(), 10, 290, state);
        }
    }

    public boolean isShowState(double touchPosX, double touchPosY) {
        if((touchPosX > 10 && touchPosX < 180)
                && (touchPosY > 40 && touchPosY < 100)){
            return true;
        }
        else return  false;
    }

    public void setShowState(){
        showState = !showState;
    }
}
