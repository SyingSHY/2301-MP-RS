package com.example.roguelikesurvival.gamepanel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.roguelikesurvival.Game;
import com.example.roguelikesurvival.R;
import com.example.roguelikesurvival.Utils;

public class GamePauseButton {
    private static final float BORDER_SPRITE_WIDTH = 96;
    private static final float BORDER_SPRITE_HEIGHT = 96;
    private int posX;
    private int posY;
    private Paint paint;
    private boolean isPressed;
    private Bitmap[] iconBitmap = new Bitmap[1];

    private Game game;

    public GamePauseButton(Context context, int posX, int posY, Game game) {
        this.posX = posX;
        this.posY = posY;
        this.game = game;

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        iconBitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.gui_gamepause_button, bitmapOptions);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(iconBitmap[0], posX, posY, null);
    }

    public boolean isPressed(double touchPosX, double touchPosY) {
        double distance = Utils.getDistanceBetweenPoints(posX, posY, touchPosX, touchPosY);
        return ((touchPosX > posX && touchPosX < (posX + BORDER_SPRITE_WIDTH))
                && (touchPosY > posY && touchPosY < (posY + BORDER_SPRITE_HEIGHT)));
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
        if (isPressed) {
            game.onGamePauseButtonPressed();
        }
    }

    public boolean getIsPressed() {
        return isPressed;
    }
}
