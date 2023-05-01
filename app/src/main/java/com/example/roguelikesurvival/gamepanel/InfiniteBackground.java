package com.example.roguelikesurvival.gamepanel;

import android.content.Context;
import android.graphics.Canvas;

import com.example.roguelikesurvival.Camera;
import com.example.roguelikesurvival.object.Player;

public class InfiniteBackground {

    public static final int WIDTH = 96;
    public static final int HEIGHT = 96;
    public static final int ROW_TILES = 40;
    public static final int COLUMN_TILES = 40;
    private Background backgroundPart1;
    private Background backgroundPart2;
    private Background backgroundPart3;
    private Background backgroundPart4;
    private Player player;



    public InfiniteBackground(Context context, Player player){
        backgroundPart1 = new Background(context, player, 500, 500);
        backgroundPart2 = new Background(context, player,500, 500 - HEIGHT*COLUMN_TILES);
        backgroundPart3 = new Background(context, player,500 - WIDTH*ROW_TILES, 500);
        backgroundPart4 = new Background(context, player,500 - WIDTH*ROW_TILES, 500 - HEIGHT*COLUMN_TILES);

        this.player = player;

    }

    public void draw(Canvas canvas, Camera camera){
        backgroundPart1.draw(canvas,camera);
        backgroundPart2.draw(canvas, camera);
        backgroundPart3.draw(canvas, camera);
        backgroundPart4.draw(canvas, camera);
    }

    public void update(Camera camera){
        backgroundPart1.update(camera);
        backgroundPart2.update(camera);
        backgroundPart3.update(camera);
        backgroundPart4.update(camera);
    }
}
