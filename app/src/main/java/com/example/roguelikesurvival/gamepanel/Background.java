package com.example.roguelikesurvival.gamepanel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.roguelikesurvival.Camera;
import com.example.roguelikesurvival.R;
import com.example.roguelikesurvival.object.Player;

public class Background {
    public static final int WIDTH = 96;
    public static final int HEIGHT = 96;
    public static final int ROW_TILES = 40;
    public static final int COLUMN_TILES = 40;
    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;

    private Bitmap[] bitmap = new Bitmap[8];
    private int[][] background;
    private Player player;


    private int startX, startY;

    public Background(Context context, Player player, int startX, int startY) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        bitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.floor_1, bitmapOptions);
        bitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.floor_2, bitmapOptions);
        bitmap[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.floor_3, bitmapOptions);
        bitmap[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.floor_4, bitmapOptions);
        bitmap[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.floor_5, bitmapOptions);
        bitmap[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.floor_6, bitmapOptions);
        bitmap[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.floor_7, bitmapOptions);
        bitmap[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.floor_8, bitmapOptions);

        this.startX = startX;
        this.startY = startY;

        this.player = player;


        initializeBackground();
    }

    private void initializeBackground() {
        int randomNum;
        background = new int[ROW_TILES][COLUMN_TILES];

        for (int i = 0; i < ROW_TILES; i++) {
            for (int j = 0; j < COLUMN_TILES; j++) {
                randomNum = (int) (Math.random() * 8);
                background[i][j] = randomNum;
            }
        }


    }

    public void draw(Canvas canvas, Camera camera){
        int row= 0, col = 0, x = 0, y = 0;

        while(row < ROW_TILES && col < COLUMN_TILES){
            int tileNum = background[row][col];

            canvas.drawBitmap(bitmap[tileNum], (float)camera.gameToScreenCoordinateX(startX+x), (float)camera.gameToScreenCoordinateY(startY+y), null);
            row++;
            x+=WIDTH;

            if(row == ROW_TILES){
                row = 0;
                x = 0;
                col++;
                y+= HEIGHT;
            }
        }
    }

    public void update(Camera camera){
        if(player.getDirectionX() > 0 &&
                camera.gameToScreenCoordinateX(player.getPositionX()) > camera.gameToScreenCoordinateX(startX +  WIDTH*ROW_TILES + SCREEN_WIDTH/2)){
            startX += 2*WIDTH*ROW_TILES;
        }
        else if(player.getDirectionX() < 0 &&
                camera.gameToScreenCoordinateX(player.getPositionX()) < camera.gameToScreenCoordinateX(startX - SCREEN_WIDTH/2)){
            startX -= 2*WIDTH*ROW_TILES;
        }
        if(player.getDirectionY() > 0 &&
                camera.gameToScreenCoordinateY(player.getPositionY()) > camera.gameToScreenCoordinateY(startY + HEIGHT*COLUMN_TILES + SCREEN_HEIGHT/2)){
            startY += 2*HEIGHT*COLUMN_TILES;
        }
        else if(player.getDirectionY() < 0 &&
                camera.gameToScreenCoordinateY(player.getPositionY()) < camera.gameToScreenCoordinateY(startY - SCREEN_HEIGHT)){
            startY -= 2*HEIGHT*COLUMN_TILES;
        }
    }
}
