package com.example.roguelikesurvival;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.roguelikesurvival.gamepanel.InfiniteBackground;
import com.example.roguelikesurvival.gamepanel.Joystick;
import com.example.roguelikesurvival.gamepanel.Performance;
import com.example.roguelikesurvival.gamepanel.ReStart;
import com.example.roguelikesurvival.gamepanel.GameTimer;
import com.example.roguelikesurvival.object.Enemy;
import com.example.roguelikesurvival.object.Player;
import com.example.roguelikesurvival.object.Spell;

import java.util.ArrayList;
import java.util.List;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;
    private final Player player;
    private final Joystick joystick;
    public static List<Enemy> enemyList = new ArrayList<Enemy>();
    public List<Spell> spellList = new ArrayList<Spell>();
    private int joystckPointerId = 0;
    public int numberOfSpellsToCast = 0;
    private Performance performance;
    private Camera camera;
    private InfiniteBackground background;
    private EnemySpawn enemySpawn;
    private GameTimer gameTimer;

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    public Game(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        //시간 설정
        gameTimer = new GameTimer(context);

        //게임 패널 초기화
        performance = new Performance(context, gameLoop);
        joystick = new Joystick(170, 800, 100, 60);


        //오브젝트 초기설정
        player = new Player(getContext(), joystick, 500, 500, 30);
        enemySpawn = new EnemySpawn(this, player, camera, gameTimer);

        //배경 설정
        background = new InfiniteBackground(context, player);

        //카메라 시점을 플레이어가 중앙에 오게 설정
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics((displayMetrics));
        camera = new Camera(displayMetrics.widthPixels, displayMetrics.heightPixels, player);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick.getIsPressed()) {
                    numberOfSpellsToCast++;
                } else if (joystick.isPressed((double) event.getX(), (double) event.getY())) {
                    joystckPointerId = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                } else {
                    numberOfSpellsToCast++;
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                if (joystick.getIsPressed()) {
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (joystckPointerId == event.getPointerId(event.getActionIndex())) {
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //배경 그리기
        background.draw(canvas, camera);

        //오브젝트 그리기
        player.draw(canvas, camera);

        for (Enemy enemy : enemyList) {
            enemy.draw(canvas, camera);
        }

        for (Spell spell : spellList) {
            spell.draw(canvas, camera);
        }

        // 게임패널 그리기
        joystick.draw(canvas);

        //시간 출력
        gameTimer.draw(canvas);

    }

    public void update() {

        joystick.update();
        player.update();

        enemySpawn.update(camera);

        camera.update();

        if (player.getHealthPoint() <= 0) {
            checkHP();
        }

        background.update(camera);
    }

    public void checkHP() {
        if (player.getHealthPoint() <= 0) {
            enemyList.clear();
            Intent intent = new Intent(getContext(), ReStart.class);
            getContext().startActivity(intent);
            ((Activity) getContext()).finish();
        }
    }

    public void pause() {
        gameLoop.stopLoop();
    }

    public List<Enemy> getEnemyList() {
        return enemyList;
    }
}

