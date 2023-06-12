package com.example.roguelikesurvival;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.gamepanel.ReStart;
import com.example.roguelikesurvival.object.Player;
import com.example.roguelikesurvival.object.item.PlusAtk;
import com.example.roguelikesurvival.object.item.PlusHp;
import com.example.roguelikesurvival.object.item.RotateAttack;

import java.util.Random;

public class PauseMenu {

    private static final float BACGROUND_SPRITE_WIDTH = 748;
    private static final float BACKGROUND_SPRITE_HEIGHT = 688;
    private static final float BORDER_SPRITE_WIDTH = 574;
    private static final float BORDER_SPRITE_HEIGHT = 169;
    private static final int NUMBER_OF_OPTIONS = 2;
    private float selectPosX;
    private float firstSelectPosY;
    private float secondSelectPosY;
    private Context context;
    private Player player;
    private Bitmap backgroudBitmap = null;
    private Bitmap[] optionBitmap = new Bitmap[NUMBER_OF_OPTIONS];
    boolean gamePauseMenu = false;
    private int color;
    private int colorW;
    private Paint pauseMenuTitle;
    private Paint name;
    private Paint nameW;
    private Game game;


    public PauseMenu(Context context, Player player, Camera camera) {

        this.context = context;
        this.player = player;
        color = ContextCompat.getColor(context, R.color.black);
        colorW = ContextCompat.getColor(context, R.color.white);

        pauseMenuTitle = new Paint();
        pauseMenuTitle.setColor(color);
        pauseMenuTitle.setTextSize(60);
        pauseMenuTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        name = new Paint();
        name.setColor(color);
        name.setTextSize(50);
        name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        nameW = new Paint();
        nameW.setColor(colorW);
        nameW.setTextSize(50);
        nameW.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        backgroudBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.temporary_pause_background, bitmapOptions);
        optionBitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.gui_resume_select, bitmapOptions);
        optionBitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.gui_game_giveup_select, bitmapOptions);
    }

    public void draw(Canvas canvas, Camera camera) {
        // 일시정지 메뉴 위치 설정
        selectPosX = (float) camera.gameToScreenCoordinateX(player.getPositionX()) - (BORDER_SPRITE_WIDTH / 2) - 3;
        firstSelectPosY = (float) camera.gameToScreenCoordinateY(player.getPositionY()) - (BORDER_SPRITE_HEIGHT / 2) - 20;
        secondSelectPosY = (float) camera.gameToScreenCoordinateY(player.getPositionY()) - (BORDER_SPRITE_HEIGHT / 2) + 170;

        // 일시정지 메뉴 배경 draw
        canvas.drawBitmap(backgroudBitmap, (float) camera.gameToScreenCoordinateX(player.getPositionX()) - (BACGROUND_SPRITE_WIDTH / 2),
                (float) camera.gameToScreenCoordinateY(player.getPositionY()) - (BACKGROUND_SPRITE_HEIGHT / 2), null);

        // 일시정지 메뉴 버튼 Draw
        canvas.drawBitmap(optionBitmap[0], selectPosX, firstSelectPosY, null);
        canvas.drawBitmap(optionBitmap[1], selectPosX, secondSelectPosY, null);

        // 일시정지 메뉴 텍스트 Draw
        canvas.drawText("게임 일시정지됨", selectPosX + 90, firstSelectPosY - 80, pauseMenuTitle);
        canvas.drawText("게임 재개", selectPosX + 160, firstSelectPosY + 100, name);
        canvas.drawText("게임 포기", selectPosX + 160, secondSelectPosY + 100, nameW);
    }

    public void update(){

    }

    public boolean isGamePauseMenu(){
        return gamePauseMenu;
    }

    public void setGamePauseMenu(boolean bool){
        gamePauseMenu = bool;
    }

    public void gamePauseMenu(){
        gamePauseMenu = true;
    }

    public boolean isFirstSelectPressed(double touchPosX, double touchPosY) {
        if((touchPosX > selectPosX && touchPosX < (selectPosX + BORDER_SPRITE_WIDTH))
                && (touchPosY > firstSelectPosY && touchPosY < (firstSelectPosY + BORDER_SPRITE_HEIGHT))){
            // 게임 재개
            return true;
        }
        else return false;
    }

    public boolean isSecondSelectPressed(double touchPosX, double touchPosY) {
        if((touchPosX > selectPosX && touchPosX < (selectPosX + BORDER_SPRITE_WIDTH))
                && (touchPosY > secondSelectPosY && touchPosY < (secondSelectPosY + BORDER_SPRITE_HEIGHT))){
            // 옵션 메뉴 활성화
            return true;
        }
        else return  false;
    }
}
