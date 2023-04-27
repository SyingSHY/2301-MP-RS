package com.example.roguelikesurvival;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Background {
    public static final int WIDTH = 96;
    public static final int HEIGHT = 96;
    public static final int ROW_TILES = 40;
    public static final int COLUMN_TILES = 40;
    private Bitmap[] bitmap = new Bitmap[8];
    private Bitmap backgroundBitmap;

    private int[][] background;

    public enum Floor {
        FLOOR1,
        FLOOR2,
        FLOOR3,
        FLOOR4,
        FLOOR5,
        FLOOR6,
        FLOOR7,
        FLOOR8
    }

    public Background(Context context) {
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

    public int[][] getBackground() {
        return background;
    }

    public void draw(Canvas canvas, Camera camera){
        int row= 0, col = 0, x = 0, y = 0;

        while(row < ROW_TILES && col < COLUMN_TILES){
            int tileNum = background[row][col];

            canvas.drawBitmap(bitmap[tileNum], (float)camera.gameToScreenCoordinateX(x+500), (float)camera.gameToScreenCoordinateY(y+500), null);
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
}
