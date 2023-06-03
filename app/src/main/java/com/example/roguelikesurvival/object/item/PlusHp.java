package com.example.roguelikesurvival.object.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.roguelikesurvival.R;
import com.example.roguelikesurvival.object.Player;

public class PlusHp {
    private Player player;
    private Bitmap plusHpBitmap = null;
    private int playerHaveThisItem;


    public PlusHp(Context context, Player player, Bitmap bitmap){
        this.player = player;

        playerHaveThisItem = 0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        plusHpBitmap = bitmap;

    }

    public void isSelect(){
        player.plusMaxHealthPoint(2);
        playerHaveThisItem++;
    }

    public Bitmap getBitmap(){
        return plusHpBitmap;
    }
}
