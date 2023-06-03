package com.example.roguelikesurvival.object.item;

import com.example.roguelikesurvival.object.Player;

public class PlusHp {
    private Player player;
    private int playerHaveThisItem;


    public PlusHp(Player player){
        this.player = player;

        playerHaveThisItem = 0;

    }

    public void isSelect(){
        player.plusMaxHealthPoint(2);
        playerHaveThisItem++;
    }
}
