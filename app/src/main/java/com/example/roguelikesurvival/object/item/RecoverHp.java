package com.example.roguelikesurvival.object.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.roguelikesurvival.object.Player;

public class RecoverHp {
    private Player player;
    private Bitmap RecoverHpBitmap = null;
    private int playerHaveThisItem;


    public RecoverHp(Context context, Player player, Bitmap bitmap){
        this.player = player;

        playerHaveThisItem = 0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        RecoverHpBitmap = bitmap;

    }

    public void isSelect(){
        player.setHealthPoint(player.getMaxHealthPoint());
        playerHaveThisItem++;
    }

    public Bitmap getBitmap(){
        return RecoverHpBitmap;
    }
}

