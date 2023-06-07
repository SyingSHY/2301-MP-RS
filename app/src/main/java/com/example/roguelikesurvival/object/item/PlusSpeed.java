package com.example.roguelikesurvival.object.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.roguelikesurvival.object.Player;

public class PlusSpeed {
    private Player player;
    private Bitmap plusSpeedBitmap = null;
    private int playerHaveThisItem;


    public PlusSpeed(Context context, Player player, Bitmap bitmap){
        this.player = player;

        playerHaveThisItem = 0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        plusSpeedBitmap = bitmap;

    }

    public void isSelect(){
        player.plusSpeed(20);
        playerHaveThisItem++;
    }

    public Bitmap getBitmap(){
        return plusSpeedBitmap;
    }
}

