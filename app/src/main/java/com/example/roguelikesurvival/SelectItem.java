package com.example.roguelikesurvival;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.roguelikesurvival.object.Player;
import com.example.roguelikesurvival.object.item.PlusAtk;
import com.example.roguelikesurvival.object.item.PlusHp;
import com.example.roguelikesurvival.object.item.PlusSpeed;
import com.example.roguelikesurvival.object.item.RecoverHp;
import com.example.roguelikesurvival.object.item.RotateAttack;

import java.util.Random;

public class SelectItem {

    private static final float BACGROUND_SPRITE_WIDTH = 748;
    private static final float BACKGROUND_SPRITE_HEIGHT = 917;
    private static final float BORDER_SPRITE_WIDTH = 574;
    private static final float BORDER_SPRITE_HEIGHT = 169;
    private static final int NUMBER_OF_ITEMS = 5;
    private float selectPosX;
    private float firstSelectPosY;
    private float secondSelectPosY;
    private float thirdSelectPosY;
    private Context context;
    private Player player;
    private Bitmap backgroudBitmap = null;
    private Bitmap[] itemBitmap = new Bitmap[NUMBER_OF_ITEMS];
    private int[] randomItem = new int[3];
    boolean levelUp = false;
    boolean setRandom = false;
    private int color;
    private Paint levelUpMessage;
    private Paint name;
    private Paint explain;

    private PlusHp plusHp;
    private PlusAtk plusAtk;
    private RotateAttack rotateAttack;
    private PlusSpeed plusSpeed;
    private RecoverHp recoverHp;
    private int itemAttackPower = 1;

