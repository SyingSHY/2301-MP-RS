package com.example.roguelikesurvival;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.object.Player;

import java.util.Random;

public class SelectItem {

    private static final float BACGROUND_SPRITE_WIDTH = 748;
    private static final float BACKGROUND_SPRITE_HEIGHT = 917;
    private static final float BORDER_SPRITE_WIDTH = 574;
    private static final float BORDER_SPRITE_HEIGHT = 169;
    private float selectPosX;
    private float firstSelectPosY;
    private float secondSelectPosY;
    private float thirdSelectPosY;
    private Context context;
    private Player player;
    private Bitmap backgroudBitmap = null;
    private Bitmap[] itemBitmap = new Bitmap[3];
    private int[] randomItem = new int[3];
    boolean levelUp = false;
    boolean setRandom = false;

    public SelectItem(Context context, Player player, Camera camera) {

        this.context = context;
        this.player = player;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        backgroudBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.select_item_background, bitmapOptions);
        itemBitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.gui_plus_hp_select, bitmapOptions);
        itemBitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.gui_plus_atk_select, bitmapOptions);
        itemBitmap[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.gui_blank_select, bitmapOptions);
    }

    public void draw(Canvas canvas, Camera camera) {

        //랜덤으로 아이템 선택창 배치
        if(setRandom) {
            Random r = new Random();
            for (int i = 0; i < 3; i++) {
                randomItem[i] = r.nextInt(3);
                for (int j = 0; j < i; j++) {
                    if (randomItem[i] == randomItem[j])
                        i--;
                }
            }
            setRandom = false;
        }

        //아이템 선택 테두리(아이템창) 위치 설정
        selectPosX = (float) camera.gameToScreenCoordinateX(player.getPositionX()) - (BORDER_SPRITE_WIDTH / 2) - 3;
        firstSelectPosY = (float) camera.gameToScreenCoordinateY(player.getPositionY()) - (BORDER_SPRITE_HEIGHT / 2) - 120;
        secondSelectPosY = (float) camera.gameToScreenCoordinateY(player.getPositionY()) - (BORDER_SPRITE_HEIGHT / 2) + 70;
        thirdSelectPosY = (float) camera.gameToScreenCoordinateY(player.getPositionY()) - (BORDER_SPRITE_HEIGHT / 2) + 260;

        //선택창 배경 draw
        canvas.drawBitmap(backgroudBitmap, (float) camera.gameToScreenCoordinateX(player.getPositionX()) - (BACGROUND_SPRITE_WIDTH / 2),
                (float) camera.gameToScreenCoordinateY(player.getPositionY()) - (BACKGROUND_SPRITE_HEIGHT / 2), null);
        //선택 아이템창 draw
        canvas.drawBitmap(itemBitmap[randomItem[0]], selectPosX, firstSelectPosY, null);
        canvas.drawBitmap(itemBitmap[randomItem[1]], selectPosX, secondSelectPosY, null);
        canvas.drawBitmap(itemBitmap[randomItem[2]], selectPosX, thirdSelectPosY, null);

        int color = ContextCompat.getColor(context, R.color.black);

        Paint levelUpMessage = new Paint();
        levelUpMessage.setColor(color);
        levelUpMessage.setTextSize(60);
        levelUpMessage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        Paint name = new Paint();
        name.setColor(color);
        name.setTextSize(35);
        name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        Paint explain = new Paint();
        explain.setColor(color);
        explain.setTextSize(30);
        explain.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        //레벨업 메세지
        canvas.drawText("레벨업!", selectPosX + 195, firstSelectPosY - 90, levelUpMessage);
//        //아이템 설명 출력
//        canvas.drawText("체력증가", selectPosX + 150, firstSelectPosY + 75, name);
//        canvas.drawText("체력을 2 증가시킵니다.", selectPosX + 150, firstSelectPosY + 115, explain);
    }

    public void update(){

    }

    public boolean isLevelUp(){
        return levelUp;
    }

    public void setLevelUp(boolean bool){
        levelUp = bool;
        setRandom = bool;
    }

    public void levelUp(){
        levelUp = true;
        setRandom = true;
    }

    public boolean isFirstSelectPressed(double touchPosX, double touchPosY) {
        if((touchPosX > selectPosX && touchPosX < (selectPosX + BORDER_SPRITE_WIDTH))
                && (touchPosY > firstSelectPosY && touchPosY < (firstSelectPosY + BORDER_SPRITE_HEIGHT))){
            return true;
        }
        else return  false;
    }

    public boolean isSecondSelectPressed(double touchPosX, double touchPosY) {
        if((touchPosX > selectPosX && touchPosX < (selectPosX + BORDER_SPRITE_WIDTH))
                && (touchPosY > secondSelectPosY && touchPosY < (secondSelectPosY + BORDER_SPRITE_HEIGHT))){
            return true;
        }
        else return  false;
    }

    public boolean isThirdSelectPressed(double touchPosX, double touchPosY) {
        if((touchPosX > selectPosX && touchPosX < (selectPosX + BORDER_SPRITE_WIDTH))
                && (touchPosY > thirdSelectPosY && touchPosY < (thirdSelectPosY + BORDER_SPRITE_HEIGHT))){
            return true;
        }
        else return  false;
    }
}
