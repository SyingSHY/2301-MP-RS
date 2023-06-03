package com.example.roguelikesurvival.object.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.roguelikesurvival.R;
import com.example.roguelikesurvival.object.Player;

public class PlusAtk {
    private Player player;
    private Bitmap plusAtkBitmap = null;
    private int playerHaveThisItem;


    public PlusAtk(Context context, Player player, Bitmap bitmap){
        this.player = player;

        playerHaveThisItem = 0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        plusAtkBitmap = bitmap;

    }

    public void isSelect(){
        player.plusAttackPower(2);
        playerHaveThisItem++;
    }

    public Bitmap getBitmap(){
        return plusAtkBitmap;
    }
}