    public SelectItem(Context context, Player player, Camera camera) {

        this.context = context;
        this.player = player;
        color = ContextCompat.getColor(context, R.color.black);

        levelUpMessage = new Paint();
        levelUpMessage.setColor(color);
        levelUpMessage.setTextSize(60);
        levelUpMessage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        name = new Paint();
        name.setColor(color);
        name.setTextSize(35);
        name.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        explain = new Paint();
        explain.setColor(color);
        explain.setTextSize(30);
        explain.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));


        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        backgroudBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.select_item_background, bitmapOptions);
        itemBitmap[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.gui_plus_hp_select, bitmapOptions);
        itemBitmap[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.gui_plus_atk_select, bitmapOptions);
        itemBitmap[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.gui_rotate_attack_select, bitmapOptions);
        itemBitmap[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.gui_plus_speed_select, bitmapOptions);
        itemBitmap[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.gui_recover_hp_select, bitmapOptions);

        plusHp = new PlusHp(context, player, itemBitmap[0]);
        plusAtk = new PlusAtk(context, player, itemBitmap[1]);
        rotateAttack = new RotateAttack(context, player, itemBitmap[2]);
        plusSpeed = new PlusSpeed(context, player, itemBitmap[3]);
        recoverHp = new RecoverHp(context, player, itemBitmap[4]);
    }

    public void draw(Canvas canvas, Camera camera) {

        //랜덤으로 아이템 선택창 배치
        if(setRandom) {
            Random r = new Random();
            for (int i = 0; i < 3; i++) {
                randomItem[i] = r.nextInt(NUMBER_OF_ITEMS);
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

        //레벨업 메세지
        canvas.drawText("레벨업!", selectPosX + 195, firstSelectPosY - 90, levelUpMessage);
        //아이템 설명 출력
        explainItem(canvas, itemBitmap[randomItem[0]], plusHp, plusAtk, plusSpeed, recoverHp, selectPosX, firstSelectPosY);
        explainItem(canvas, itemBitmap[randomItem[1]], plusHp, plusAtk, plusSpeed, recoverHp, selectPosX, secondSelectPosY);
        explainItem(canvas, itemBitmap[randomItem[2]], plusHp, plusAtk, plusSpeed, recoverHp, selectPosX, thirdSelectPosY);

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

    //아이템 설명
    public void explainItem(Canvas canvas, Bitmap select, PlusHp plusHp, PlusAtk plusAtk, PlusSpeed plusSpeed, RecoverHp recoverHp, float posX, float posY){
        if(select.equals(plusHp.getBitmap())){
            canvas.drawText("체력증가", posX + 150, posY + 75, name);
            canvas.drawText("체력을 2 증가시킵니다.", posX + 150, posY + 115, explain);
        }
        else if(select.equals(plusAtk.getBitmap())){
            canvas.drawText("공격력증가", posX + 150, posY + 75, name);
            canvas.drawText("공격력을 2 증가시킵니다.", posX + 150, posY + 115, explain);
        }
        else if(select.equals(rotateAttack.getBitmap())){
            canvas.drawText("회전공격", posX + 150, posY + 75, name);
            if(!rotateAttack.getIsSelect())
                canvas.drawText("5초마다 회전공격을 합니다.", posX + 150, posY + 115, explain);
            else{
                canvas.drawText("아이템공격력을 2 증가시킵니다.", posX + 150, posY + 115, explain);
            }
        }
        else if(select.equals(plusSpeed.getBitmap())){
            canvas.drawText("속도증가", posX + 150, posY + 75, name);
            canvas.drawText("속도를 20 증가시킵니다.", posX + 150, posY + 115, explain);
        }
        else if(select.equals(recoverHp.getBitmap())){
            canvas.drawText("체력회복", posX + 150, posY + 75, name);
            canvas.drawText("체력을 max로 회복시킵니다.", posX + 150, posY + 115, explain);
        }
    }

    //아이템 선택 메서드
    public void updateItem(Bitmap select, PlusHp plusHp, PlusAtk plusAtk, RotateAttack rotateAttack, PlusSpeed plusSpeed, RecoverHp recoverHp){
        if(select.equals(plusHp.getBitmap())){
            plusHp.isSelect();
        }
        else if(select.equals(plusAtk.getBitmap())){
            plusAtk.isSelect();
        }
        else if(select.equals(rotateAttack.getBitmap())){
            if(!rotateAttack.getIsSelect())
                rotateAttack.isSelect();
            else {
                plusItemAttackPower(2);
            }
        }
        else if(select.equals(plusSpeed.getBitmap())){
            plusSpeed.isSelect();
        }
        else if(select.equals(recoverHp.getBitmap())){
            recoverHp.isSelect();
        }
    }

    public boolean isFirstSelectPressed(double touchPosX, double touchPosY) {
        if((touchPosX > selectPosX && touchPosX < (selectPosX + BORDER_SPRITE_WIDTH))
                && (touchPosY > firstSelectPosY && touchPosY < (firstSelectPosY + BORDER_SPRITE_HEIGHT))){
            updateItem(itemBitmap[randomItem[0]], plusHp, plusAtk, rotateAttack, plusSpeed, recoverHp);
            return true;
        }
        else return  false;
    }

    public boolean isSecondSelectPressed(double touchPosX, double touchPosY) {
        if((touchPosX > selectPosX && touchPosX < (selectPosX + BORDER_SPRITE_WIDTH))
                && (touchPosY > secondSelectPosY && touchPosY < (secondSelectPosY + BORDER_SPRITE_HEIGHT))){
            updateItem(itemBitmap[randomItem[1]], plusHp, plusAtk, rotateAttack, plusSpeed, recoverHp);
            return true;
        }
        else return  false;
    }

    public boolean isThirdSelectPressed(double touchPosX, double touchPosY) {
        if((touchPosX > selectPosX && touchPosX < (selectPosX + BORDER_SPRITE_WIDTH))
                && (touchPosY > thirdSelectPosY && touchPosY < (thirdSelectPosY + BORDER_SPRITE_HEIGHT))){
            updateItem(itemBitmap[randomItem[2]], plusHp, plusAtk, rotateAttack, plusSpeed, recoverHp);
            return true;
        }
        else return  false;
    }

    public RotateAttack getRotateAttack(){
        return rotateAttack;
    }

    public int getItemAttackPower(){
        return itemAttackPower;
    }

    public void plusItemAttackPower(int attackpower){
        itemAttackPower += attackpower;
    }
}
